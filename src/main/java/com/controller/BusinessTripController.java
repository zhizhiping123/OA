package com.controller;

import com.common.ServerResponse;
import com.dto.BusinessTripDto;
import com.entity.BusinessTrip;
import com.entity.User;
import com.service.IBusinessTripPoiService;
import com.service.IUserService;
import com.service.IBusinessTripService;
import com.service.impl.BusinessTripPoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @Author:EdenJia
 * @Date：create in 16:13 2017/10/10
 * @Describe:外出实体 控制器
 */
@Controller
@RequestMapping("/api/businessTrip")
public class BusinessTripController {

    @Autowired
    private IBusinessTripService iBusinessTripService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private IUserService iUserService;

    /**
     *添加一个外出申请表
     * 需要用户的Id
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse save(BusinessTrip businessTrip){
        //todo 判断当前用户是否登录
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication() == null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            return ServerResponse.buildErrorMsg("请登录");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        businessTrip.setCreateTime(new Date());
        businessTrip.setUpdateTime(new Date());
        return iBusinessTripService.save(user.getUserId(),businessTrip);
    }

    /**
     *管理员查看
     * 需要判断权限
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findAll(
            @RequestParam(value = "pageIndex",defaultValue = "0")int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        //todo 判断登录
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication() == null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            return ServerResponse.buildErrorMsg("请登录");
        }
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return iBusinessTripService.findAll(pageIndex,pageSize);
    }

    /**
     * 更新外出表
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(BusinessTrip businessTrip){
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication() == null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            return ServerResponse.buildErrorMsg("请登录");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        businessTrip.setUpdateTime(new Date()); //更新时间
        return iBusinessTripService.save(user.getUserId(),businessTrip);
    }

    /**
     * 根据条件查询
     * 可以根据用户ID 部门id 用户姓名 时间
     */
    @RequestMapping(value = "/findByCondition",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse findByCondition(BusinessTripDto businessTripDto,
                                          @RequestParam(value = "pageIndex",defaultValue = "0")int pageIndex,
                                          @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        //todo 判断是否登录
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication() == null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            return ServerResponse.buildErrorMsg("请登录");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        businessTripDto.setUserId(user.getUserId());
        return iBusinessTripService.findByCondiction(businessTripDto.getWhereClause(businessTripDto),new PageRequest(0,10));
    }
    /**
     * 删除根据Businessid
     * userid
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(String businessId){
        //todo 判断是否登录
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication() == null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            return ServerResponse.buildErrorMsg("请登录");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         iBusinessTripService.deleteByUserIdAndBusinessId(user.getUserId(),businessId);
         return ServerResponse.buildSuccessMsg("删除成功");
    }

    /*------------外出请假流程----------------*/

    @RequestMapping(value = "/start",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse start(String businessId){
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication() == null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            return ServerResponse.buildErrorMsg("请登录");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       return ServerResponse.buildSuccessData(iBusinessTripService.start(businessId,user));
    }

    @RequestMapping(value = "/findToDoTask",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findToDoTask(@RequestParam(value = "pageIndex",defaultValue = "0")int pageIndex,
                                       @RequestParam(value = "pageSize",defaultValue = "5")int pageSize){
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication() == null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            return ServerResponse.buildErrorMsg("请登录");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageRequest pageRequest = new PageRequest(pageIndex,pageSize);
        return ServerResponse.buildSuccessData(iBusinessTripService.findTodoTask(user.getUserId(),pageRequest));
    }

    @RequestMapping(value = "/complete",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse complete(String taskId,String opinion,boolean pass,String businessId,String carType){
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication() == null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            return ServerResponse.buildErrorMsg("请登录");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       return ServerResponse.buildSuccessData(iBusinessTripService.compalte(taskId,user,pass,opinion,businessId,carType));
    }

    @RequestMapping(value = "/changeApply",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse changeApply(String taskId,boolean idea,int flag){
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication() == null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            return ServerResponse.buildErrorMsg("请登录");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ServerResponse.buildSuccessData(iBusinessTripService.changeApply(user,taskId,idea,flag));
    }

    @Autowired
    private IBusinessTripPoiService businessTripPoiService;
    /*生成Poi*/
   @RequestMapping(value = "/getPoi",method = RequestMethod.GET)
   public void buildPoi(BusinessTripDto businessTripDto, HttpServletResponse response) throws Exception{
       if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
               && SecurityContextHolder.getContext().getAuthentication() == null
               && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
           return ;
       }
     //  User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       response.setCharacterEncoding("utf-8");
       response.setContentType("application/vnd.ms-excel");        //改成输出excel文件
       response.setHeader("Content-disposition","attachment; filename= " + URLEncoder.encode("file.xls", "UTF-8"));
       OutputStream outputStream = response.getOutputStream();
       businessTripPoiService.buildPoiByDepartmentIdandTime(businessTripDto.getWhereClause(businessTripDto),outputStream);
   }
}
