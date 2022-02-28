/*
 * Master.java

 */

import common.Cons;
import common.MRDto;
import common.TaskFile;
import rpc.io.RpcServer;
import util.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author razertory
 * @date 2021/1/1
 */
public class Master extends RpcServer {
    private Integer reduceNum;
    private Boolean mapsDone;
    private Boolean done;
    private Map<String, TaskFile> inputFileMap;
    private Map<String, TaskFile> intermediateFileMap;

    public void start(Integer port, List<String> files, Integer reduceNum) {
        this.intermediateFileMap = new HashMap<>(files.size() + reduceNum);
        this.inputFileMap = new HashMap<>(files.size());
        files.forEach(file -> {
            this.inputFileMap.put(file, new TaskFile(Cons.TASK_STATUS_TODO, file));
        });
        this.mapsDone = false;
        this.done = false;
        this.reduceNum = reduceNum;
        try {
            serve(port);
        } catch (Exception e) {
            LogUtil.log("fail to start master server on port: " + port);
        }
    }

    /**
     * 指派任务
     *
     * @return
     */
    public MRDto assignTask() {
        for (String url : inputFileMap.keySet()) {
            TaskFile taskFile = inputFileMap.get(url);
            if (Cons.TASK_STATUS_TODO.equals(taskFile.getStatus())) {
                return new MRDto(Cons.TASK_TYPE_MAP, taskFile.getUrl());
            }
        }
        for (String url : intermediateFileMap.keySet()) {
            TaskFile taskFile = intermediateFileMap.get(url);
            if (Cons.TASK_STATUS_TODO.equals(taskFile.getStatus())) {
                return new MRDto(Cons.TASK_TYPE_REDUCE, taskFile.getUrl());
            }
        }
        return null;
    }

    public MRDto doneTask() {
        return null;
    }

    private boolean mapDone() {
        return false;
    }

    private boolean reduceDone() {
        return false;
    }
}
