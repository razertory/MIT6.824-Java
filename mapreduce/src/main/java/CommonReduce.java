import com.alibaba.fastjson.JSON;
import common.KeyValue;
import common.Util;
import func.ReduceFunc;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author gusu
 * @date 2022/7/23
 */
public class CommonReduce {

    public void doReduce(ReduceFunc reduceFunc, Integer id, Integer nMap, String reduceFilePath) {
        Map<String, List<String>> kvsMap = new HashMap<>();
        List<String> ks = new ArrayList<>();
        for (int i = 0; i < nMap; i++) {
            String tempFile = CommonFile.tempFileBase(i, id);
            Stream<String> stream = Util.stream(tempFile);
            stream.forEach(
                s -> {
                    KeyValue keyValue = JSON.parseObject(
                        new String(s.getBytes(StandardCharsets.UTF_8)),
                        KeyValue.class);
                    List<String> keyValues = kvsMap.getOrDefault(keyValue.getKey(),
                        new ArrayList<>());
                    keyValues.add(keyValue.getValue());
                    kvsMap.put(keyValue.getKey(), keyValues);
                    ks.add(keyValue.getKey());
                });
        }
        ks.sort(String::compareTo);
        ks.forEach(k -> {
                List<String> v = kvsMap.get(k);
                String cnt = reduceFunc.reduceF(k, v);
                Util.write(reduceFilePath, JSON.toJSONString(new KeyValue(k, cnt)));
            }
        );
    }
}
