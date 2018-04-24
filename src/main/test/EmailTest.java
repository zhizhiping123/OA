import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@ContextConfiguration(locations = {"classpath*:applicationContext-*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(value = false)
public class EmailTest {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Test
    public void deploy(){
        repositoryService.createDeployment().addClasspathResource("diagrams/email.bpmn20.xml").deploy();
       // ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionName("email").singleResult();
       // System.out.println(definition.getDeploymentId());
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        System.out.println(list.size());
        for (ProcessDefinition definition:list){
            System.out.println(definition.getName());
        }

    }
    @Test
    public void sendEmail(){
        Map<String,Object> variables= Maps.newHashMap();
        variables.put("from","337095899@qq.com");
        variables.put("to","1092827230@qq.com");
        variables.put("applyUserId","201701014");
        //variables.put("")
        runtimeService.startProcessInstanceByKey("email","12345",variables);
        //taskService.c



    }
    @Test
    public void findTodo(){
        Map<String,Object> variables= Maps.newHashMap();
        variables.put("from","337095899@qq.com");
        variables.put("to","1092827230@qq.com");
       // variables.put("pass",false);
        taskService.complete("157505",variables);

    }
    ;

}
