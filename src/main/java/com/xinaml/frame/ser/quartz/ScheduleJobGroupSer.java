package com.xinaml.frame.ser.quartz;

import com.xinaml.frame.base.ser.Ser;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.dto.quartz.ScheduleJobGroupDTO;
import com.xinaml.frame.entity.quartz.ScheduleJobGroup;
import com.xinaml.frame.to.quartz.ScheduleJobGroupTO;

/**
 * @Author: [liguiqin]
 * @Date:[2018-12-29 10:10]
 * @Description: [ 定时器组]
 * @Version: [3.0]
 * @Copy: [com.xinaml]
 */
public interface ScheduleJobGroupSer extends Ser<ScheduleJobGroup, ScheduleJobGroupDTO> {

    /**
     * 添加任务调度组
     *
     * @param jobGroup
     * @return
     */
    default ScheduleJobGroup add(ScheduleJobGroupTO jobGroup) throws SerException {
        return null;
    }

    /**
     * 编辑任务调度组
     *
     * @param jobGroupTO
     */
    default void edit(ScheduleJobGroupTO jobGroupTO) throws SerException {

    }

    /**
     * 删除任务调度组
     *
     * @param id
     */
    default void delete(String id) throws SerException {

    }

    /**
     * 启用关闭任务调度组
     *
     * @param enable
     */
    default void enable(String id, boolean enable) throws SerException {

    }
}
