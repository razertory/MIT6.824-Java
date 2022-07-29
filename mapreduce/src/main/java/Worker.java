import com.alibaba.fastjson.JSON;
import common.Cons;
import common.MRArg;
import func.MapFunc;
import func.ReduceFunc;
import java.util.Arrays;
import rpc.io.RpcNode;
import util.LogUtil;

/**
 * @author razertory
 * @date 2021/1/1
 */
public class Worker extends RpcNode {

    private CommonMap commonMap = new CommonMap();
    private CommonReduce commonReduce = new CommonReduce();

    public void start(MapFunc mapFunc, ReduceFunc reduceFunc) {
        MRArg mrArg = requireTask(getPort());
        while (mrArg.getWorkerId() != null) {
            if (mrArg.getWorkerType().equals(Cons.TASK_TYPE_MAP)) {
                commonMap.doMap(mapFunc, mrArg.getWorkerId(), mrArg.getJobFile(),
                    mrArg.getReduceNum());
            }
            if (mrArg.getWorkerType().equals(Cons.TASK_TYPE_REDUCE)) {
                commonReduce.doReduce(reduceFunc, mrArg.getWorkerId(), mrArg.getMapNum(),
                    mrArg.getReduceOutFile());
            }
            mrArg = doneTask(mrArg.getWorkerId(), mrArg.getWorkerType());
        }
    }

    private MRArg requireTask(Integer port) {
         MRArg arg = JSON.parseObject(
            call(Cons.MASTER_HOST, "requireTask", Arrays.asList(port)).toString(),
            MRArg.class);
         return arg;
    }

    private MRArg doneTask(Integer id, Integer type) {
        return JSON.parseObject(
            call(Cons.MASTER_HOST, "doneTask", Arrays.asList(id, type)).toString(), MRArg.class);
    }

    public String rpcPing() {
        return "pong";
    }
}
