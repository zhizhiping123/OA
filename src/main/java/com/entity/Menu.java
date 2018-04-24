package com.entity;

import com.dto.MenuDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @Author:EdenJia
 * @Date：create in 15:22 2017/9/29
 * @Describe:
 */
@Entity
@Table(name = "oa_menu")
public class Menu implements Serializable {

    public Menu(long menuId, long parentId, String menuName, String faico, String url, Set<Role> roles) {
        this.menuId = menuId;
        this.parentId = parentId;
        this.menuName = menuName;
        this.faico = faico;
        this.url = url;
        this.roles = roles;
    }
    public Menu(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "menu_id")
    private long menuId;

    @Column(name="parent_id")
    private long parentId;


    @Column(name = "menu_name")
    private String menuName;

    @Column(name="faico")
    private String faico;

    @Column(name = "url")
    private String url;


    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "menu_role",joinColumns =
              @JoinColumn(name = "menu_id",referencedColumnName = "menu_id"),
              inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "role_id"))
    private Set<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getFaico() {
        return faico;
    }

    public void setFaico(String faico) {
        this.faico = faico;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        return menuId == menu.menuId;
    }

    @Override
    public int hashCode() {
        return (int) (menuId ^ (menuId >>> 32));
    }


}
