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

    public static MRArg empty() {
        return new MRArg();
    }
}
