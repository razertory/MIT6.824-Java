import func.MapFunc;
import func.ReduceFunc;

/**
 * @author razertory
 * @date 2021/1/1
 */
public class Worker {

    private Integer masterHost;
    private Integer id;

    public Worker(Integer masterHost, Integer id) {
        this.masterHost = masterHost;
        this.id = id;
    }

    public void doTask(MapFunc mapFunc, ReduceFunc reduceFunc) {
    }


    public static void main(String[] args) {
    }
}
