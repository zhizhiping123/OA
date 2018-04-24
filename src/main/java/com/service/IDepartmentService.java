package com.service;

import com.common.ServerResponse;
import com.entity.Department;

import java.util.List;


/**
 * Created by jiale on 2017/10/10.
 */
public interface IDepartmentService {

    /**
     * 查询一级部门(分公司)
     * @return
     */
    ServerResponse listTopLevelDepartment();

    /**
     * 插入一个一级部门
     */
    ServerResponse insertTopLevelDepartment(String headDepartmentName);

    /**
     * 根据部门ID查询其子一级部门
     * @return
     */
    ServerResponse listChildDepartmentByDeptId(String deptId);

    /**
     * 根据部门ID判断是否叶子节点
     * @param deptId
     * @return
     */
    ServerResponse isLeafByDeptId(String deptId);

    /**
     * 插入一个部门(非一级部门)
     */
    ServerResponse insertDepartment(String parentId,String departmentName);


    /**
     * 根据部门ID改部门名称
     * @param deptId
     * @param deptName
     * @return
     */
    ServerResponse updateByDeptId(String deptId,String deptName);

    ServerResponse updateHeadDepartmentByDeptId(String deptId,String deptName);

    /**
     * 根据部门ID删除部门,递归删除
     * @param deptId
     * @return
     */
    ServerResponse deleteAllByDeptId(String deptId);

    Department findDepartmentById(String departmentId);

}
