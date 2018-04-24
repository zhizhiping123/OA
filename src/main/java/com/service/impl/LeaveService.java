package com.service.impl;

import com.common.LeaveStatus;
import com.common.ServerResponse;
import com.dao.DepartmentRepository;
import com.dao.LeaveRepository;
import com.dao.RoleRepository;
import com.dao.UserRepository;
import com.dto.LeaveDto;
import com.dto.LeaveQueryDto;
import com.dto.PageDto;
import com.entity.Department;
import com.entity.Leave;
import com.entity.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.service.ILeaveService;
import com.util.UserRoleUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author zzping
 *
 */
@Service
public class LeaveService implements ILeaveService {
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;

    /**
     * 保存请假实例，此时未进入工作流
     * @param userId
     * @param leaveDto
     * @return
     */
    @Override
    @Transactional
    public ServerResponse save(String userId, LeaveDto leaveDto) {

        Leave leave = new Leave();
        LeaveDto.dtoToEntity(leaveDto,leave);
        String leaveId = String.valueOf(System.currentTimeMillis());//使用当前时间戳作为id
        leave.setLeaveId(leaveId);
        User user = userRepository.findByUserId(userId);
        Set<User> users = Sets.newHashSet();
        users.add(user);
        leave.setUsers(users);
        /*
        * 防止前端从dto中传入不存在的数据
        * */
        leave.setFlag(LeaveStatus.NEW.getStatus());//保存状态,未提交申请
        leave.setHrOpinion(null);
        leave.setDepartmentOpinion(null);
        leave.setProcessInstanceId(null);
        leave.setCreateTime(new Date());
        leave.setUpdateTime(new Date());
        leave.setDepartmentId(user.getDepartment().getDepartmentId());
        leaveRepository.save(leave);
        LeaveDto.entityToDto(leave,leaveDto);
        return ServerResponse.buildSuccessData(leaveDto);
    }

    /**
     * 找到本人的所有请假记录
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse findSelf(String userId, int pageIndex, int pageSize) {

        PageRequest pageRequest =new PageRequest(pageIndex,pageSize);
        User user = userRepository.findByUserId(userId);
        String userName=user.getUsername();
        String deptName=user.getDepartment().getDepartmentName();
        String deptId=user.getDepartment().getDepartmentId();
        Page<Leave> leavePage = leaveRepository.findByUserId(userId, pageRequest);
        if (leavePage==null){
            return ServerResponse.buildErrorMsg("查找失败");
        }
        PageDto pageDto =new PageDto();
        pageDto.setTotalPage(leavePage.getTotalPages());
        pageDto.setPageIndex(pageIndex+1);//
        pageDto.setPageSize(pageSize);
        ArrayList<LeaveDto> leaveDtos = Lists.newArrayList();
        for (Leave leave:leavePage.getContent()){
            LeaveDto leaveDto=new LeaveDto();
            if (leave.getProcessInstanceId()!=null){
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
                if (processInstance!=null) {
                    String processDefinitionId = processInstance.getProcessDefinitionId();
                    leaveDto.setProcessDefinitionId(processDefinitionId);
                }
            }
            LeaveDto.entityToDto(leave,leaveDto);
            leaveDto.setUserId(userId);
            leaveDto.setUsername(userName);
            leaveDto.setDeptName(deptName);
            leaveDto.setDeptId(deptId);
            leaveDtos.add(leaveDto);
        }
        pageDto.setObj(leaveDtos);
        return ServerResponse.buildSuccessData(pageDto);
    }

    /**
     * 删除未进入工作流的请假条，已进入不可删除
     * @param leaveId
     * @return
     */
    @Override
    @Transactional
    public ServerResponse delete(String userId,String leaveId) {

        if (leaveId==null){
            return ServerResponse.buildErrorMsg("删除失败");
        }
        Leave leave = leaveRepository.findByUserIdAndLeaveId(userId,leaveId);
        //有流程id表示已经入流程
        if(leave.getProcessInstanceId()!=null||leave.getFlag()!=LeaveStatus.NEW.getStatus()){
            return ServerResponse.buildErrorMsg("不可删除");
        }

        leaveRepository.deleteByLeaveId(leaveId);
        return ServerResponse.buildSuccessMsg("删除成功");
    }

