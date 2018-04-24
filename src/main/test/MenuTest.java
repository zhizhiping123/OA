import com.dao.MenuRepository;
import com.dao.RoleRepository;
import com.entity.Leave;
import com.entity.Menu;
import com.entity.Role;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.service.impl.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ContextConfiguration(locations = {"classpath*:applicationContext-*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(value = false)
public class MenuTest {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuService menuService;
     //private Menu menu;
    @Autowired
    private RoleRepository roleRepository;

     @Test
     @Transactional
     public void initMenuData() {
         Role role = roleRepository.findByRoleId(3L);//总经理
         Role role1 = roleRepository.findByRoleId(1L);//超级管理员
         Role role2= roleRepository.findByRoleId(4L);//部门经理
         Role role3=roleRepository.findByRoleId(5L);//部门助理
         Role role4 =roleRepository.findByRoleId(6L);//普通员工
        Role role5 = roleRepository.findByRoleId(2L);//董事长
         Role role6=roleRepository.findByRoleId(7L);//保安
         Set<Role> roleSet= Sets.newHashSet();//所有角色
         roleSet.add(role);
         roleSet.add(role1);
         roleSet.add(role2);
         roleSet.add(role3);
         roleSet.add(role4);
         roleSet.add(role5);
         roleSet.add(role6);
         Set<Role> roleSet1 = Sets.newHashSet();//员工和保安
         roleSet1.add(role4);
         roleSet1.add(role6);
         Set<Role> roleSet2 = Sets.newHashSet();//助理，经理，董事长，总经理，超级管理员
         roleSet2.add(role);
         roleSet2.add(role1);
         roleSet2.add(role2);
         roleSet2.add(role3);
         roleSet2.add(role5);
        // roleSet2.add(role);
         Menu menu1 = new Menu(1, 0, "员工信息", "fa-users", null, null);
         menu1.setRoles(roleSet2);//助理，经理，董事长，总经理，超级管理员

         Menu menu2 = new Menu(2, 0, "加班管理", "fa-briefcase", null, null);
         menu2.setRoles(roleSet);//所有人

         Menu menu3 = new Menu(3, 0, "外出管理", "fa-automobile", null, null);
         menu3.setRoles(roleSet);//所有人

         Menu menu4 = new Menu(4, 0, "请假管理", "fa-plus-square", null, null);
         menu4.setRoles(roleSet);//所有人

         Menu menu5 = new Menu(5, 0, "报餐管理", "fa-shopping-basket", null, null);
         menu5.setRoles(roleSet2);//所有人

         Menu menu6 = new Menu(6, 0, "工作流部署", "fa-laptop", null, null);
         Set<Role> roleSet3=Sets.newHashSet();
         roleSet3.add(role1);
         menu6.setRoles(roleSet3);//只有超级管理员可以部署

         Menu menu7 = new Menu(7, 0, "设置", "fa-cog", null, null);
         menu7.setRoles(roleSet);//所有人

         Menu menu8 = new Menu(8, 1, "员工列表", null, "/index/user/userlist", null);
         menu8.setRoles(roleSet2);//助理，经理，董事长，总经理，超级管理员

         Menu menu9 = new Menu(9, 2, "加班申请", null, "/index/work/planwork", null);
         Set<Role> roleSet4=Sets.newHashSet();
         roleSet4.add(role3);
         menu9.setRoles(roleSet4);//只有助理

         Menu menu10 = new Menu(10, 2, "加班信息", null,  "/index/work/realwork", null);
         menu10.setRoles(roleSet);//所有人

         Menu menu11 = new Menu(11, 2, "加班审批", null, "/index/work/worklist", null);
         Set<Role> roleSet5=Sets.newHashSet();
         roleSet5.add(role2);
         menu11.setRoles(roleSet5);//部门经理

         Menu menu12 = new Menu(12, 3, "外出申请", null,  "/index/workout/workoutapply", null);
         menu12.setRoles(roleSet);//所有人

         Menu menu13 = new Menu(13, 3, "外出详情", null, "/index/workout/workoutlist", null);
         menu13.setRoles(roleSet);//所有人

         Menu menu14 = new Menu(14, 3, "外出审批", null,  "/index/workout/viewprocess", null);
         menu14.setRoles(roleSet2);//助理，经理，董事长，总经理，超级管理员
         menu14.getRoles().add(role6);
         Menu menu15 = new Menu(15, 4, "请假申请", null, "/index/leave/leaveapply", null);
         menu15.setRoles(roleSet);//所有人

         Menu menu16 = new Menu(16, 4, "请假详情", null, "/index/leave/leavelist", null);
         menu16.setRoles(roleSet2);//  menu12.setRoles(roleSet2);//助理，经理，董事长，总经理，超级管理员

         Menu menu17 = new Menu(17, 4, "请假审批", null, "/index/leave/viewprocess'", null);
         menu17.setRoles(roleSet2);//  menu12.setRoles(roleSet2);//助理，经理，董事长，总经理，超级管理员

         Menu menu18 = new Menu(18, 5, "报餐申请", null, "/index/meals/mealsapply", null);
         menu18.setRoles(roleSet4);//只有助理

         Menu menu19 = new Menu(19, 5, "报餐详情", null, "/index/meals/mealslist", null);
         menu19.setRoles(roleSet2);////助理，经理，董事长，总经理，超级管理员

         Menu menu20 = new Menu(20, 5, "报餐审批", null, "/index/meals/viewprocess", null);
         menu20.setRoles(roleSet5);//部门经理

         Menu menu21 = new Menu(21, 6, "流程模型", null, "/index/workflowset/workflowmodel", null);
         menu21.setRoles(roleSet3);//超级管理员

         Menu menu22 = new Menu(22, 6, "流程定义", null, "/index/workflowset/workflowdefined", null);
         menu22.setRoles(roleSet3);//超级管理员

         Menu menu23 = new Menu(23, 7, "个人设置", null,  "/index/setting/personset", null);
         menu23.setRoles(roleSet);//所有

         menuRepository.save(menu1);
         menuRepository.save(menu2);
         menuRepository.save(menu3);
         menuRepository.save(menu4);
         menuRepository.save(menu5);
         menuRepository.save(menu6);
         menuRepository.save(menu7);
         menuRepository.save(menu8);
         menuRepository.save(menu9);
         menuRepository.save(menu10);
         menuRepository.save(menu11);
         menuRepository.save(menu12);
         menuRepository.save(menu13);
         menuRepository.save(menu14);
         menuRepository.save(menu15);
         menuRepository.save(menu16);
         menuRepository.save(menu17);
         menuRepository.save(menu18);
         menuRepository.save(menu19);
         menuRepository.save(menu20);
         menuRepository.save(menu21);
         menuRepository.save(menu22);
         menuRepository.save(menu23);
     }

     @Test
     public void initRoleMenu(){


     }
}
