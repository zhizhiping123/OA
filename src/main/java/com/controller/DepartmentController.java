package com.controller;

import com.common.ServerResponse;
import com.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiale on 2017/10/11.
 */

@Controller
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @RequestMapping(value = "listTopDept",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listTopLevelDepartment(){
        return departmentService.listTopLevelDepartment();
    }

    @RequestMapping(value = "insertTopDept",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse insertTopLevelDepartment(@RequestParam(required = true) String headDeptName){

       return departmentService.insertTopLevelDepartment(headDeptName);
    }

    @RequestMapping(value = "listChildByDeptId",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listChildDepartmentByDeptId(String deptId){

        return departmentService.listChildDepartmentByDeptId(deptId);
    }

    @RequestMapping(value = "isLeafByDeptId",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse isLeafByDeptId(String deptId){
        return departmentService.isLeafByDeptId(deptId);
    }

    @RequestMapping(value = "insertDept",method = RequestMethod.POST)
    @ResponseBody
    ServerResponse insertDepartment(String parentId,String departmentName){
        return departmentService.insertDepartment(parentId,departmentName);
    }

    @RequestMapping(value = "updateByDeptId",method = RequestMethod.POST)
    @ResponseBody
    ServerResponse updateByDeptId(String deptId,String deptName){
        return departmentService.updateByDeptId(deptId,deptName);
    }

    @RequestMapping(value = "updateHeadDeptByDeptId",method = RequestMethod.POST)
    @ResponseBody
    ServerResponse updateHeadDepartmentByDeptId(String deptId,String deptName){
        return departmentService.updateHeadDepartmentByDeptId(deptId,deptName);
    }

    @RequestMapping(value = "deleteAllByDeptId",method = RequestMethod.POST)
    @ResponseBody
    ServerResponse deleteAllByDeptId(String deptId){
        return departmentService.deleteAllByDeptId(deptId);
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ServerResponse handleException(RuntimeException runtimeException){
        return ServerResponse.buildErrorMsg(runtimeException.getMessage());
    }








}