    /**
     * 更新未进入工作流的请假条，进入流程的不可更改
     *
     * @param leaveDto
     * @return
     */
    @Override
    @Transactional
    public ServerResponse update(String userId,LeaveDto leaveDto){

        Leave leave = leaveRepository.findByUserIdAndLeaveId(userId,leaveDto.getLeaveId());
        if (leave==null){
            return ServerResponse.buildErrorMsg("非法操作");
        }
        if(leave.getProcessInstanceId()!=null||leave.getFlag()!=LeaveStatus.NEW.getStatus()){
            ServerResponse.buildErrorMsg("不可更改已提交申请");
        }

            LeaveDto.dtoToEntity(leaveDto,leave);
            //初始化数据，防止从dto中传入其他字段
            leave.setUpdateTime(new Date());
            leave.setFlag(LeaveStatus.CHECK.getStatus());
            leave.setDepartmentOpinion(null);
            leave.setHrOpinion(null);
            leaveRepository.save(leave);
            leaveRepository.flush();
            //从数据库中返回
            leave=leaveRepository.findByLeaveId(leaveDto.getLeaveId());
            LeaveDto.entityToDto(leave,leaveDto);
            User user = userRepository.findByUserId(userId);
            leaveDto.setUsername(user.getUsername());
            leaveDto.setDeptName(user.getDepartment().getDepartmentName());
        return ServerResponse.buildSuccessData(leaveDto);

    }

    @Override
    public ServerResponse findByCondiction(Specification<Leave> spec,  int pageIndex,int pageSize) {

        PageRequest pageRequest=new PageRequest(pageIndex,pageSize);
        Page<Leave> leavePage = leaveRepository.findAll(spec, pageRequest);
        List<Leave> leaveList = leavePage.getContent();
        List<LeaveDto> leaveDtoList=Lists.newArrayList();
        LeaveDto.entityListToDtoList(leaveList,leaveDtoList);
        for (LeaveDto leaveDto:leaveDtoList){
            //返回用户名和部门,有可能查找部门的，所以要遍历userId
            User user=userRepository.findByLeaveId(leaveDto.getLeaveId());
            if (user==null){
                continue;
            }
            Leave leave = leaveRepository.findByLeaveId(leaveDto.getLeaveId());
            if (leave.getProcessInstanceId()!=null){
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
                if (processInstance!=null) {
                    String processDefinitionId = processInstance.getProcessDefinitionId();
                    leaveDto.setProcessDefinitionId(processDefinitionId);
                }
            }
            leaveDto.setDeptId(user.getDepartment().getDepartmentId());
            leaveDto.setDeptName(user.getDepartment().getDepartmentName());
            leaveDto.setUsername(user.getUsername());
            leaveDto.setUserId(user.getUserId());
        }
        PageDto pageDto=new PageDto();
        pageDto.setObj(leaveDtoList);
        pageDto.setPageSize(pageSize);
        pageDto.setPageIndex(pageIndex+1);
        pageDto.setTotalPage(leavePage.getTotalPages());
        return ServerResponse.buildSuccessData(pageDto);
    }


    /**
     * 重新提交申请
     *
     * @return
     */
    @Override
    @Transactional
    public ServerResponse changeApply(String userId, LeaveDto leaveDto){

        String leaveId = leaveDto.getLeaveId();
        Leave leave = leaveRepository.findByUserIdAndLeaveId(userId, leaveId);
        if (leave==null){
            return ServerResponse.buildErrorMsg("非法操作");
        }

        Integer flag = leave.getFlag();
        if(flag!=LeaveStatus.UNPASS.getStatus()){
            return ServerResponse.buildErrorMsg("当前状态不可重新提交");
        }
        LeaveDto.dtoToEntity(leaveDto,leave);
        leave.setFlag(LeaveStatus.NEW.getStatus());//修改状态
        leave.setDepartmentOpinion(null);
        leave.setHrOpinion(null);
        leave.setUpdateTime(new Date());
        leaveRepository.save(leave);
        leaveRepository.flush();//立即刷新到数据库
        User user = userRepository.findByUserId(userId);
        ServerResponse response = start(leaveId, user);
        return response;
    }

