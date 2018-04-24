package com.service;

import com.common.ServerResponse;

/**
 * Created by TEST on 2017/10/11.
 */
public interface IRoleService {

    public ServerResponse insertRole(String roleZhName, String roleName);

}
