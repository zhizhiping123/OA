package com.util;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.beans.ExceptionListener;

/**
 * @Author:EdenJia
 * @Date：create in 9:39 2017/10/11
 * @Describe:流程邮件工具类
 */
public class EmailInfoUtil implements ExecutionListener{

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        IdentityService identityService =
                 execution.getEngineServices().getIdentityService();
        execution.setVariableLocal("to","1092827230@qq.com");
        execution.setVariableLocal("from","337095899@qq.com");
        execution.setVariableLocal("name","zzping");
    }
}
