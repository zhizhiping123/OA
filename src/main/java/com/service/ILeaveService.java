package com.service;

import com.common.ServerResponse;
import com.dto.LeaveDto;
import com.dto.LeaveQueryDto;
import com.entity.Leave;
import com.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface ILeaveService {

    ServerResponse save(String userId, LeaveDto leaveDto);
    ServerResponse findSelf(String userId, int pageIndex, int pageSize);
    ServerResponse delete(String userId,String leaveId);
    ServerResponse update(String userId,LeaveDto leaveDto);
    ServerResponse findByCondiction(Specification<Leave> spec, int indexPage,int pageSize);
    ServerResponse deploy();
    ServerResponse start(String businessId, User user);
    ServerResponse findTotoWork(String userId,int indexPage,int pageSize);
     ServerResponse complete(String userId,String businessId,String taskId,Boolean pass,String opinion);
    Specification<Leave> getWhereClause(LeaveQueryDto leaveDto, User user);
    ServerResponse changeApply(String userId, LeaveDto leaveDto);
    int findFlagByUserIdAndLeaveId(String userId,String leaveId);
    ServerResponse updateOrChangeApply(String userId,LeaveDto leaveDto);
}
