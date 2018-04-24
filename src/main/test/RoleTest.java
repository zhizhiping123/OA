import com.common.ServerResponse;
import com.dao.MenuRepository;
import com.dao.RoleRepository;
import com.dto.MenuDto;
import com.entity.Menu;
import com.entity.Role;
import com.mysql.cj.fabric.Server;
import com.service.IMenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@ContextConfiguration(locations = {"classpath*:applicationContext-*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(value = false)
public class RoleTest {
    @Autowired
    private RoleRepository repository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private IMenuService iMenuService;
    @Test
    public void findAllRole(){
      /* List<Role> roles = repository.findAllByUserId("111111");
        System.out.println(roles);*/
      /*  List<Menu> menus = menuRepository.findAllByRolesRoleZhName("总经理");
        System.out.println(menus.size());
*/
        Role role = repository.findByRoleZhName("总经理");
        System.out.println(role.getRoleId());
        List<String> roles = iMenuService.findRoles("111111");
        System.out.println(roles);
        Set<MenuDto> menus = iMenuService.findMenus(roles);
        System.out.println(menus);
        //System.out.println(menus.size());


    }
    @Test
    public void findMenu(){
        Set<Menu> allByRoleId = menuRepository.findAllFirstLevelMenuByRoleId(3L);
       // iMenuService.findMenus()
        System.out.println(allByRoleId);
    }
    @Test
    public void testEquals(){
      MenuDto menuDto=new MenuDto();
      menuDto.setMenuId(1L);
        MenuDto menuDto1=new MenuDto();
        menuDto1.setMenuId(1L);

    }


}
