package com.service.impl;

import com.common.ServerResponse;
import com.dao.DepartmentRepository;
import com.dao.HeadDepartmentRepository;
import com.dao.RoleRepository;
import com.dto.DepartmentDto;
import com.entity.Department;
import com.entity.HeadDepartment;
import com.entity.Role;
import com.google.common.collect.Lists;
import com.service.IDepartmentService;
import com.util.UserRoleUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jiale on 2017/10/10.
 */
@Service("IDepartmentService")
@Transactional
public class DepartmentService implements IDepartmentService{

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private HeadDepartmentRepository headDepartmentRepository;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ServerResponse listTopLevelDepartment() {
        Iterable<HeadDepartment> departmentIterable = headDepartmentRepository.findAll();
        Iterator<HeadDepartment> iterator = departmentIterable.iterator();
        List<DepartmentDto> departmentDtoList = Lists.newArrayList();
        while (iterator.hasNext()){
            HeadDepartment headDepartment = iterator.next();
            DepartmentDto departmentDto = new DepartmentDto();
            headDepartmentToDepartmentDto(headDepartment,departmentDto);
            departmentDtoList.add(departmentDto);
        }
        return ServerResponse.buildSuccessData(departmentDtoList);
    }

    @Override
    public ServerResponse insertTopLevelDepartment(String headDepartmentName) {

        // 生成 deptId
        long count = headDepartmentRepository.count() + 1;
        String deptId;
        if(count < 10){
            deptId = "20170"+ count;
        }else{
            deptId = "2017" + count;
        }

        HeadDepartment headDepartment = new HeadDepartment();
        headDepartment.setHeadDepartmentName(headDepartmentName);
        headDepartment.setHeadDepartmentId(deptId);
        headDepartment.setCreateTime(new Date(System.currentTimeMillis()));
        headDepartment.setUpdateTime(new Date(System.currentTimeMillis()));
        headDepartmentRepository.save(headDepartment);

        DepartmentDto departmentDto = new DepartmentDto();
        headDepartmentToDepartmentDto(headDepartment,departmentDto);

        return ServerResponse.buildSuccessData(departmentDto);
    }

    @Override
    public ServerResponse listChildDepartmentByDeptId(String deptId) {

        List<Department> departmentList =  departmentRepository.findByParentId(deptId);
        List<DepartmentDto> departmentDtoList = Lists.newArrayList();
        if(departmentList != null){
            for(Department department :departmentList){
                DepartmentDto departmentDto = new DepartmentDto();
                departmentToDepartmentDto(department,departmentDto);
                departmentDtoList.add(departmentDto);
            }
        }
        return ServerResponse.buildSuccessData(departmentDtoList);
    }

    @Override
    public ServerResponse isLeafByDeptId(String deptId) {
        int count = departmentRepository.countByParentId(deptId);
        if(count > 0){
            return ServerResponse.buildSuccessData(false);
        }
        return ServerResponse.buildSuccessData(true);
    }

    /**
     * 只接受尾插入
     * @param parentId
     * @param departmentName
     * @return
     */
    @Override
    public ServerResponse insertDepartment(String parentId, String departmentName) {

        // 生成departmentId
        int count = departmentRepository.countByParentId(parentId) + 1 ;
        String departmentId;
        if(count > 10){
            departmentId = parentId + count;
        }else {
            departmentId = parentId + "0" + count;
        }

        Department department = new Department();
        department.setParentId(parentId);
        department.setDepartmentId(departmentId);
        department.setDepartmentName(departmentName);
        department.setCreateTime(new Date(System.currentTimeMillis()));
        department.setUpdateTime(new Date(System.currentTimeMillis()));
        // 持久化
        departmentRepository.save(department);
        saveActGroup(departmentName,departmentId);

        DepartmentDto departmentDto = new DepartmentDto();
        departmentToDepartmentDto(department,departmentDto);
        return ServerResponse.buildSuccessData(departmentDto);
    }

    /**
     * 根据 deptId 持久化 activiti 中的用户组
     * @param deptName
     * @param deptId
     */
    private void saveActGroup(String deptName,String deptId){

        Iterable<Role> roleIterable = roleRepository.findAll();
        Iterator<Role> roleIterator = roleIterable.iterator();

        while (roleIterator.hasNext()){
            Role role = roleIterator.next();

            if("boss".equals(role.getRoleName()) || "admin".equals(role.getRoleName())){
                continue;
            }

            if("generalManager".equals(role.getRoleName())){
                if(!"普通部门".equals(deptName)){
                    continue;
                }
            }

            if("doorkeeper".equals(role.getRoleName())){
                if(!"后勤部".equals(deptName)){
                    continue;
                }
            }

            String actRoleId = UserRoleUtil.buildActRoleId(deptId,role.getRoleId());
            Group group = identityService.newGroup(actRoleId);
            group.setName(deptName+role.getRoleZhName());
            identityService.saveGroup(group);
        }

    }


    /**
     * 只接受修改部门名称
     * @param deptId
     * @param deptName
     * @return
     */
    @Override
    public ServerResponse updateByDeptId(String deptId, String deptName) {

        departmentRepository.updateByDeptId(deptName,deptId);
        Department department = departmentRepository.findByDepartmentId(deptId);
        DepartmentDto departmentDto = new DepartmentDto();
        departmentToDepartmentDto(department,departmentDto);
        return ServerResponse.buildSuccessData(departmentDto);
    }

    @Override
    public ServerResponse updateHeadDepartmentByDeptId(String deptId, String deptName) {

        headDepartmentRepository.updateByDeptId(deptName,deptId);
        HeadDepartment headDepartment = headDepartmentRepository.findByHeadDepartmentId(deptId);
        DepartmentDto departmentDto  = new DepartmentDto();
        headDepartmentToDepartmentDto(headDepartment,departmentDto);
        return ServerResponse.buildSuccessData(departmentDto);
    }

    /**
     * 只接受递归删除
     * @param deptId
     * @return
     */
    @Override
    public ServerResponse deleteAllByDeptId(String deptId) {

        deleteByDeptId(deptId);

        return ServerResponse.buildSuccessData("删除成功");
    }

    /**
     * 递归删除部门
     * @param deptId
     */
    private void deleteByDeptId(String deptId){

        List<Department> departmentList = departmentRepository.findByParentId(deptId);
        if(departmentList != null){
            for(Department department : departmentList){
                deleteByDeptId(department.getDepartmentId());
            }
        }
        departmentRepository.deleteByDepartmentId(deptId);
    }

    private void headDepartmentToDepartmentDto(HeadDepartment headDepartment
            ,DepartmentDto departmentDto){
        BeanUtils.copyProperties(headDepartment,departmentDto);
        departmentDto.setLeaf(false);
        departmentDto.setDepartmentId(headDepartment.getHeadDepartmentId());
        departmentDto.setDepartmentName(headDepartment.getHeadDepartmentName());
    }

    private void departmentToDepartmentDto(Department department,DepartmentDto departmentDto){

        BeanUtils.copyProperties(department,departmentDto);
        // 查询是否为叶子节点
        ServerResponse serverResponse = isLeafByDeptId(department.getDepartmentId());
        departmentDto.setLeaf((Boolean) serverResponse.getData());
    }


    /*根据部门ID查找部门*/
    public Department findDepartmentById(String departmentId){
        return departmentRepository.findByDepartmentId(departmentId);
    }


}
