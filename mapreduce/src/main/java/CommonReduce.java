import com.alibaba.fastjson.JSON;
import common.FileUtil;
import common.KeyValue;
import func.ReduceFunc;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import util.LogUtil;

/**
 * @author gusu
 * @date 2022/7/23
 */
public class CommonReduce {

    public void doReduce(ReduceFunc reduceFunc, Integer id, Integer nMap, String reduceFilePath) {
        Map<String, List<String>> kvsMap = new HashMap<>();
        for (int i = 0; i < nMap; i++) {
            String tempFile = CommonFile.mrTempFile(i, id);
            Stream<String> stream = FileUtil.stream(tempFile);
            if (stream == null) {
                return;
            }
            stream.forEach(
                s -> {
                    KeyValue keyValue = JSON.parseObject(
                        new String(s.getBytes(StandardCharsets.UTF_8)),
                        KeyValue.class);
                    List<String> keyValues = kvsMap.getOrDefault(keyValue.getKey(),
                        new ArrayList<>());
                    keyValues.add(keyValue.getValue());
                    kvsMap.put(keyValue.getKey(), keyValues);
                });
        }
        List<String> keys = new ArrayList<>(kvsMap.keySet()).stream().sorted(String::compareTo)
            .collect(Collectors.toList());
        keys.forEach(k -> {
                List<String> v = kvsMap.get(k);
                String cnt = reduceFunc.reduceF(k, v);
                FileUtil.append(reduceFilePath, JSON.toJSONString(new KeyValue(k, cnt)));
            }
        );
        LogUtil.log("ok to do reduce for id: " + id);
    }
}
