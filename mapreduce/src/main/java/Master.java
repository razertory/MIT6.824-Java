/*
 * Master.java

 */

import common.Cons;
import common.MRArg;
import common.WorkerAttr;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import rpc.io.RpcNode;
import util.LogUtil;


/**
 * @author razertory
 * @date 2021/1/1
 */
public class Master extends RpcNode {

    private Integer reduceNum;
    private Integer mapNum;
    private Map<Integer, WorkerAttr> mappers = new HashMap<>();
    private Map<Integer, WorkerAttr> reducers = new HashMap<>();
    private Map<String, Integer> inputFileStatusMap = new HashMap<>();
    private Set<Integer> workerPorts = new HashSet<>();

    public Map<Integer, WorkerAttr> getReducers() {
        return reducers;
    }

    public Master(List<String> files, Integer mapNum, Integer reduceNum) {
        files.forEach(file -> {
            inputFileStatusMap.put(file, Cons.TASK_STATUS_TODO);
        });
        this.mapNum = mapNum;
        this.reduceNum = reduceNum;
        for (int i = 0; i < mapNum; i++) {
            mappers.put(i, WorkerAttr.idleMapper(i));
        }
        for (int i = 0; i < reduceNum; i++) {
            reducers.put(i, WorkerAttr.idleReducer(i));
        }
    }

    private WorkerAttr idleMapper() {
        return mappers.values().stream()
            .filter(mapper -> mapper.getStatus().equals(Cons.TASK_STATUS_TODO)).findFirst()
            .orElse(null);
    }

    private WorkerAttr idleReducer() {
        LogUtil.log("reducers" + reducers);
        return reducers.values().stream()
            .filter(reducer -> reducer.getStatus().equals(Cons.TASK_STATUS_TODO)).findFirst()
            .orElse(null);
    }

    private String idleFile() {
        for (String url : inputFileStatusMap.keySet()) {
            if (inputFileStatusMap.get(url).equals(Cons.TASK_STATUS_TODO)) {
                return url;
            }
        }
        return null;
    }

    /**
     * 指派任务
     *
     * @return
     */
    public MRArg requireTask(Integer port) {
        workerPorts.add(port);
        WorkerAttr idleMapper = idleMapper();
        String idleFile = idleFile();
        if (idleMapper != null && idleFile != null) {
            idleMapper.setStatus(Cons.TASK_STATUS_DOING);
            idleMapper.setMapFile(idleFile);
            idleMapper.setPort(port);
            inputFileStatusMap.put(idleFile, Cons.TASK_STATUS_DOING);
            return MRArg.mapWork(idleMapper.getId(), reduceNum, idleFile);
        }
        WorkerAttr idleReducer = idleReducer();
        if (idleReducer != null) {
            Integer id = idleReducer.getId();
            reducers.get(id).setStatus(Cons.TASK_STATUS_DOING);
            return MRArg.reduceWork(id, mapNum, CommonFile.reduceOutFile(id));
        }
        return MRArg.empty();
    }


    /**
     * 任务完成
     *
     * @param workerId
     * @param taskType
     * @return
     */
    public MRArg doneTask(Integer workerId, final Integer taskType) {
        if (taskType.equals(Cons.TASK_TYPE_MAP)) {
            WorkerAttr workerAttr = mappers.get(workerId);
            workerAttr.setStatus(Cons.TASK_STATUS_DONE);
            inputFileStatusMap.put(workerAttr.getMapFile(), Cons.TASK_STATUS_DONE);
            String idleFile = idleFile();
            if (idleFile != null) {
                workerAttr.setMapFile(idleFile);
                inputFileStatusMap.put(idleFile, Cons.TASK_STATUS_DOING);
                return MRArg.mapWork(workerAttr.getId(), reduceNum, idleFile);
            }
        }
        if (taskType.equals(Cons.TASK_TYPE_REDUCE)) {
            WorkerAttr workerAttr = reducers.get(workerId);
            workerAttr.setStatus(Cons.TASK_STATUS_DONE);
        }
        WorkerAttr idleReducer = idleReducer();
        LogUtil.log("idleReducer, " + idleReducer);
        if (idleReducer != null) {
            Integer id = idleReducer.getId();
            reducers.get(id).setStatus(Cons.TASK_STATUS_DOING);
            return MRArg.reduceWork(id, mapNum, CommonFile.reduceOutFile(id));
        }
        return MRArg.empty();
    }

    public static void main(String[] args) {
    }
}
