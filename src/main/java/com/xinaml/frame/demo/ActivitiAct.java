package com.xinaml.frame.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("activiti")
public class ActivitiAct {
    @Autowired
    private ProcessEngine processEngine;

    /**
     * 查看流程列表
     *
     * @return
     */
    @ResponseBody
    @GetMapping("list")
    public String list() {
    List<Deployment> deployments = processEngine.getRepositoryService().createDeploymentQuery().list();
    for(Deployment deployment:deployments){
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }
    return "success";
    }

    /**
     * 查看流程是否结束
     *
     * @return
     */
    @ResponseBody
    @GetMapping("is/finish")
    public String isFinish() {
        ProcessInstance processInstance = processEngine.getRuntimeService()//正在执行的流程
                .createProcessInstanceQuery()//创建流程实例查询对象
                .processInstanceId("22505")
                .singleResult();
        //如果为空，流程已结束
        if (processInstance == null) {
            System.out.println("流程已结束");
        } else {
            System.out.println("流程未结束");
        }
        return "success";
    }


    /**
     * 删除流程定义
     *
     * @return
     */
    @ResponseBody
    @GetMapping("del")
    public String del() {
        processEngine.getRepositoryService().deleteDeployment("22505");
        return "success";
    }

    /**
     * 启动流程
     *
     * @return
     */
    @ResponseBody
    @GetMapping("start/task")
    public String start() {
        String processDefinitionKey = "test";
        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID:" + pi.getId());//流程实例ID    22505
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());//流程定义ID   test:1:22504
        return "success";
    }

    @ResponseBody
    @GetMapping("find/history/task")
    public String deployList() {
        //如果只想获取到已经执行完成的，那么就要加入completed这个过滤条件
        List<HistoricTaskInstance> historicTaskInstances1 = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .taskDeleteReason("completed")
                .list();
        //如果只想获取到已经执行完成的，那么就要加入completed这个过滤条件
        List<HistoricTaskInstance> historicTaskInstances2 = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .list();
        System.out.println("执行完成的任务：" + historicTaskInstances1.size());
        System.out.println("所有的总任务数（执行完和当前未执行完）：" + historicTaskInstances2.size());

        return "success";
    }

    /**
     * 查询个人任务
     *
     * @return
     */
    @ResponseBody
    @GetMapping("find/task")
    public String myTask() {
        TaskQuery query = processEngine.getTaskService().createTaskQuery();
        query.taskAssignee("张三"); // 只查询张三的任务，其他人的任务不查
        List<Task> list = query.list();
        for (Task task : list) {
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务的创建时间:" + task.getCreateTime());
            System.out.println("任务的办理人:" + task.getAssignee());
            System.out.println("流程实例ID：" + task.getProcessInstanceId());
            System.out.println("执行对象ID:" + task.getExecutionId());
            System.out.println("流程定义ID:" + task.getProcessDefinitionId());

        }
        return "success";
    }

    /**
     * 完成任务
     *
     * @return
     */
    @ResponseBody
    @GetMapping("complete/task")
    public String complete() {
        //任务ID
        String taskId = "25009";
        processEngine.getTaskService()//与正在执行的任务管理相关的Service
                .complete(taskId);
        System.out.println("完成任务：任务ID：" + taskId);
        return "success";
    }

}
