package com.service;

import com.common.ServerResponse;
import com.dto.MenuDto;
import com.entity.Menu;
import com.entity.Role;

import java.util.List;
import java.util.Set;

public interface IMenuService {
    List<String> findRoles(String userId);
    Set<MenuDto> findMenus(List<String> rolesZhNames);

}
