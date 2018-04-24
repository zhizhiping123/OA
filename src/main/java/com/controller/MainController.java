package com.controller;

import com.common.ServerResponse;
import com.dto.FindPwdDto;
import com.dto.MenuDto;
import com.entity.User;
import com.service.ILeaveService;
import com.service.ILoginService;
import com.service.IMenuService;
import com.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

/**
 * @Author:EdenJia
 * @Date：create in 22:51 2017/10/10
 * @Describe: 主要控制器（处理登录等）
 */
@Controller
public class MainController {
    @Autowired
    private ILeaveService iLeaveService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IMenuService iMenuService;
    @Autowired
    private ILoginService iLoginService;
    @RequestMapping("/api/login")
    public String index(){
        System.out.println(12341241235L);
        return "/index";
    }

    @RequestMapping("/api/index")
    @ResponseBody
    public ServerResponse loginSuccess(){
        System.out.println("Login Sucess:------------------");
        User user=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ServerResponse response = iLoginService.buildLoginSuccessData(user.getUserId());
        return response;
    }


    @RequestMapping("/api/loginError")
    @ResponseBody
    public ServerResponse loginError(){
        return ServerResponse.buildErrorMsg("登录失败");
    }

   @RequestMapping("/api/findpw")
   @ResponseBody
   public ServerResponse findPwd(FindPwdDto pwdDto){
       ServerResponse response = iLoginService.findPwd(pwdDto);

       return response;
   }


    /**
     * 需要做验证码校验，不然随便就可以更改别人的密码
     * @param username
     * @param newPassword
     * @return
     */
   @RequestMapping("/api/resetpw")
   @ResponseBody
    public ServerResponse resetPwd(String username,@RequestParam("newpassword") String newPassword){

       ServerResponse response=iLoginService.resetPwd(username,newPassword);

    return response;

   }


   @RequestMapping("/index")
   public String toIndex(){

        return "index1";
   }

}
