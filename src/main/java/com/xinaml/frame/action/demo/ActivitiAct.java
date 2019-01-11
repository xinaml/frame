package com.xinaml.frame.action.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("activiti")
public class ActivitiAct {
    @Autowired
    private ProcessEngine processEngine;


    /**
     * 启动流程实例
     *
     * @return
     */
    @GetMapping("start/process")
    public String startProcess(String key) {
         key = "leave";
        Map<String, Object> vals = new HashMap<>();
        vals.put("student", "学生3号");
        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey(key, vals);
        System.out.println("流程实例ID:" + pi.getId());
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
        return "success";
    }

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
     * 添加个人任务
     *
     * @return
     */
    @GetMapping("add/task")
    public String addAssigneeTask() {
        processEngine.getTaskService().setAssignee("2505", "员工7号");
        return "success";
    }

    /**
     * 查询个人任务
     *
     * @return
     */
    @GetMapping("find/task")
    public String myTask() {
        TaskQuery query = processEngine.getTaskService().createTaskQuery();
        query.taskAssignee("学生3号"); // 只查询张三的任务，其他人的任务不查
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
    @GetMapping("complete/task")
    public String complete() {
        //任务ID
//        String taskId = "25009";
//        processEngine.getTaskService()//与正在执行的任务管理相关的Service
//                .complete(taskId);
//        System.out.println("完成任务：任务ID：" + taskId);

        //任务ID
        String taskId = "17517";
        //完成任务的同时，设置流程变量，使用流程变量用来指定完成任务后，下一个连线，flow.bpmn文件中${message=='不重要'}
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("depter", "部门经理1");
        TaskService taskService = processEngine.getTaskService();
        taskService.complete(taskId, variables);
        System.out.println("完成任务：任务ID：" + taskId);
        return "success";
    }


    /**
     * 查看流程列表
     *
     * @return
     */
    @GetMapping("list")
    public String list() {
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()
                .list();
        if(list != null && list.size()>0){
            for(ProcessDefinition processDefinition:list){
                System.out.println("ID:"+processDefinition.getId());
                System.out.println("NAME:"+processDefinition.getName());
                System.out.println("KEY:"+processDefinition.getKey());
                System.out.println("VERSION:"+processDefinition.getVersion());
                System.out.println("resourceName:"+processDefinition.getResourceName());
                System.out.println("图片名字:"+processDefinition.getDiagramResourceName());
                System.out.println("部署的流程id:"+processDefinition.getDeploymentId());
                System.out.println("################################");
            }
        }
        return "success";
    }

    /**
     * 查看流程是否结束
     *
     * @return
     */
    @GetMapping("is/finish")
    public String isFinish() {
        ProcessInstance processInstance = processEngine.getRuntimeService()//正在执行的流程
                .createProcessInstanceQuery()//创建流程实例查询对象
                .processInstanceId("30005")
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
    @GetMapping("del")
    public String del() {
        processEngine.getRepositoryService().deleteDeployment("22505");
        return "success";
    }

}
