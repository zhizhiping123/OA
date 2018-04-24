import com.common.LeaveStatus;
import com.common.ServerResponse;
import com.dao.DepartmentRepository;
import com.dao.LeaveRepository;
import com.dao.UserRepository;
import com.dto.LeaveDto;
import com.entity.Leave;
import com.entity.User;
import com.service.ILeaveService;
import com.common.LeaveType;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Set;

@ContextConfiguration(locations = {"classpath*:applicationContext-*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(value = false)
public class LeaveTest {
    @Autowired
    private ILeaveService iLeaveService;
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private RepositoryService repositoryService;
    @Test
    public void testSave(){
        for(int i=0;i<10;i++) {
            LeaveDto dto = new LeaveDto();
            dto.setDescribe("事假");
            dto.setFromTime("2017-10-15");
            dto.setToTime("2017-10-15");
            //dto.setUserId("12");
            dto.setFlag(0);
            dto.setCreateTime(new Date());
            dto.setUpdateTime(new Date());
            //dto.setDepartmentId("201701");
            dto.setTypes(LeaveType.A);
            iLeaveService.save("201701014", dto);
        }
    }
    @Test
    public void findSelfAll(){
       // iLeaveService.findSelfAll("111111", 1, 1);
        PageRequest pageRequest=new PageRequest(0,1);
        //iLeaveService.findSelfAll("111111",0,1);
       // Page<Leave> leaves = leaveRepository.findByUserId("111111", pageRequest);
        ServerResponse response=iLeaveService.findSelf("111111",0,1);
        System.out.println(response.getData());

    }

    @Test
    public void delete(){
        iLeaveService.delete("201701014","1ea235fc-3e7c-46e4-8bc1-322ec78fd15b");
        //leaveRepository.deleteByLeaveId("6582892c-b367-442c-8bad-ccf17a3b4a16");

    }

    @Test
    public void update(){
        LeaveDto dto=new LeaveDto();
        dto.setDescribe("事假A");
        dto.setFromTime("2017-10-15");
        dto.setToTime("2017-10-15");
        //dto.setUserId("12");
        dto.setCreateTime(new Date());
        dto.setUpdateTime(new Date());
        //dto.setDepartmentId("201701");
        dto.setTypes(LeaveType.A);
        dto.setLeaveId("1ea235fc-3e7c-46e4-8bc1-322ec78fd15b");
        iLeaveService.update("111111",dto);
    }
    @Test
    public void resetPwd(){
        Md5PasswordEncoder encoder=new Md5PasswordEncoder();
        String encodePassword = encoder.encodePassword("Zhiping123", "");
        System.out.println(encodePassword);
    }

    @Test
    public void findByCondiction(){

       // dto.setUserId("20170101");
        User user = userRepository.findByUserId("201701014");
        String id = user.getDepartment().getDepartmentId();
        //根据部门id查找所有的用户
        Page<User> users = userRepository.findByDeptId(id, null);
        List<User> usersContent = users.getContent();

        for (User user1:usersContent){
            LeaveDto dto=new LeaveDto();
            dto.setTypes(LeaveType.A);
            //dto.setTypes(LeaveType.B);
            dto.setFlag(0);
            dto.setUserId(user1.getUserId());
            Specification<Leave> whereClause = iLeaveService.getWhereClause(null,user);
            iLeaveService.findByCondiction(whereClause,0,20);

        }

       // ServerResponse byCondiction = iLeaveService.findByCondiction(spec, null, 0, 1);


    }
    @Test
    public void deploy(){
        iLeaveService.deploy();//部署流程定义
    }
    @Test
    public void start(){
        User user = userRepository.findByUserId("201701014");//普通员工
        iLeaveService.start("d5e36320-355b-470f-93cc-70524f2d174f",user);
    }

    @Test
    public void findTodoWork(){
      /*  User user = userRepository.findByUserId("201701012");//普通部门经理
        iLeaveService.findTotoWork("201701012",0,1);*/
        User user = userRepository.findByUserId("201701022");//人事部助理
        iLeaveService.findTotoWork("201701022",0,1);
    }

    @Test
    public void complete(){
        iLeaveService.complete("201701012","d5e36320-355b-470f-93cc-70524f2d174f","70010",false,"不同意");
        //iLeaveService.complete("201701022","2c074f44-342b-4ef8-a3f5-d121d57c5379","62505",true,"同意");//LeaveService.complete("201701021","50004",true,"同意");
        //iLeaveService.complete("201701021","2c074f44-342b-4ef8-a3f5-d121d57c5379","65004",false,"不同意");

    }
    @Test
    public void findUserByLeaveid(){
        User user = userRepository.findByLeaveId("9835d883-cfc2-49a2-b2d5-30b6b96cae6b");
        System.out.println(user.getRoles().get(0).getRoleName());
        Group group = identityService.createGroupQuery().groupMember("201701012").singleResult();
        System.out.println(group.getName());
        System.out.println(group.getId());

    }
    @Test
    public void changeApply(){
        /*LeaveDto leaveDto = new LeaveDto();
        leaveDto.setUserId("201701012");
        leaveDto.setDepartmentId("20170101");
        leaveDto.setFlag(0);
        leaveDto.setTypes(LeaveType.A);
        leaveDto.setFromTime("2017-10-18");
        leaveDto.setToTime("2017-10-18");
        leaveDto.setCreateTime(new Date());
        leaveDto.setUpdateTime(new Date());
        leaveDto.setDescribe("事假");*/
        Leave leave = leaveRepository.findByLeaveId("d5e36320-355b-470f-93cc-70524f2d174f");
        LeaveDto leaveDto = new LeaveDto();
        LeaveDto.entityToDto(leave,leaveDto);
        leaveDto.setDescribe("调整请假申请");
        leaveRepository.save(leave);
        //iLeaveService.changeApply("201701014","72509",true,leaveDto);
    }

    @Test
    public void findByUserIdAndLeaveId(){
        Leave leave = leaveRepository.findByUserIdAndLeaveId("20170101", "1508230166650");
        System.out.println(leave.getFlag());

    }
    @Test
    public  void flagToMsg(){
        int flag=1;
        for (LeaveStatus leaveStatus : LeaveStatus.values()) {
            if (leaveStatus.getStatus()==flag){
                System.out.println(leaveStatus.getMsg());

            }

        }

    }
    @Test
    public void leaveTypeToMsg(){
        String types="A";
        for (LeaveType leaveType : LeaveType.values()) {
            if (leaveType.name().equals(types)){
                System.out.println(leaveType.getType());

            }

        }

    }
    @Test
    public  void getProcessDefinitionId(){
        ProcessDefinitionQuery leaveDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave");
        List<ProcessDefinition> definition = leaveDefinition.active().list();
        //System.out.println(definition.getId());
        for (ProcessDefinition definition1:definition){
            System.out.println(definition1.getId());
            System.out.println(definition1.getName());
            System.out.println(definition1.getResourceName());
        }

    }

}
