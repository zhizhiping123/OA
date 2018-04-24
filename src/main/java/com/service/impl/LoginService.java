package com.service.impl;

import com.common.ServerResponse;
import com.dao.UserRepository;
import com.dto.FindPwdDto;
import com.dto.LoginSuccessDto;
import com.dto.MenuDto;
import com.entity.User;
import com.google.common.base.Objects;
import com.service.ILeaveService;
import com.service.ILoginService;
import com.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LoginService implements ILoginService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IMenuService iMenuService;

    /**
     * 匹配用户输入的答案
     * @param pwdDto
     * @return
     */
    @Override
    public ServerResponse findPwd(FindPwdDto pwdDto) {
        String username=pwdDto.getUsername();
        if(username==""||username==null){
            return ServerResponse.buildErrorMsg("用户名输入有误");
        }
        User user = userRepository.findUserByUsername(username);
        if (user==null){
            return ServerResponse.buildErrorMsg("没有此用户");
        }
        String answer1 = user.getAnswer1();
        String answer2 = user.getAnswer2();
        if (Objects.equal(answer1,pwdDto.getAnswer1())&&Objects.equal(answer2,pwdDto.getAnswer2())){
            return ServerResponse.buildSuccessMsg("");
        }
        return ServerResponse.buildErrorMsg("匹配失败");

    }

    /**
     *
     * 重置用户密码,到时候弄一个到验证码的
     * @param username
     * @param newPassword
     * @return
     */
    //todo
    @Override
    public ServerResponse resetPwd(String username, String newPassword) {

        User user = userRepository.findUserByUsername(username);
        if (user==null) {
            return ServerResponse.buildErrorMsg("用户不存在");
        }
        user.setPassword(newPassword);
        Md5PasswordEncoder encoder= new Md5PasswordEncoder();
        String encodePassword = encoder.encodePassword(newPassword, "");
        user.setPassword(encodePassword);
        userRepository.save(user);
        return ServerResponse.buildSuccessMsg("修改成功");
    }

    /**
     * 构建用户成功登陆后返回的数据
     * @param userId
     * @return
     */
    @Override
    public ServerResponse buildLoginSuccessData(String userId){

        LoginSuccessDto loginSuccessDto=new LoginSuccessDto();
        User user = userRepository.findByUserId(userId);
        List<String> roles = iMenuService.findRoles(userId);
        Set<MenuDto> menus = iMenuService.findMenus(roles);
        loginSuccessDto.setName(user.getUsername());
        loginSuccessDto.setRole(roles.get(0));//只显示第一个角色
        loginSuccessDto.setMenus(menus);
        return ServerResponse.buildSuccessData(loginSuccessDto);
    }
}
