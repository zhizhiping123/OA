package login;

import com.dao.MenuRepository;
import com.entity.Menu;
import com.service.ILoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@ContextConfiguration(locations = {"classpath*:applicationContext-*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(value = false)
public class LoginTest {
    @Autowired
    private ILoginService iLoginService;
    @Autowired
    private MenuRepository menuRepository;
    @Test
    public void loginSuccesstest(){

       iLoginService.buildLoginSuccessData("111111");

    }
    @Test
    public void findsecond(){

        Set<Menu> menus = menuRepository.findAllSecondLevelMenuByRoleId(3L);
        System.out.println(menus);
    }
    @Test
    public void testMd5(){
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String abc123 = encoder.encodePassword("Abc12345", "");
        System.out.println(abc123);

    }
}
