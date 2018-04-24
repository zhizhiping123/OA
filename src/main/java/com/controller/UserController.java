package com.controller;

import com.common.SearchUserCondition;
import com.common.ServerResponse;
import com.entity.Department;
import com.entity.Role;
import com.entity.User;
import com.google.common.collect.Lists;
import com.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jiale on 2017/10/10.
 */

@Controller
@RequestMapping("/api/user")
@Transactional
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "insert",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse insertUser(String deptId, @RequestParam("roleIdList[]")

            List<Long> roleIdList, String username){
        return userService.insertUser(deptId,roleIdList,username);
    }

    @RequestMapping(value = "findByUserId",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findByUserId(String userId){
        return userService.findByUserId(userId);
    }

    @RequestMapping(value = "listByDeptId",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listUserByDeptId(String deptId,@RequestParam(value = "pageNo",defaultValue = "0") int pageNo
            ,@RequestParam(value = "size",defaultValue = "5") int size){
        return userService.listUserByDept(deptId,pageNo,size);
    }

    @RequestMapping(value = "listByRoleId",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listUserByRoleId(Long roleId,@RequestParam(value = "pageNo",defaultValue = "0") int pageNo
            ,@RequestParam(value = "size",defaultValue = "5") int size){
        return userService.listUserByRole(roleId,pageNo,size);
    }

    @RequestMapping(value = "quitByUserId",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse quitByUserId(String userId){
        return userService.quitJobForUserId(userId);
    }

    @RequestMapping(value = "updateByUserId",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateByUserId(User user, @RequestParam(value = "deptId",required = false) String deptId
            , @RequestParam(value = "roleIdList[]",required = false) List<Long> roleIdList){

        if(!StringUtils.isEmpty(deptId)){
            Department department  = new Department();
            department.setDepartmentId(deptId);
            user.setDepartment(department);
        }
        if(roleIdList != null){
            List<Role> roleList = Lists.newArrayList();
            for(Long roleId : roleIdList){
                Role role = new Role();
                role.setRoleId(roleId);
                roleList.add(role);
            }
            user.setRoles(roleList);
        }
        return userService.updateByUserId(user);
    }

    /**
     * 动态查询user,查询条件
     * username
     * userId
     * deptId
     * pageNo(默认0)
     * pageSize(默认5)
     * @param condition
     * @return
     */
    @RequestMapping(value = "listUserByCondition",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse listUserByCondition(SearchUserCondition condition){
        return userService.listUserByCondition(condition);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ServerResponse handleException(RuntimeException runtimeException){
        return ServerResponse.buildErrorMsg(runtimeException.getMessage());
    }

}
