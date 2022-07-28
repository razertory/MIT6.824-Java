import com.alibaba.fastjson.JSON;
import common.KeyValue;
import common.Util;
import func.MapFunc;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author gusu
 * @date 2022/7/23
 */
public class CommonMap {

    public void doMap(MapFunc mapFunc, Integer workerId, String filePath, Integer nReduce) {
        String cnt = Util.readFile(filePath);
        List<KeyValue> values = mapFunc.mapF(filePath, cnt);
        values.forEach(p -> {
            String key = p.getKey(), tmPath = tempFilePath(workerId, key, nReduce);
            Util.append(tmPath, JSON.toJSONString(p).getBytes(StandardCharsets.UTF_8));
        });
    }

    /**
     * 选择文件
     *
     * @param key
     * @return
     */
    private Integer hash(String key, Integer nReduce) {
        return key.hashCode() % nReduce;
    }

    /**
     * 中间文件路径
     *
     * @param workerId
     * @param key
     * @param nReduce
     * @return
     */
    private String tempFilePath(Integer workerId, String key, Integer nReduce) {
        Integer hashNum = hash(key, nReduce);
        return CommonFile.tempFileBase(workerId, hashNum);
    }
}
