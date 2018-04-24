package com.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @Author:EdenJia
 * @Date：create in 18:59 2017/10/11
 * @Describe:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-activiti.xml","/applicationContext-orm.xml"})
public class IUserServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private IUserService iUserService;
    @Test
    public void findAll() throws Exception {
      //  iUserService.findAll();
    }


}
