package com.dto;

import java.util.List;
import java.util.Set;

/**
 * create by zzping
 */
public class LoginSuccessDto {

    private String name;
    private String role;
    private Set<MenuDto> menus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<MenuDto> getMenus() {
        return menus;
    }

    public void setMenus(Set<MenuDto> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "LoginSuccessDto{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", menus=" + menus +
                '}';
    }
}
