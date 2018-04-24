package com.service;

import com.common.ServerResponse;
import com.entity.BusinessTrip;
import com.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


/**
 * @Author:EdenJia
 * @Date：create in 11:28 2017/10/8
 * @Describe:外出service接口
 */
public interface IBusinessTripService {
    ServerResponse save(String userId, BusinessTrip businessTrip);
    void deleteByUserIdAndBusinessId(String userId,String businessId);
    ServerResponse findAll(int pageIndex,int pageSize);
    ServerResponse findByCondiction(Specification<BusinessTrip> userSpecification, Pageable pageable);

    ServerResponse deploy();
    ServerResponse start(String businessId,User user);
    ServerResponse findTodoTask(String userId,Pageable pageable);
    public ServerResponse compalte(String taskId,User user,Boolean pass,String opinion,String businessId,String carType);
    ServerResponse changeApply(User user,String taskId,boolean idea,int flag);
}
