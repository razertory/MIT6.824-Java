import com.alibaba.fastjson.JSON;
import util.FileUtil;
import common.KeyValue;
import func.MapFunc;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.LogUtil;

/**
 * @author gusu
 * @date 2022/7/23
 */
public class CommonMap {

    public void doMap(MapFunc mapFunc, Integer workerId, String filePath, Integer nReduce) {
        String cnt = FileUtil.readFile(filePath);
        List<KeyValue> values = mapFunc.mapF(filePath, cnt);
        Map<String, String> toWrite = new HashMap<>();
        values.forEach(p -> {
            String key = p.getKey(), tmPath = tempFilePath(workerId, key, nReduce);
            String s = toWrite.getOrDefault(tmPath, "");
            String data = s + JSON.toJSONString(p) + "\n";
            toWrite.put(tmPath, data);
        });
        toWrite.forEach((k, v) -> FileUtil.write(k, v));
        LogUtil.log("ok to do map for id: " + workerId);
    }

    /**
     * 选择文件
     *
     * @param key
     * @return
     */
    private Integer hash(String key, Integer nReduce) {
        return Math.abs(key.hashCode()) % nReduce;
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
        return CommonFile.mrTempFile(workerId, hashNum);
    }
}