    @Override
    public ServerResponse updateOrChangeApply(String userId, LeaveDto leaveDto) {
        ServerResponse response=null;
        int flag = findFlagByUserIdAndLeaveId(userId, leaveDto.getLeaveId());
        if (flag== LeaveStatus.NEW.getStatus()){
            response = update(userId, leaveDto);
        }
        else if (flag==LeaveStatus.UNPASS.getStatus()){
            response=changeApply(userId,leaveDto);
        }
        else {
            return ServerResponse.buildErrorMsg("当前状态不可编辑");
        }
        return response;
    }


    @Override
    public ServerResponse deploy(){
        repositoryService.createDeployment().addClasspathResource("diagrams/leave.bpmn20.xml").deploy();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition definition:list){
            System.out.println(definition.getName());
            System.out.println(definition.getDeploymentId());
        }
        return ServerResponse.buildSuccessMsg("部署成功");
    }
    /**
     * 启动请假流程
     * @param
     * @return
     */
    @Override
    public ServerResponse start(String businessId, User user) {
        ProcessInstance processInstance = null;
        Map<String,Object> variables= Maps.newHashMap();
        //找到领导和设置员工的类型
        Department department = user.getDepartment();
        String departmentId = department.getDepartmentId();
        String headLeaderRole = findHeadLeader(user,variables);
        String leaderActRole = UserRoleUtil.buildLeaderActRoleId(departmentId, headLeaderRole, roleRepository);
        String hrAssistActRole = UserRoleUtil.buildHrAssistantByDeptId(departmentId,departmentRepository,roleRepository);
        String hrActRole = UserRoleUtil.buildHrManagerByDeptId(departmentId,departmentRepository,roleRepository);
        //设置流程中的用户变量
        variables.put("hrAssist",hrAssistActRole);
        variables.put("hr",hrActRole);
        variables.put("applyUserId",user.getUserId());
        switch ((Integer) variables.get("type")){
            case 1:variables.put("deptManager",leaderActRole);break;
            case 2:variables.put("generalManager",leaderActRole);break;
            case 3:variables.put("boss",leaderActRole);break;
            default:return ServerResponse.buildErrorMsg("系统错误");

        }
        try {

            identityService.setAuthenticatedUserId(user.getUserId());
            Leave leave = leaveRepository.findByLeaveId(businessId);
            if (leave.getFlag()!=LeaveStatus.NEW.getStatus()||leave.getProcessInstanceId()!=null){
                return ServerResponse.buildErrorMsg("不可以申请");
            }
            processInstance = runtimeService.startProcessInstanceByKey("leave", businessId, variables);
            leave.setProcessInstanceId(processInstance.getId());//设置流程实例id
            leave.setFlag(LeaveStatus.CHECK.getStatus());//把状态修改成审核中
            leave.setUpdateTime(new Date());//修改更新时间
            leaveRepository.save(leave);
            LeaveDto leaveDto=new LeaveDto();
            LeaveDto.entityToDto(leave,leaveDto);
            leaveDto.setDeptName(department.getDepartmentName());
            leaveDto.setUsername(user.getUsername());
            return ServerResponse.buildSuccessData(leaveDto);
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.buildErrorMsg("申请失败");

        }finally {
            identityService.setAuthenticatedUserId(null);
        }

    }


    /**
     * 找到用户相对应的请假任务
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse findTotoWork(String userId,int pageIndex,int pageSize){

        ProcessInstance processInstance =null;
        PageRequest pageable=new PageRequest(pageIndex,pageSize);
        List<LeaveDto> leaveDtoList=Lists.newArrayList();
        String groupId = identityService.createGroupQuery().groupMember(userId).singleResult().getId();
        //找到所有要做的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(groupId).list();
        for (Task task:tasks){
            String taskId = task.getId();
            String processInstanceId = task.getProcessInstanceId();//流程id
            //根据流程id获取流程实例
            processInstance = runtimeService.createProcessInstanceQuery().active().processInstanceId(processInstanceId).singleResult();
            //获取业务关键字，即leaveId,与业务实例结合
            String businessKey = processInstance.getBusinessKey();
            Leave leave = leaveRepository.findByLeaveId(businessKey);
            if (leave == null){
                continue;//排除不是请假流程的businessKey
            }
            //根据businessKey,找到请假的用户
            User user = userRepository.findByLeaveId(businessKey);
            String deptName=user.getDepartment().getDepartmentName();
            LeaveDto leaveDto=new LeaveDto();
            LeaveDto.entityToDto(leave,leaveDto);
            leaveDto.setUsername(user.getUsername());
            leaveDto.setDeptName(deptName);
            leaveDto.setTaskId(taskId);
            leaveDto.setTaskName(task.getName());
            leaveDtoList.add(leaveDto);
        }

        return ServerResponse.buildSuccessData(new PageImpl<LeaveDto>(leaveDtoList,pageable,leaveDtoList.size()));
    }

    /**
     * 签收并完成任务
     * @param userId
     * @param taskId
     * @return
     */
    @Override
    public ServerResponse complete(String userId,String businessId,String taskId,Boolean pass,String opinion){

        Map<String,Object> variables=Maps.newHashMap();
        variables.put("pass",pass);
        taskService.claim(taskId,userId);//签收任务
        User applyUser = userRepository.findByLeaveId(businessId);//找到申请的用户
        Leave leave = leaveRepository.findByLeaveId(businessId);//找到请假实体
        Group group = identityService.createGroupQuery().groupMember(userId).singleResult();//获取签收者所在的用户组
        String deptId=applyUser.getDepartment().getDepartmentId();
        String hrActRole=UserRoleUtil.buildHrManagerByDeptId(deptId,departmentRepository,roleRepository);
        String hrAssistActRole=UserRoleUtil.buildHrAssistantByDeptId(deptId,departmentRepository,roleRepository);
        if (!pass){
            leave.setFlag(LeaveStatus.UNPASS.getStatus());//不通过申请
        }
        taskService.complete(taskId,variables);
        if (group.getId().equals(hrActRole)){//如果是人事经理审批
            leave.setHrOpinion(opinion);//todo 人事部门员工没有部门领导意见，只有人事经理意见
            int flag=(pass==true?1:0);
            leave.setFlag(flag);
        }
        else if(group.getId().equals(hrAssistActRole)){

        }
        else {
            leave.setDepartmentOpinion(opinion);
        }
        leave.setUpdateTime(new Date());
        leaveRepository.save(leave);
        LeaveDto leaveDto = new LeaveDto();
        LeaveDto.entityToDto(leave,leaveDto);
        return ServerResponse.buildSuccessData(leaveDto);
    }

    //动态条件
    public  Specification<Leave> getWhereClause(final LeaveQueryDto leaveDto, User user){
        return new Specification<Leave>() {
            @Override
            public Predicate toPredicate(Root<Leave> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate=criteriaBuilder.conjunction();
                if (StringUtils.isNotEmpty(leaveDto.getDeptId())){
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("departmentId"),leaveDto.getDeptId()));
                }
                if (StringUtils.isNotEmpty(leaveDto.getUserId())){
                    Join<Leave,User> join = root.join("users",JoinType.LEFT);
                    predicate.getExpressions().add(criteriaBuilder.equal(join.get("userId"),leaveDto.getUserId()));
                }
                if (StringUtils.isNotEmpty(leaveDto.getUsername())){
                    Join<Leave,User> join = root.join("users",JoinType.LEFT);
                    predicate.getExpressions().add(criteriaBuilder.like(join.get("username"),"%"+leaveDto.getUsername()+"%"));
                }
                if (StringUtils.isNotEmpty(leaveDto.getFromTime())&&StringUtils.isNotEmpty(leaveDto.getToTime())){
                    predicate.getExpressions().add(criteriaBuilder.between(root.get("fromTime"),leaveDto.getFromTime(),leaveDto.getToTime()));
                }
                //不查询新增的请假申请
                predicate.getExpressions().add(criteriaBuilder.notEqual(root.<Integer>get("flag"),LeaveStatus.NEW.getStatus()));
                return predicate;
            }
           /* @Override
            public Predicate toPredicate(Root<Leave> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                String roleName=user.getRoles().get(0).getRoleName();
                System.out.println("角色名称"+roleName);
                //如果是部门经理或者助理查而且查的不是个人,添加部门Id
                System.out.println("是否个人申请："+leaveDto.isPersonal());
                if((roleName.equals("manager")||roleName.equals("assistant"))&&leaveDto.isPersonal()!=null&&!leaveDto.isPersonal()){
                    System.out.println("查找本部门");
                    //不可以查看别人新增的请假
                    if (leaveDto.getFlag()!=null&&leaveDto.getFlag()==LeaveStatus.NEW.getStatus()){
                        return null;
                    }
                    else if(leaveDto.getFlag()==null){
                        System.out.println("查找部门");
                        //当flag为空时，排除新增的请假记录
                       predicate.getExpressions().add(criteriaBuilder.notEqual(root.<Integer>get("flag"),LeaveStatus.NEW.getStatus()));
                    }
                    String deptId=user.getDepartment().getDepartmentId();
                    System.out.println("部门Id"+deptId);
                    predicate.getExpressions().add(criteriaBuilder.equal(root.<String>get("departmentId"),deptId));
                }
                else{
                    //如果查的是个人
                    Join<Leave,User> join = root.join("users",JoinType.LEFT);
                    predicate.getExpressions().add(criteriaBuilder.equal(join.get("userId"),user.getUserId()));
                }

                if (leaveDto.getFlag()!=null){
                    System.out.println(leaveDto.getFlag());
                    predicate.getExpressions().add(criteriaBuilder.equal(root.<Integer>get("flag"),leaveDto.getFlag()));
                }

                if(leaveDto.getTypes()!=null){
                    System.out.println(leaveDto.getTypes());
                    predicate.getExpressions().add(criteriaBuilder.equal(root.<String>get("types"),leaveDto.getTypes().name()));
                }

                if(leaveDto.getFromTime()!=null&&leaveDto.getToTime()!=null){
                    predicate.getExpressions().add(criteriaBuilder.between(root.<String>get("fromTime"),leaveDto.getFromTime(),leaveDto.getToTime()));

                }

                else if (leaveDto.getFromTime()!=null&&leaveDto.getToTime()==null){
                    predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.<String>get("fromTime"),leaveDto.getFromTime()));

                }
                else if(leaveDto.getFromTime()==null&&leaveDto.getToTime()!=null){
                    predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.<String>get("toTime"),leaveDto.getToTime()));

                }

                return predicate;
            }*/
        };
    }

    @Override
    public int findFlagByUserIdAndLeaveId(String userId, String leaveId) {
        Leave leave = leaveRepository.findByUserIdAndLeaveId(userId, leaveId);
        int flag=leave.getFlag();
        return flag;
    }

    private String getProcessDefinitionId(){
        ProcessDefinitionQuery leaveDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave");
        ProcessDefinition definition = leaveDefinition.active().singleResult();
        System.out.println(definition.getId());
        return definition.getId();
    }


    private String findHeadLeader(User user, Map<String,Object> variables){
        String  roleName = user.getRoles().get(0).getRoleName();
        if (roleName.equals("doorkeeper") || roleName.equals("worker") || roleName.equals("assistant")){
            variables.put("type",1);
            return "manager";
        }
        if (roleName.equals("manager")){
            variables.put("type",2);
            return "generalManager";
        }
        if (roleName.equals("generalManager")){
            variables.put("type",3);
            return "boss";
        }
        return null;
    }
}
