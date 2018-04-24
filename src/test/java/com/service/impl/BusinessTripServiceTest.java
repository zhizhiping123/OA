package com.service.impl;

import com.common.ServerResponse;
import com.dao.UserRepository;
import com.dto.BusinessTripDto;
import com.entity.BusinessTrip;
import com.entity.User;
import com.service.IBusinessTripService;
import com.service.IUserService;
import groovy.ui.SystemOutputInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


/**
 * @Author:EdenJia
 * @Date：create in 10:31 2017/10/9
 * @Describe:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-activiti.xml","/applicationContext-orm.xml"})
public class BusinessTripServiceTest extends AbstractJUnit4SpringContextTests {

    Logger logger = LoggerFactory.getLogger(BusinessTripServiceTest.class);
    @Autowired
    private IBusinessTripService iBusinessTripService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void save() throws Exception {
        BusinessTrip businessTrip = new BusinessTrip();
        businessTrip.setCarType("c");
        businessTrip.setDepartmentId("20170101");
        businessTrip.setDescribe("外出");
        businessTrip.setFromLocation("东莞");
        businessTrip.setToLocation("北京");
        businessTrip.setFromTime("2017-10-09");
        businessTrip.setToTime("2017-10-09");
        businessTrip.setCreateTime(new Date());
        businessTrip.setProcessInstanceId("1");
        businessTrip.setUpdateTime(new Date());
        iBusinessTripService.save("201701012",businessTrip);
    }

    @Test
    public void findAll(){
       PageRequest pageRequest = new PageRequest(0,10);
       ServerResponse serverResponse = iBusinessTripService.findAll(0,10);
       logger.info(serverResponse.getData().toString());
    }

    @Test
    public void deleteById() throws Exception {
        iBusinessTripService.deleteByUserIdAndBusinessId("11","1507626001733");
    }

    @Test
    public void findByCondiction() throws Exception {
        //根据用户ID查
        BusinessTripDto businessTripDto = new BusinessTripDto();
      //  businessTripDto.setUserId("11");
       businessTripDto.setDepartmentId("20170101");
        //businessTripDto.setUsername("shujia");
       // businessTripDto.setFromTime("2017-09-01");
        //businessTripDto.setToTime("2018-01-01");
        ServerResponse page = iBusinessTripService.findByCondiction(businessTripDto.getWhereClause(businessTripDto),new PageRequest(0,10));
        System.out.println(page.getData());
    }

    @Test
    public void update(){
        BusinessTrip businessTrip = new BusinessTrip();
        businessTrip.setId(4l);
        businessTrip.setBusinessId("1507722645006");
        businessTrip.setCarType("c");
        businessTrip.setDepartmentId("20170101");
        businessTrip.setDescribe("外出");
        businessTrip.setFromLocation("东莞");
        businessTrip.setToLocation("潮州读书");
        businessTrip.setFromTime("2017-10-09");
        businessTrip.setToTime("2017-10-09");
        businessTrip.setCreateTime(new Date());
        businessTrip.setProcessInstanceId("1");
        businessTrip.setUpdateTime(new Date());
        iBusinessTripService.save("11",businessTrip);
    }

    /*------------*/
    @Test
    public void deploy(){
        iBusinessTripService.deploy();
    }

    @Test
    public void start(){
        User user = userRepository.findByUserId("201701012"); //部门经理
        iBusinessTripService.start("1507858410043",user);
    }

    @Test
    public void findTodoTask(){
        PageRequest pageRequest = new PageRequest(0,10);
        //ServerResponse serverResponse =  iBusinessTripService.findTodoTask("201701012",pageRequest);
        ServerResponse serverResponse =  iBusinessTripService.findTodoTask("201701011",pageRequest);
        System.out.println(serverResponse.toString());
    }

    @Test
    public void complete(){
        User user = userRepository.findByUserId("201701043");
        iBusinessTripService.compalte("20006",user,true,"","1507858410043","");
    }

    @Test
    public  void change(){
       User user = userRepository.findByUserId("201701012");
        ServerResponse serverResponse = iBusinessTripService.changeApply(user,"10008",true,1);
    }
}
