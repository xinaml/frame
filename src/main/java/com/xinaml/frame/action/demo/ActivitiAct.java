package com.xinaml.frame.action.demo;

import com.xinaml.frame.common.utils.DateUtil;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("activiti")
public class ActivitiAct {
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;

    @GetMapping("execute")
    public String execute() {
//        流程启动开始
        Map<String, Object> vals = new HashMap<>();
        vals.put("emp", "员工");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("leave", vals);
        String processId = pi.getId();
        System.out.println("流程创建成功，当前流程实例ID：" + processId + ",员工已申请了请假");

        // 查询员工的任务列表
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("员工").list();
        System.out.println("员工任务列表：");
        for (Task task : taskList) {
            System.out.println("任务ID：" + task.getId() + "," + "任务名称:" + task.getName() + ",流程实例ID" + task.getProcessInstanceId());
        }
        System.out.println();
        // 设置请假的受理人变量
        vals = new HashMap<>();
        vals.put("manager", "部门经理");
        // 因为员工只有一个代办的任务，直接完成任务，并赋值下一个节点的受理人为部门经理 办理
        taskService.complete(taskList.get(0).getId(), vals);
        System.out.println("任务" + taskList.get(0).getName() + "被完成了，此时流程已流转到部门经理审核！");
        System.out.println();

        // 查询部门经理的任务列表
        taskList = taskService.createTaskQuery().taskAssignee("部门经理").list();
        System.out.println("部门经理任务列表：");
        for (Task task : taskList) {
            System.out.println("任务ID：" + task.getId() + "," + "任务名称:" + task.getName() + ",流程实例ID" + task.getProcessInstanceId());
        }
        System.out.println();

        vals = new HashMap<>();
        vals.put("boss", "老板");
        // 因为部门经理只有一个代办的任务，直接完成任务，并赋值下一个节点的受理人为老板 办理
        processEngine.getTaskService().complete(taskList.get(0).getId(), vals);
        System.out.println("任务" + taskList.get(0).getName() + "被完成了，此时流程已流转到老板审核！");
        System.out.println();

        taskList = taskService.createTaskQuery().taskAssignee("老板").list();
        System.out.println("老板任务列表：");
        for (Task task : taskList) {
            System.out.println("任务ID：" + task.getId() + "," + "任务名称:" + task.getName() + ",流程实例ID" + task.getProcessInstanceId());
        }
        System.out.println();

        // 因为老板只有一个代办的任务，直接完成任务
        taskService.complete(taskList.get(0).getId());
        System.out.println("任务" + taskList.get(0).getName() + "被完成了！");
        System.out.println("流程未结束");


        return "success";
    }


    /**
     * 启动实例
     *
     * @return
     */
    @GetMapping("start/process")
    public String startProcess(String key) {
        key = "leave";//流程名
        Map<String, Object> vals = new HashMap<>();
        vals.put("emp", "aml");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(key, vals);
        String processId = pi.getId();
        System.out.println("流程创建成功，当前流程实例ID：" + processId);
        return "success";
    }

    @GetMapping("find/history/task")
    public String deployList() {
        //如果只想获取到已经执行完成的，那么就要加入completed这个过滤条件
        System.out.println("员工执行完成的任务：");
        List<HistoricTaskInstance> hisTasks = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee("员工").finished()
                .list();
        for (HistoricTaskInstance his : hisTasks) {
            System.out.println(his.getName() + "--" + his.getEndTime());
        }
        System.out.println();
        System.out.println("员工执行未完成的任务：");
        hisTasks = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee("员工").unfinished()
                .list();
        for (HistoricTaskInstance his : hisTasks) {
            System.out.println(his.getName() + "--" + his.getEndTime());
        }
        System.out.println();
        long count  = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee("员工").processFinished()
                .count();
        System.out.println("员工完成流程数："+count);

        return "success";
    }

    /**
     * 添加个人任务
     *
     * @return
     */
    @GetMapping("add/task")
    public String addAssigneeTask() {
        taskService.setAssignee("22520", "aml");
        return "success";
    }

    /**
     * 查询个人任务
     *
     * @return
     */
    @GetMapping("find/task")
    public String myTask() {
        TaskQuery query = taskService.createTaskQuery();
        query.taskAssignee("aml"); // 只查询张三的任务，其他人的任务不查
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
        String taskId = "17517";
        //完成任务的同时，设置流程变量，使用流程变量用来指定完成任务后，下一个连线，flow.bpmn文件中${message=='不重要'}
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("depter", "部门经理1");
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
        if (list != null && list.size() > 0) {
            for (ProcessDefinition processDefinition : list) {
                System.out.println("ID:" + processDefinition.getId());
                System.out.println("NAME:" + processDefinition.getName());
                System.out.println("KEY:" + processDefinition.getKey());
                System.out.println("VERSION:" + processDefinition.getVersion());
                System.out.println("resourceName:" + processDefinition.getResourceName());
                System.out.println("图片名字:" + processDefinition.getDiagramResourceName());
                System.out.println("部署的流程id:" + processDefinition.getDeploymentId());
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
