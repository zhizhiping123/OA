package com.entity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @Author:EdenJia
 * @Date：create in 10:38 2017/9/29
 * @Describe: 角色实体
 */
@Entity
@Table(name = "oa_role")
public class Role implements Serializable, GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "role_id")
    private long  roleId;
    @Column(name = "role_name" ,length = 10)
    private String roleName;

    @Column(name = "role_zh_name")
    private String roleZhName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private Set<Menu> menus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleZhName() {
        return roleZhName;
    }

    public void setRoleZhName(String roleZhName) {
        this.roleZhName = roleZhName;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String getAuthority() {
        return roleZhName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", roleZhName='" + roleZhName + '\'' +

                '}';
    }
}
