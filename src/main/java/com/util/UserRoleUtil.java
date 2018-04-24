package com.util;

import com.dao.DepartmentRepository;
import com.dao.RoleRepository;
import com.entity.Department;
import com.entity.Role;
import org.springframework.util.StringUtils;

/**
 * Created by jaile on 2017/10/11.
 */
public class UserRoleUtil {

    /**
     * 根据部门ID和角色ID生成 activiti 角色ID
     * @param deptId
     * @param roleId
     * @return
     */
    public static String buildActRoleId(String deptId,Long roleId){

        if(StringUtils.isEmpty(deptId) || roleId == null){
            return null;
        }

        if(roleId > 10 ){
            return "_" + deptId + roleId;
        }
        return "_" + deptId + "0" + roleId;
    }

    /**
     * 注意总经理在普通部门里
     * 注意总经理在普通部门里
     * 注意总经理在普通部门里
     *
     * 根据部门ID和角色类别生成上一级领导 activiti 角色ID
     * @param deptId
     * @param roleType 需要生成上一级领导的角色类别
     * @return
     */
    public static String buildLeaderActRoleId(String deptId, String roleType, RoleRepository roleRepository){

        Role role = roleRepository.findByRoleName(roleType);
        if(role == null){
            return null;
        }
        Long roleId = role.getRoleId();
        return buildActRoleId(deptId,roleId);
    }

    private static String findHrDeptId(String deptId,DepartmentRepository departmentRepository){
        Department department = departmentRepository.findByDepartmentId(deptId);
        if(department == null){
            return null;
        }
        String parentId = department.getParentId();
        Department dept =  departmentRepository.findByDepartmentNameAndParentId("人事行政部",parentId);
        if(dept == null){
            return null;
        }
        return dept.getDepartmentId();
    }

    private static String findLogisticsDeptId(String deptId,DepartmentRepository departmentRepository){
        Department department = departmentRepository.findByDepartmentId(deptId);
        if(department == null){
            return null;
        }
        String parentId = department.getParentId();

        Department dept =  departmentRepository.findByDepartmentNameAndParentId("后勤部",parentId);
        if(dept == null){
            return null;
        }
        return dept.getDepartmentId();
    }


    private static String findCommonDeptId(String deptId,DepartmentRepository departmentRepository){

        Department department = departmentRepository.findByDepartmentId(deptId);
        if(department == null){
            return null;
        }
        String parentId = department.getParentId();

        Department dept =  departmentRepository.findByDepartmentNameAndParentId("普通部门",parentId);
        if(dept == null){
            return null;
        }
        return dept.getDepartmentId();

    }


    public static String buildgeneralManagerByDeptId(String deptId, DepartmentRepository departmentRepository
            ,RoleRepository roleRepository){
        String hrDeptId = findHrDeptId(deptId,departmentRepository);
        return buildLeaderActRoleId(hrDeptId,"generalManager",roleRepository);
    }


    public static String buildHrManagerByDeptId(String deptId, DepartmentRepository departmentRepository
            ,RoleRepository roleRepository){
        String hrDeptId = findHrDeptId(deptId,departmentRepository);
        return buildLeaderActRoleId(hrDeptId,"manager",roleRepository);
    }




    public static String buildHrAssistantByDeptId(String deptId,DepartmentRepository departmentRepository
            ,RoleRepository roleRepository){

        String hrDeptId = findHrDeptId(deptId,departmentRepository);
        return buildLeaderActRoleId(hrDeptId,"assistant",roleRepository);

    }

    public static String buildDoorKeeperByDeptId(String deptId,DepartmentRepository departmentRepository
            ,RoleRepository roleRepository){

        String logisticsDeptId = findLogisticsDeptId(deptId,departmentRepository);
        return buildLeaderActRoleId(logisticsDeptId,"doorkeeper",roleRepository);
    }







}
