package com.service.impl;

import com.entity.User;
import com.google.common.collect.Lists;
import com.service.IUserService;
import groovy.transform.ToString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @Author:EdenJia
 * @Date：create in 17:30 2017/10/9
 * @Describe:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-activiti.xml","/applicationContext-orm.xml"})
public class UserServiceImplTest  extends AbstractJUnit4SpringContextTests {

    @Autowired
    private IUserService iUserService;

    @Test
    public void insertUser() throws Exception {
        String deptId = "20170201";
        // roleId 人事助理
        Long roleId = 7l;
        String username = "xiaomo";
        iUserService.insertUser(deptId, Lists.newArrayList(roleId),username).getData();
        System.out.println();
    }

    @Test
    public void newUser(){
        User user = new User();
        //用户名数据库我改成111111
        user.setEncodePassword("Abc12345");
        System.out.println(user.getPassword());
    }

    @Test
    public void test1(){
      User user =  (User) iUserService.loadUserByUsername("201701014");
      System.out.println(user);
    }
}
