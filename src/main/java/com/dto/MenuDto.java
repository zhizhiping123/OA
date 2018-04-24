package com.dto;

import com.entity.Menu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * create by zzping
 */
public class MenuDto implements Comparable<MenuDto>{
    private Long menuId;
    @JsonIgnore//不参与序列化
    private Long parentId;
    @JsonProperty("menutitle")
    private String menuName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)//空和null时不参与序列化
    private String url;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String faico;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("submenus")
    private TreeSet<MenuDto> subMenus= Sets.newTreeSet();//二级菜单
    public static final void dtoToEntity(MenuDto dto, Menu entity){

        BeanUtils.copyProperties(dto,entity);

    }

    public static  final  void  entityToDto(Menu entity,MenuDto dto){

        BeanUtils.copyProperties(entity,dto);
    }

    public static final void entitySetToDtoSet(Set<Menu> entitySet, Set<MenuDto> dtoSet){
        for (Menu menuEntity:entitySet){
            MenuDto menuDto = new MenuDto();
            entityToDto(menuEntity,menuDto);
            dtoSet.add(menuDto);
        }

    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
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

    public String getFaico() {
        return faico;
    }

    public void setFaico(String faico) {
        this.faico = faico;
    }

    public TreeSet<MenuDto> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(TreeSet<MenuDto> subMenus) {
        this.subMenus = subMenus;
    }

    @Override
    public String toString() {
        return "MenuDto{" +
                "menuId=" + menuId +
                ", parentId=" + parentId +
                ", menuName='" + menuName + '\'' +
                ", url='" + url + '\'' +
                ", faico='" + faico + '\'' +
                ", subMenus=" + subMenus +
                '}';
    }

    @Override
    public int compareTo(MenuDto obj) {
        long tId=this.getMenuId();
        long oId=obj.getMenuId();
        if(tId>oId){return 1;}
        if (tId<oId){return -1;}
        return 0;
    }
}
