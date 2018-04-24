package com.service.impl;

import com.common.ServerResponse;
import com.dao.RoleRepository;
import com.entity.Role;
import com.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jiale on 2017/10/11.
 */
@Service("IRoleService")
public class RoleService implements IRoleService{

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ServerResponse insertRole(String roleZhName,String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setRoleZhName(roleZhName);
        role.setRoleId(roleRepository.count() + 1);
        roleRepository.save(role);
        return ServerResponse.buildSuccessMsg("添加角色成功");
    }
}
