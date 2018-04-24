package com.controller;

import com.common.ServerResponse;
import com.entity.Role;
import com.service.IMenuService;
import com.service.impl.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
public class MenuController {
    @Autowired
    private IMenuService iMenuService;
    @RequestMapping("/menus")
    @ResponseBody
    public ServerResponse findRole(String userId){

        List<String> roles = iMenuService.findRoles(userId);

        return ServerResponse.buildSuccessData(roles);
    }


}
