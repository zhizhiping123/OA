package com.dao;

import com.entity.Department;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by jiale on 2017/10/9.
 */

@Repository
public interface DepartmentRepository extends CrudRepository<Department,Long>{

    Department findByDepartmentId(String departmentId);

    List<Department> findByParentId(String deptId);

    @Query("select count(d.parentId) from Department d  where d.parentId = :parentId")
    int countByParentId(@Param("parentId") String parentId);

    void deleteByDepartmentId(String departmentId);

    @Modifying
    @Query("update Department d set d.departmentName = :departmentName where d.departmentId = :departmentId")
    int updateByDeptId(@Param("departmentName") String departmentName, @Param("departmentId") String departmentId);


    Department findByDepartmentNameAndParentId(@Param("departmentName") String departmentName
            , @Param("parentId") String parentId);




}
