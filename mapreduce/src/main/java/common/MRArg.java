package common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MRArg {

    private Integer workerType;
    private Integer workerId;
    private Integer mapNum;
    private Integer reduceNum;
    private String jobFile;
    private String reduceOutFile;
    private Boolean done = false;

    public MRArg(MRTask mrTask) {
        this.workerId = mrTask.getId();
        this.workerType = mrTask.getTaskType();
        this.jobFile = mrTask.getFile();
        this.reduceNum = mrTask.getReduceNum();
        this.mapNum = mrTask.getMapNum();
        this.reduceOutFile = mrTask.getReduceOutFile();
    }

    public static MRArg mapWork(Integer workerId, Integer reduceNum, String jobFile) {
        MRArg mrArg = new MRArg();
        mrArg.workerType = Cons.TASK_TYPE_MAP;
        mrArg.workerId = workerId;
        mrArg.reduceNum = reduceNum;
        mrArg.jobFile = jobFile;
        return mrArg;
    }

    public static MRArg reduceWork(Integer workerId, Integer mapNum,
        String reduceOutFile) {
        MRArg mrArg = new MRArg();
        mrArg.workerType = Cons.TASK_TYPE_REDUCE;
        mrArg.workerId = workerId;
        mrArg.mapNum = mapNum;
        mrArg.reduceOutFile = reduceOutFile;
        return mrArg;
    }


    public static MRArg empty(Boolean done) {
        MRArg mrArg = new MRArg();
        mrArg.setDone(done);
        return mrArg;
    }
}
