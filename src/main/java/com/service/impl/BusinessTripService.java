package com.service.impl;

import com.common.ResponseCode;
import com.common.ServerResponse;

import com.dao.BusinessTripRepository;
import com.dao.DepartmentRepository;
import com.dao.RoleRepository;
import com.dao.UserRepository;
import com.dto.BusinessTripDto;
import com.dto.PageDto;
import com.dto.UserDto;
import com.entity.BusinessTrip;
import com.entity.Department;
import com.entity.Role;
import com.entity.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.service.IBusinessTripService;
import com.service.IUserService;
import com.util.UserRoleUtil;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author:EdenJia
 * @Date：create in 11:29 2017/10/8
 * @Describe: 外出的服务类
 */
@Service
public class BusinessTripService implements IBusinessTripService {


    @Autowired
    private BusinessTripRepository businessTripRepository;

    @Autowired
    private IUserService iUserService;
    /*流程对象*/
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    //保存更新
    @Transactional
    public ServerResponse save(String userId, BusinessTrip businessTrip){
        if (StringUtils.isEmpty(userId)){
            return ServerResponse.buildErrorMsg(ResponseCode.OA_ERROR.getStatus(),"参数不能为空");
        }
        //查询相关的用户
        User user = userRepository.findByUserId(userId);
        if (user == null){
            return ServerResponse.buildErrorMsg(ResponseCode.OA_ERROR.getStatus(),"用户不存在");
        }
        if (businessTrip.getId() == 0){
            businessTrip.setBusinessId(createBusinessId());
            HashSet<User> users = Sets.newHashSet();
            users.add(user);
            businessTrip.setUsers(users);
            businessTripRepository.save(businessTrip);
            return ServerResponse.buildSuccessMsg("新增成功");
        }
        //对用户做修改
        BusinessTrip businessTrip1 = businessTripRepository.findByBusinessId(businessTrip.getBusinessId());
        Date createTime = businessTrip1.getCreateTime();
        BeanUtils.copyProperties(businessTrip,businessTrip1);
        businessTrip1.setCreateTime(createTime); //创建时间无法修改
        HashSet<User> users1 = Sets.newHashSet();
        users1.add(user);
        businessTrip1.setUsers(users1);
        businessTripRepository.save(businessTrip1);
        return ServerResponse.buildSuccessMsg("修改成功");
    }
    //删除
    @Transactional
    public void deleteByUserIdAndBusinessId(String userId,String businessId){
        BusinessTrip businessTrip =  businessTripRepository.seltectByUserIdAndBusinessId(userId,businessId);
        businessTripRepository.delete(businessTrip);
    }

    //查询全部
    public ServerResponse findAll(int pageIndex,int pageSize){
        PageRequest pageRequest = new PageRequest(pageIndex,pageSize);
        Page<BusinessTrip> page = businessTripRepository.findAll(pageRequest);
        List<BusinessTripDto> businessTripDtos = Lists.newArrayList();
        for (BusinessTrip businessTrip : page.getContent()){
            BusinessTripDto businessTripDto = entityToDto(businessTrip);
            businessTripDtos.add(businessTripDto);
        }
        PageDto pageDto = new PageDto();
        pageDto.setPageIndex(pageIndex);
        pageDto.setPageSize(pageSize);
        pageDto.setTotalPage(page.getTotalPages());
        pageDto.setObj(businessTripDtos);
        return ServerResponse.buildSuccessData(pageDto);
    }

    public ServerResponse findByCondiction(Specification<BusinessTrip> userSpecification, Pageable pageable){
        List<BusinessTripDto> businessTripDtos = Lists.newArrayList();
        if (pageable == null){
            List<BusinessTrip> list = businessTripRepository.findAll(userSpecification);
            if (list == null || list.size() == 0){
                return ServerResponse.buildErrorMsg("不存在相应的外出信息");
            }
            for (BusinessTrip businessTrip :list){
                BusinessTripDto businessTripDto = entityToDto(businessTrip);
                businessTripDtos.add(businessTripDto);
            }
            return ServerResponse.buildSuccessData(businessTripDtos);
        }

        Page<BusinessTrip> page =  this.businessTripRepository.findAll(userSpecification,pageable);
        if (page == null){
            return ServerResponse.buildErrorMsg("不存在相应的外出信息");
        }
        for (BusinessTrip businessTrip : page.getContent()){
            BusinessTripDto businessTripDto = entityToDto(businessTrip);
            businessTripDtos.add(businessTripDto);
        }

        PageDto pageDto = new PageDto();
        pageDto.setPageSize(page.getSize());
        pageDto.setPageIndex(page.getNumber());
        pageDto.setTotalPage(page.getTotalPages());
        pageDto.setObj(businessTripDtos);
        return ServerResponse.buildSuccessData(pageDto);
    }

