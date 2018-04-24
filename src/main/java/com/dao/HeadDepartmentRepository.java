package com.dao;

import com.entity.HeadDepartment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiale on 2017/10/10.
 */
@Repository
public interface HeadDepartmentRepository extends CrudRepository<HeadDepartment,Long>{

    HeadDepartment findByHeadDepartmentId(String headDepartmentId);

    @Modifying
    @Query("update HeadDepartment hd set hd.headDepartmentName = :headDepartmentName where hd.headDepartmentId = :headDepartmentId")
    int updateByDeptId(@Param("headDepartmentName") String headDepartmentName, @Param("headDepartmentId") String headDepartmentId);

}
