package com.dao;

import com.entity.Leave;
import com.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave,Long>,JpaSpecificationExecutor<Leave>{

      //@Query("delete from Leave l where l.leaveId=:leaveId")
      //@Modifying
      void deleteByLeaveId(@Param("leaveId") String LeaveId);
      Leave findByLeaveId(String leaveId);
      @Query("select l from Leave l left join l.users u where u.userId=:userId and l.leaveId=:leaveId")
      Leave findByUserIdAndLeaveId(@Param("userId") String userId,@Param("leaveId") String leaveId);
      @Query("select l from Leave l left join  l.users u where u.userId=:userId")
      Page<Leave> findByUserId(@Param("userId") String userId,Pageable pageable);
      @Query("select l from Leave l left join  l.users u where u.userId=:userId")
      List<Leave> findByUserId(@Param("userId")String userId);

}