    private BusinessTripDto entityToDto(BusinessTrip businessTrip){
        BusinessTripDto businessTripDto = new BusinessTripDto();
        BeanUtils.copyProperties(businessTrip, businessTripDto);
        Set<UserDto> userDtos = Sets.newHashSet();
        for (User u :businessTrip.getUsers()){
            UserDto userDto = new UserDto();
            userDto.setUsername(u.getUsername());
            userDto.setUserId(u.getUserId());
            List<String> roleName = Lists.newArrayList();
            for (Role role:u.getRoles()){
                roleName.add(role.getRoleName());
            }
            userDto.setRoleName(roleName);
            userDtos.add(userDto);
        }
        businessTripDto.setUsers(userDtos);
        return businessTripDto;
    }
    //根据时间建立businessId
    private synchronized String createBusinessId(){
        return String.valueOf(System.currentTimeMillis());
    }

/*-----------------------------外出流程---------------------------*/

    //部署
    public ServerResponse deploy(){
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("diagrams/goOut.bpmn20.xml").deploy();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition p:list){
            System.out.println(p.getId() +"部署对象流程Id"+p.getDeploymentId());
        }
        return ServerResponse.buildSuccessMsg("部署成功");
    }

    //填写外出条，上面应经完成

    /**
     * 启动流程
     *@param businessId,userId
     */
    public ServerResponse start(String businessId,User user){
        Map<String,Object> variables = Maps.newHashMap();
        Department department =  user.getDepartment();
        String departmentId = department.getDepartmentId();

        //用户上一级领导
        String roleType =  findHeadLeader(user,variables);
        String activitiGroroup = UserRoleUtil.buildLeaderActRoleId(departmentId,roleType,roleRepository);
        System.out.println(activitiGroroup);
        variables.put("applyUserId",user.getUserId());
        variables.put("departmentManager",activitiGroroup);  ///部门领导 zhuhai

        //人事助理GroupId
        String hrAssisant = UserRoleUtil.buildHrAssistantByDeptId(
                user.getDepartment().getDepartmentId(),departmentRepository,roleRepository);
        //人事经理
        String hrManager = UserRoleUtil.buildHrManagerByDeptId(  user.getDepartment().getDepartmentId(),departmentRepository,roleRepository);
        //总经理
        String genneral = UserRoleUtil.buildgeneralManagerByDeptId(user.getDepartment().getDepartmentId(),departmentRepository,roleRepository);
        //保安
        String doorKeeper = UserRoleUtil.buildHrManagerByDeptId(user.getDepartment().getDepartmentId(),departmentRepository,roleRepository);
        variables.put("hrAssistant",hrAssisant); //珠海人事助理
        variables.put("hrManager",hrManager);//珠海人事经理
        variables.put("doorkeeper",doorKeeper);//珠海人事保安
        variables.put("generalManager",genneral);//总经理
        variables.put("boss","000000"); //董事长


        //申明流程实例
        ProcessInstance processInstance = null;
        BusinessTrip businessTrip = businessTripRepository.findByBusinessId(businessId);
        if (businessTrip!=null){
            identityService.setAuthenticatedUserId(user.getUserId());  //用户的Id
            String businessKey = String.valueOf(businessTrip.getBusinessId());
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
            for (ProcessDefinition list1:list){
                System.out.println(list1.getId() +"部署对象ID" + list1.getKey() );
            }
            //启动流程实例

            processInstance = runtimeService.startProcessInstanceByKey("goOut",businessKey,variables);
            System.out.println("流程实例ID"+processInstance.getId());
            //流程与业务实体相关联
            businessTrip.setProcessInstanceId(processInstance.getId());
            businessTrip.setUpdateTime(new Date());
            businessTripRepository.save(businessTrip);
            return ServerResponse.buildSuccessMsg("流程成功启动");
        }
        return ServerResponse.buildErrorMsg("流程启动失败，不存在相应的业务实体");
    }

    //查询所有的任务
    public ServerResponse findTodoTask(String userId,Pageable pageable){
        List<BusinessTripDto> businessTripDtos = Lists.newArrayList();
        //根据当前人的ID查询
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(userId);
        List<Task> tasks = taskQuery.list();
        for (Task task:tasks){
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance =
                    runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null){
                continue;
            }
            BusinessTrip businessTrip = businessTripRepository.findByBusinessId(businessKey);
            if (businessTrip != null){
                BusinessTripDto businessTripDto = entityToDto(businessTrip);
                businessTripDto.setTaskId(task.getId());
                businessTripDto.setTaskName(task.getName());
                businessTripDto.setTaskCreateTime(task.getCreateTime());
                businessTripDto.setAssignee(task.getAssignee());
                businessTripDto.setTaskDefinitionKey(task.getTaskDefinitionKey());
                businessTripDto.setSuspended(processInstance.isSuspended());
                ProcessDefinition processDefinition =
                        repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
                businessTripDto.setProcessDefinitionId(processDefinition.getId());
                businessTripDto.setVersion(processDefinition.getVersion());
                businessTripDtos.add(businessTripDto);
            }
        }
        return ServerResponse.buildSuccessData(new PageImpl<BusinessTripDto>(businessTripDtos,pageable,businessTripDtos.size()));
    }
    //签收并完成任务 （任务的传递）

    /**
     *
     * @param taskId
     * @param user  登录的人
     * @param pass
     * @return
     */
    public ServerResponse compalte(String taskId,User user,Boolean pass,String opinion,String businessId,String carType){
        BusinessTrip businessTrip = businessTripRepository.findByBusinessId(businessId);
        Map<String,Object> variables = Maps.newHashMap();
        //查找这个用户属于那个组
        Group groupId =  identityService.createGroupQuery().groupMember(user.getUserId()).singleResult();
        String roleType = user.getRoles().get(0).getRoleName();
        //查找部门领导（上一级）
        String departmentLeader = UserRoleUtil.buildLeaderActRoleId(user.getDepartment().getDepartmentId(),roleType,roleRepository);
        //查找总经理
        String genneral = UserRoleUtil.buildgeneralManagerByDeptId(user.getDepartment().getDepartmentId(),departmentRepository,roleRepository);
        if (groupId.getId().equals(departmentLeader) && (!departmentLeader.equals(genneral))){ //如果是部门的领导
            variables.put("departmentPass",pass);
            businessTrip.setDepartmentOpinion(opinion);
            businessTrip.setUpdateTime(new Date());
        }
        String hrAssisant = UserRoleUtil.buildHrAssistantByDeptId(
                user.getDepartment().getDepartmentId(),departmentRepository,roleRepository);
        if (groupId.getId().equals(hrAssisant)){  //如果是人事的助理
            variables.put("hrAssistantPass",pass);
            businessTrip.setUpdateTime(new Date());
        }
        String hrManager = UserRoleUtil.buildHrManagerByDeptId(  user.getDepartment().getDepartmentId(),departmentRepository,roleRepository);
        if (groupId.getId().equals(hrManager)){  //如果是人事经理
            variables.put("hrManagerPass",pass);
            businessTrip.setHrOpinion(opinion);
            //分配车辆
            businessTrip.setCarType(carType);
            businessTrip.setUpdateTime(new Date());
        }
        if (groupId.getId().equals(genneral)){
            variables.put("generalPass",pass);
            businessTrip.setDepartmentOpinion(opinion);
            businessTrip.setUpdateTime(new Date());
        }
        if (groupId.getId().equals("000000")){
            variables.put("bossPass",pass);
            businessTrip.setDepartmentOpinion(opinion);
            businessTrip.setUpdateTime(new Date());
        }
        String doorKeeper = UserRoleUtil.buildHrManagerByDeptId(user.getDepartment().getDepartmentId(),departmentRepository,roleRepository);
        //保安只需签收完成任务
       /* if (groupId.getId().equals(doorKeeper)){  //保安
        }*/
        taskService.claim(taskId,user.getUserId()); //签收任务
        taskService.complete(taskId,variables);

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().finished().singleResult();
        if (historicProcessInstance!=null){
            businessTrip.setFlag(1);
            businessTripRepository.save(businessTrip);
            return ServerResponse.buildSuccessMsg("任务完成");
        }
        businessTripRepository.save(businessTrip);
        return ServerResponse.buildSuccessMsg("任务完成");
    }

    /*调整申请*/
    public ServerResponse changeApply(User user,String taskId,boolean idea,int flag){
        Map<String,Object> objectMap = Maps.newHashMap();
        if (flag != 1 || flag != 2) {
            return ServerResponse.buildErrorMsg("请选择重新申请或是关闭申请");
        }
        if (flag == 1){
            objectMap.put("againApply",idea);
        }
        if (flag == 2){
            objectMap.put("cancleApply",idea);
        }

        taskService.claim(taskId,user.getUserId());
        taskService.complete(taskId,objectMap);

        return ServerResponse.buildSuccessMsg("成功");
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