package com.service.impl;

import com.common.ServerResponse;
import com.dao.MenuRepository;
import com.dao.RoleRepository;
import com.dao.UserRepository;
import com.dto.MenuDto;
import com.entity.Menu;
import com.entity.Role;
import com.entity.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create by zzping
 */
@Service
public class MenuService implements IMenuService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenuRepository menuRepository;
    /**
     * 找到用户所对应的角色
     * @param userId
     * @return
     */
    @Override
    public List<String> findRoles(String userId) {
        //todo 从security中获取
        User user = userRepository.findByUserId(userId);
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) user.getAuthorities();
       List<String> rolesZhName = Lists.newArrayList();
        for (SimpleGrantedAuthority authority:authorities){
            rolesZhName.add(authority.getAuthority());
        }
       return rolesZhName;
    }

    /**
     * 通过用户角色列表找到用户所对应的菜单
     * @param rolesZhNames
     * @return
     */
    @Override
    public Set<MenuDto> findMenus(List<String> rolesZhNames){
        Set<Menu> firstLevelMenu=Sets.newHashSet();
        Set<Menu> secondLevelMenu=Sets.newHashSet();
        TreeSet<MenuDto> firstLevelDtoSet = Sets.newTreeSet();
        HashSet<MenuDto> secondLevelDtoSet = Sets.newHashSet();
        for (String roleZhName:rolesZhNames){
            Role role = roleRepository.findByRoleZhName(roleZhName);
            firstLevelMenu.addAll(menuRepository.findAllFirstLevelMenuByRoleId(role.getRoleId()));
            secondLevelMenu.addAll(menuRepository.findAllSecondLevelMenuByRoleId(role.getRoleId()));
            MenuDto.entitySetToDtoSet(firstLevelMenu,firstLevelDtoSet);
            MenuDto.entitySetToDtoSet(secondLevelMenu,secondLevelDtoSet);
        }

        for (MenuDto fMenu:firstLevelDtoSet){
            for (MenuDto sMenu:secondLevelDtoSet){
                if (sMenu.getParentId().intValue()==fMenu.getMenuId().intValue()){

                    fMenu.getSubMenus().add(sMenu);
                }

            }
        }

        return firstLevelDtoSet;
    }
}
