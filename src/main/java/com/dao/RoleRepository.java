package com.dao;

import com.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiale on 2017/10/9.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role,Long>{
   /* @Query("select r from Role r left join r.users u where u.userId=:userId")
    List<Role> findAllByUserId(@Param("userId") String userId);*/
    Role findByRoleZhName(String zhName);
    Role findByRoleId(Long roleId);
    Role findByRoleName(String roleName);

}
