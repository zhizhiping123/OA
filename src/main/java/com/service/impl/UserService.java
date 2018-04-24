package com.service.impl;

import com.common.SearchUserCondition;
import com.common.ServerResponse;
import com.common.SystemParams;
import com.dao.DepartmentRepository;
import com.dao.RoleRepository;
import com.dao.UserRepository;
import com.dto.PageDto;
import com.dto.UserDto;
import com.entity.Department;
import com.entity.Role;
import com.entity.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.service.IUserService;
import com.util.UserRoleUtil;
import org.activiti.engine.IdentityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jiale on 2017/10/9.
 */
@Service("IUserService")
@Transactional
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // activiti 服务

    @Autowired
    private IdentityService identityService;


    ////////////////////////


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername  "+s);
        return userRepository.findByUserId(s);
    }

    /*@Override
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }*/


    ////////////////











    @Override
    public ServerResponse insertUser(String deptId, List<Long> roleIdList,String username) {
        User user = new User();
        UserDto userDto = new UserDto();

        // 查出 dept
        Department department = departmentRepository.findByDepartmentId(deptId);

        if(department == null){
            return ServerResponse.buildErrorMsg("部门信息有误");
        }
        // 查出 roles
        List<Role> roleList = Lists.newArrayList();
        for(Long roleId : roleIdList){
            Role role = roleRepository.findByRoleId(roleId);
            if(role != null){
                roleList.add(role);
            }
        }
        if(roleList.size() == 0){
            return ServerResponse.buildErrorMsg("角色信息有误");
        }
        // 根据 deptId 生成 userId
        String userId = deptId + (userRepository.countByDeptId(deptId) + 1);

        // 注入
        user.setUserId(userId);
        user.setDepartment(department);
        user.setRoles(roleList);
        user.setPassword(DigestUtils.md5DigestAsHex(SystemParams.INIT_PASSWORD.getBytes()));
        user.setUsername(username);
        user.setFlag(SystemParams.ON_JOB);
        user.setCreateTime(new Date(System.currentTimeMillis()));
        user.setUpdateTime(new Date(System.currentTimeMillis()));

        // 持久化
        userRepository.save(user);
        // 持久化处理相应的 activiti 数据表
        handleActivitiTable(user);
        // 转 dto
        UserToUserDto(user,userDto);

        return ServerResponse.buildSuccessData(userDto);

    }

    /**
     * 持久化处理相应的 activiti 数据表
     * @param user
     */
    private void handleActivitiTable(User user){
        // 插入 user
        org.activiti.engine.identity.User actUser = identityService.newUser(user.getUserId());
        actUser.setPassword(DigestUtils.md5DigestAsHex(SystemParams.INIT_PASSWORD.getBytes()));
        identityService.saveUser(actUser);
        // 插入 memberShip
        List<Role> roleList = user.getRoles();
        if(roleList != null){

            for(Role role : roleList){
                // 生成 actRoleId
                String actRoleId = UserRoleUtil.buildActRoleId(user.getDepartment().getDepartmentId()
                        , role.getRoleId());
                identityService.createMembership(user.getUserId(),actRoleId);

            }
        }

    }


    @Override
    public ServerResponse findByUserId(String userId) {
        User user = userRepository.findByUserId(userId);
        if(user == null){
            return ServerResponse.buildErrorMsg("用户ID不存在");
        }
        UserDto userDto = new UserDto();
        UserToUserDto(user,userDto);
        return ServerResponse.buildSuccessData(userDto);
    }

    @Override
    public ServerResponse listUserByDept(String deptId,int page,int size) {
        Pageable pageable = new PageRequest(page,size);
        Page<User> userPage = userRepository.findByDeptId(deptId,pageable);
        PageDto pageDto = new PageDto();
        PageToPageDto(userPage,pageDto);
        return ServerResponse.buildSuccessData(pageDto);
    }

    @Override
    public ServerResponse listUserByRole(Long roleId,int page,int size) {
        Pageable pageable = new PageRequest(page,size);
        Page<User> userPage = userRepository.findByRoleId(roleId,pageable);
        PageDto pageDto = new PageDto();
        PageToPageDto(userPage,pageDto);
        return ServerResponse.buildSuccessData(pageDto);
    }


    @Override
    public ServerResponse quitJobForUserId(String userId) {
        int result = userRepository.updateUserFlag(SystemParams.QUIT_JOB,userId);
        if(result > 0){
            return ServerResponse.buildSuccessMsg("离职修改成功");
        }
        return ServerResponse.buildErrorMsg("离职修改失败");
    }

    @Override
    public ServerResponse updateByUserId(User user) {
        String userId = user.getUserId();
        if(!StringUtils.isEmpty(userId)){
            User savedUser = userRepository.findByUserId(userId);
            if(savedUser != null){
                // 设置需要修改的信息到被持久化托管状态的 savedUser 中
                handleUpdateUser(user,savedUser);
                userRepository.save(savedUser);
                UserDto userDto = new UserDto();
                UserToUserDto(savedUser,userDto);
                return ServerResponse.buildSuccessData(userDto);
            }
        }
        return ServerResponse.buildErrorMsg("用户ID不存在");
    }


    /**
     * 使用jpa 动态查询
     * 查询条件：
     * username
     * userId
     * deptId
     * pageNo (默认为 0)
     * pageSize (默认为 5)
     * @param condition
     * @return
     */
    @Override
    public ServerResponse listUserByCondition(SearchUserCondition condition) {
        Map<String,String> paramsMap = Maps.newHashMap();
        paramsMap.put("username",condition.getUsername());
        paramsMap.put("userId",condition.getUserId());
        paramsMap.put("deptId",condition.getDeptId());

        PageRequest pageRequest = new PageRequest(condition.getPageNo(),condition.getPageSize());
        Page<User> userPage =  userRepository.findAll(UserRepository.Specs.buQueryParams(paramsMap),pageRequest);

        // 转DTO
        PageDto pageDto = new PageDto();
        pageDto.setPageIndex(userPage.getNumber());
        pageDto.setPageSize(userPage.getSize());
        pageDto.setTotalPage(userPage.getTotalPages());
        List<UserDto> userDtoList = Lists.newArrayList();
        Iterator<User> userIterator = userPage.iterator();
        while (userIterator.hasNext()){
            User user = userIterator.next();
            UserDto userDto = new UserDto();
            UserToUserDto(user,userDto);
            userDtoList.add(userDto);
        }
        pageDto.setObj(userDtoList);
        return ServerResponse.buildSuccessData(pageDto);
    }

    /**
     * 设置需要修改的信息到被持久化托管状态的 savedUser 中
     * todo 修改部门与角色的数据一致性校验有待解决
     * @param user
     * @param savedUser
     */
    private void handleUpdateUser(final User user,final User savedUser){

        if(user.getAnswer1() != null){
            savedUser.setAnswer1(user.getAnswer1());
        }
        if(user.getAnswer2() != null){
            savedUser.setAnswer2(user.getAnswer2());
        }
        if(user.getEmail() != null){
            savedUser.setEmail(user.getEmail());
        }
        if(user.getPhoneNum() != null){
            savedUser.setPhoneNum(user.getPhoneNum());
        }
        if(user.getUsername() != null){
            savedUser.setUsername(user.getUsername());
        }
        if(user.getPassword() != null){
            savedUser.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        if(user.getDepartment() != null){
            Department department = departmentRepository.findByDepartmentId(user.getDepartment().getDepartmentId());
            if(department != null){
                savedUser.setDepartment(department);
            }
        }
        if(user.getRoles() != null){
            List<Role> roleList = Lists.newArrayList();
            for(Role role : user.getRoles()){
                Role savedRole = roleRepository.findByRoleId(role.getRoleId());
                if(savedRole != null){
                    roleList.add(savedRole);
                }
            }
            if(roleList.size() >0){
                updateMemberShip(savedUser,roleList);
                savedUser.setRoles(roleList);
            }
        }

        savedUser.setUpdateTime(new Date(System.currentTimeMillis()));
    }

    /**
     * 修改 activiti 的角色用户关联表
     */
    private void updateMemberShip(User savedUser,List<Role> saveRoleList){
        String userId = savedUser.getUserId();
        String deptId = savedUser.getDepartment().getDepartmentId();
        // 删除原来角色用户关联
        for(Role role : savedUser.getRoles()){
            String actRoleId = UserRoleUtil.buildActRoleId(deptId, role.getRoleId());
            identityService.deleteMembership(userId,actRoleId);
        }
        //  插入原来角色用户关联
        for(Role role : saveRoleList){
            String actRoleId = UserRoleUtil.buildActRoleId(deptId, role.getRoleId());
            identityService.createMembership(userId,actRoleId);
        }
    }



    private void UserToUserDto(final User user,final UserDto userDto){
        BeanUtils.copyProperties(user,userDto);
        Department department = user.getDepartment();
        if(department != null){
            userDto.setDeptName(department.getDepartmentName());
        }
        List<Role> roleList = user.getRoles();
        List<String> roleNames = Lists.newArrayList();
        if(roleList != null){
            for(Role role : roleList){
                roleNames.add(role.getRoleZhName());
            }
        }
        userDto.setRoleName(roleNames);
    }

    private void PageToPageDto(final Page<User> userPage,final PageDto pageDto){
        pageDto.setTotalPage(userPage.getTotalPages());
        pageDto.setPageSize(userPage.getSize());
        pageDto.setPageIndex(userPage.getNumber());

        List<User> userList = userPage.getContent();
        List<UserDto> userDtoList = Lists.newArrayList();
        for(User user : userList){
            UserDto userDto = new UserDto();
            UserToUserDto(user,userDto);
            userDtoList.add(userDto);
        }
        pageDto.setObj(userDtoList);
    }







}
