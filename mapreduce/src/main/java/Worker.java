import com.alibaba.fastjson.JSON;
import common.MRDto;
import func.MapFunc;
import func.ReduceFunc;
import rpc.io.RpcClient;

/**
 * @author razertory
 * @date 2021/1/1
 */
public class Worker {
    private Integer masterHost;

    public Worker(Integer masterHost) {
        this.masterHost = masterHost;
    }

    public void work(MapFunc mapFunc, ReduceFunc reduceFunc) {
        Object o = new RpcClient().call(masterHost, "assignTask", null);
        MRDto mrDto = JSON.parseObject(o.toString(), MRDto.class);
    }

    public static void main(String[] args) {
    }
}
