/*
 * WorkerAttributes.java
 * Copyright 2022 Razertory, all rights reserved.
 * GUSU PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gusu
 * @date 2022/7/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerAttr {

    private Integer status;
    private Integer taskType;
    private Integer id;
    private Integer port;
    private String mapFile;

    public static WorkerAttr idleMapper(Integer id) {
        WorkerAttr workerAttr = new WorkerAttr();
        workerAttr.setId(id);
        workerAttr.setTaskType(Cons.TASK_TYPE_MAP);
        workerAttr.setStatus(Cons.TASK_STATUS_TODO);
        return workerAttr;
    }

    public static WorkerAttr idleReducer(Integer id) {
        WorkerAttr workerAttr = new WorkerAttr();
        workerAttr.setId(id);
        workerAttr.setTaskType(Cons.TASK_TYPE_REDUCE);
        workerAttr.setStatus(Cons.TASK_STATUS_TODO);
        return workerAttr;
    }

    public Boolean mapDone() {
        return Cons.TASK_TYPE_MAP.equals(taskType) && Cons.TASK_STATUS_DONE.equals(status);
    }

    public Boolean reduceDone() {
        return Cons.TASK_TYPE_REDUCE.equals(taskType) && Cons.TASK_STATUS_DONE.equals(status);
    }
}
