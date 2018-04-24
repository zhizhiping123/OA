import com.common.ServerResponse;
import com.common.SystemParams;
import com.dao.DepartmentRepository;
import com.dao.RoleRepository;
import com.dao.UserRepository;
import com.dto.DepartmentDto;
import com.entity.Role;
import com.entity.User;
import com.google.common.collect.Lists;
import com.service.IDepartmentService;
import com.service.IRoleService;
import com.service.IUserService;
import com.util.UserRoleUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by jiale on 2017/10/11.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-activiti.xml","/applicationContext-orm.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class InitSystemData {

    @Autowired
    private IUserService userService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IRoleService roleService;

    @Test
    public void initData(){
        initRoleData();
        initDeptData();
        initUserData();
    }



    @Test
    public void initRoleData(){
        roleService.insertRole("超级管理员","admin");
        roleService.insertRole("董事长","boss");
        roleService.insertRole("总经理","generalManager");
        roleService.insertRole("部门经理","manager");
        roleService.insertRole("部门助理","assistant");
        roleService.insertRole("员工","worker");
        roleService.insertRole("保安","doorkeeper");
    }

     @Test
    public void initDeptData(){
        departmentService.insertTopLevelDepartment("广州分公司");
        departmentService.insertTopLevelDepartment("东莞分公司");
        departmentService.insertTopLevelDepartment("珠海分公司");
        departmentService.insertTopLevelDepartment("深圳分公司");

        ServerResponse serverResponse = departmentService.listTopLevelDepartment();
        if(serverResponse.isSuccess()){
            List<DepartmentDto> departmentDtoList = (List<DepartmentDto>) serverResponse.getData();

            for(DepartmentDto departmentDto : departmentDtoList ){

                departmentService.insertDepartment(departmentDto.getDepartmentId(),"普通部门");
                departmentService.insertDepartment(departmentDto.getDepartmentId(),"人事行政部");
                departmentService.insertDepartment(departmentDto.getDepartmentId(),"财务部");
                departmentService.insertDepartment(departmentDto.getDepartmentId(),"后勤部");
            }
        }

    }

    @Test
    public void initUserData(){


        ServerResponse serverResponse = departmentService.listTopLevelDepartment();
        if(serverResponse.isSuccess()){
            List<DepartmentDto> headDepartmentDtoList = (List<DepartmentDto>) serverResponse.getData();

            for(DepartmentDto departmentDto : headDepartmentDtoList ){

                ServerResponse deptResponse = departmentService.listChildDepartmentByDeptId(departmentDto.getDepartmentId());
                if(deptResponse.isSuccess()){
                    List<DepartmentDto> departmentDtoList = (List<DepartmentDto>) deptResponse.getData();

                    for(DepartmentDto deptDto : departmentDtoList){

                        int i = 4;

                        System.out.println(deptDto.getDepartmentId());

                        if("普通部门".equals(deptDto.getDepartmentName())){

                            userService.insertUser(deptDto.getDepartmentId()
                                    ,Lists.newArrayList(new Long(3)),"分公司总经理_");

                            userService.insertUser(deptDto.getDepartmentId()
                                    ,Lists.newArrayList(new Long(4)),"普通部门经理");

                            userService.insertUser(deptDto.getDepartmentId()
                                    ,Lists.newArrayList(new Long(5)),"普通部门助理");

                            userService.insertUser(deptDto.getDepartmentId()
                                    ,Lists.newArrayList(new Long(6)),"普通员工");


                        }else{
                            userService.insertUser(deptDto.getDepartmentId(), Lists.newArrayList(new Long(i))
                                    ,deptDto.getDepartmentName()+buildUsername(i));
                            i++;
                            userService.insertUser(deptDto.getDepartmentId(), Lists.newArrayList(new Long(i))
                                    ,deptDto.getDepartmentName()+buildUsername(i));
                            i++;

                            if("后勤部".equals(deptDto.getDepartmentName())){
                                userService.insertUser(deptDto.getDepartmentId(), Lists.newArrayList(new Long(7))
                                        ,deptDto.getDepartmentName()+"保安");
                            }
                        }
                    }

                }
                break;
            }
        }
    }

    private String buildUsername(int i){
        switch (i){
            case 3:
                return "小明";
            case 4:
                return "小青";
            case 5:
                return "张三";
            case 6:
                return "李四";
            case 7:
                return "王五";
        }
        return "小强" + i;
    }

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void initBossData(){

        User user = new User();
        user.setUsername("莫董事");
        user.setUserId("201700");
        user.setPassword(SystemParams.INIT_PASSWORD);
        user.setCreateTime(new Date(System.currentTimeMillis()));
        user.setUpdateTime(new Date(System.currentTimeMillis()));

        Role role = new Role();
        role.setRoleId(new Long(2));
        user.setRoles(Lists.newArrayList(role));

        userRepository.save(user);

        org.activiti.engine.identity.User user1 = identityService.newUser(user.getUserId());
        user1.setPassword(user.getPassword());
        identityService.saveUser(user1);

        Group group = identityService.newGroup("000000");
        group.setName("董事长");

        identityService.saveGroup(group);

        identityService.createMembership(user1.getId(),"000000");

    }



    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;







}
