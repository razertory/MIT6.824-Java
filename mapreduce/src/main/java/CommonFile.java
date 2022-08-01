import com.alibaba.fastjson.JSON;
import common.FileUtil;
import common.KeyValue;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommonFile {

    public static final String MR_MERGE_OUT = "src/main/resources/out/mr-merge-out.txt";

    public static String mrTempFile(Integer mapId, Integer reduceId) {
        String base = "src/main/resources/temp/mr-iterm-%s-%s";
        String ret = String.format(base, mapId, reduceId);
        return ret;
    }

    public static String reduceOutFile(Integer reduceId) {
        String base = "src/main/resources/temp/mr-reduce-%s";
        return String.format(base, reduceId);
    }

    public static void mergeReduceOutFiles(List<String> reduceFiles) {
        Map<String, String> kvs = new HashMap<>();
        reduceFiles.forEach(reduceFile -> {
            Stream<String> stream = FileUtil.stream(reduceFile);
            if (stream == null) {
                return;
            }
            stream.forEach(s -> {
                    KeyValue keyValue = JSON.parseObject(s.getBytes(StandardCharsets.UTF_8),
                        KeyValue.class);
                    kvs.put(keyValue.getKey(), keyValue.getValue());
                }
            );
        });
        List<String> keys = new ArrayList<>(kvs.keySet());
        keys.sort(String::compareTo);
        keys.forEach(key -> {
            FileUtil.append(MR_MERGE_OUT,
                key + ": " + kvs.get(key));
        });
    }
}
