import com.alibaba.fastjson.JSON;
import common.KeyValue;
import common.FileUtil;
import func.MapFunc;
import java.util.List;

/**
 * @author gusu
 * @date 2022/7/23
 */
public class CommonMap {

    public void doMap(MapFunc mapFunc, Integer workerId, String filePath, Integer nReduce) {
        String cnt = FileUtil.readFile(filePath);
        List<KeyValue> values = mapFunc.mapF(filePath, cnt);
        values.forEach(p -> {
            String key = p.getKey(), tmPath = tempFilePath(workerId, key, nReduce);
            FileUtil.append(tmPath, JSON.toJSONString(p));
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
        return CommonFile.mrTempFile(workerId, hashNum);
    }
}
