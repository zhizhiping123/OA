package com.controller;

import com.common.LeaveStatus;
import com.common.ServerResponse;
import com.dao.UserRepository;
import com.dto.LeaveDto;
import com.dto.LeaveQueryDto;
import com.entity.Leave;
import com.entity.Role;
import com.entity.User;
import com.service.ILeavePoiService;
import com.service.ILeaveService;
import com.service.impl.LeaveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@Controller
@RequestMapping(value = "/api/leave")
public class LeaveController {
    @Autowired
    private ILeaveService iLeaveService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ILeavePoiService iLeavePoiService;
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse save(LeaveDto leaveDto){
        ServerResponse response = iLeaveService.save(getUserId(), leaveDto);
        return response;

    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateOrChangeApply(LeaveDto leaveDto){
        ServerResponse response = iLeaveService.updateOrChangeApply(getUserId(), leaveDto);
        return response;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(String leaveId){
        ServerResponse response = iLeaveService.delete(getUserId(), leaveId);
        return response;
    }

    @RequestMapping(value = "/findSelf",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse findSelfAll( @RequestParam(value = "pageIndex",defaultValue = "1") int pageIndex,
                                       @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {

        String userId=getUserId();
        ServerResponse response = iLeaveService.findSelf(userId, pageIndex-1, pageSize);
        return  response;
    }
    @RequestMapping(value = "/findByCondiction",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse findByCondiction( LeaveQueryDto leaveDto,
                                           @RequestParam(value = "pageIndex",defaultValue = "1") int pageIndex,
                                           @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = getUser();
        String deptId=user.getDepartment().getDepartmentId();
        String deptName=user.getDepartment().getDepartmentName();
        String roleName = user.getRoles().get(0).getRoleName();
       if (roleName.equals("work")||roleName.equals("doorkeeper")){
           return ServerResponse.buildErrorMsg("非法操作");
       }
       if (!deptName.contains("人事")&&!deptId.equals(leaveDto.getDeptId())){
           return ServerResponse.buildErrorMsg("非法操作");
       }
        Specification<Leave> spec = iLeaveService.getWhereClause(leaveDto,user);
        ServerResponse response = iLeaveService.findByCondiction(spec, pageIndex-1, pageSize);
        return response;
    }


    @RequestMapping(value = "/start",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse start(String leaveId){

     ServerResponse response = iLeaveService.start(leaveId, getUser());
    return response;
    }

    @RequestMapping(value = "/todo",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findTodo(@RequestParam(value = "pageIndex",defaultValue = "1") int pageIndex,
                                   @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){

        ServerResponse response = iLeaveService.findTotoWork(getUserId(), pageIndex-1,pageSize);
        return response;
    }

    /**
     *
     */
    @RequestMapping(value = "/complete",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse complete(String leaveId,String taskId,boolean pass,String opinion){

    ServerResponse response = iLeaveService.complete(getUserId(),leaveId,taskId,pass,opinion);
    return response;
    }

    /**
     * @deprecated 与update整合
     * @param leaveDto
     * @return
     */
    @RequestMapping(value = "/changeApply",method = RequestMethod.POST)
    public ServerResponse reApply(LeaveDto leaveDto){
        ServerResponse response = iLeaveService.changeApply(getUserId(),leaveDto);
        return response;
    }

    @RequestMapping(value = "/export",method = RequestMethod.GET)
    public void export(LeaveQueryDto leaveDto, HttpServletResponse response) throws IOException {
        User user=getUser();
        Specification<Leave> spec = iLeaveService.getWhereClause(leaveDto, user);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");        //改成输出excel文件
        response.setHeader("Content-disposition","attachment; filename= " + URLEncoder.encode("file.xlsx", "UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        iLeavePoiService.buildPoiByCondiction(spec,outputStream);

    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ServerResponse handleException(RuntimeException runtimeException){
        runtimeException.printStackTrace();
        return ServerResponse.buildErrorMsg(runtimeException.getMessage());
    }
    public User getUser(){

        User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
    public String getUserId(){

        return getUser().getUserId();
    }

}
