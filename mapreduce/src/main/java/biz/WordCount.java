package biz;

import common.KeyValue;
import func.MapFunc;
import func.ReduceFunc;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author gusu
 * @date 2022/7/28
 */
public class WordCount {

    public MapFunc mapFunc = (String file, String cnt) -> {
        List<String> words = words(cnt);
        return words.stream().map(word -> new KeyValue(word, "1")).collect(Collectors.toList());
    };

    public ReduceFunc reduceFunc = (String key, List<String> values) -> String.valueOf(
        values.size());

    private List<String> words(String cnt) {
        return Pattern.compile("\\W+").splitAsStream(cnt).collect(Collectors.toList());
    }
}
