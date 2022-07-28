import com.alibaba.fastjson.JSON;
import common.KeyValue;
import common.Util;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommonFile {

    public static String tempFileBase(Integer mapId, Integer reduceId) {
        String base = "src/main/resources/temp/mr-iterm-%s-%s";
        return String.format(base, mapId, reduceId);
    }

    public static void mergeReduceFile(List<String> reduceFiles) {
        Map<String, String> kvs = new HashMap<>();
        reduceFiles.forEach(reduceFile -> {
            Stream<String> stream = Util.stream(reduceFile);
            stream.forEach(s -> {
                    KeyValue keyValue = JSON.parseObject(s.getBytes(StandardCharsets.UTF_8),
                        KeyValue.class);
                    kvs.put(keyValue.getKey(), keyValue.getValue());
                }
            );
        });
        List<String> keys = new ArrayList<>(kvs.keySet());
        keys.sort(String::compareTo);
        kvs.forEach((k, v) -> {
            Util.append("src/main/resources/articles/mr-merge-out.txt", (k + ": " + v).getBytes(
                StandardCharsets.UTF_8));
        });
    }
}
