package com.dao;

import com.entity.Menu;
import com.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MenuRepository extends CrudRepository<Menu,Long>{

   @Query("select m from Menu m left join fetch m.roles r where r.roleId=:roleId and m.parentId=0 order by m.menuId")
   Set<Menu> findAllFirstLevelMenuByRoleId(@Param("roleId") Long roleId);

   @Query("select m from Menu m left join fetch m.roles r where r.roleId=:roleId and m.parentId>0 order by m.menuId")
   Set<Menu> findAllSecondLevelMenuByRoleId(@Param("roleId") Long roleId);


}
