package com.service;

import java.util.List;

import com.common.SearchUserCondition;
import com.common.ServerResponse;
import com.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * Created by jiale on 2017/10/9.
 */

public interface IUserService extends UserDetailsService{

    /**
     * 插入一个用户
     * @return
     */
    ServerResponse insertUser(String deptId, List<Long> roleIdList, String username);

    /**
     * 根据 userId 查询用户信息
     * @param userId
     * @return
     */
    ServerResponse findByUserId(String userId);


    /**
     * 根据部门查询用户
     * @param deptId
     * @return
     */
    ServerResponse listUserByDept(String deptId, int page, int size);

    /**
     * 根据角色查询用户
     * @param roleId
     * @return
     */
    ServerResponse listUserByRole(Long roleId, int page, int size);

    /**
     * 员工离职,修改状态
     * @param userId
     * @return
     */
    ServerResponse quitJobForUserId(String userId);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    ServerResponse updateByUserId(User user);


    ServerResponse listUserByCondition(SearchUserCondition condition);

}
