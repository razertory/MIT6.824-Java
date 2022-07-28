package biz;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import common.KeyValue;
import func.MapFunc;
import func.ReduceFunc;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gusu
 * @date 2022/7/28
 */
public class WordCount {

    public MapFunc mapFunc = (String file, String cnt) -> {
        List<String> words = splitToWords(cnt);
        return words.stream().map(word -> new KeyValue(word, "1")).collect(Collectors.toList());
    };

    public ReduceFunc reduceFunc = (String key, List<String> values) -> String.valueOf(
        values.size());

    private List<String> splitToWords(String cnt) {
        List<String> ret = new ArrayList<>(32);
        Splitter.on(new CharMatcher() {
            @Override
            public boolean matches(char c) {
                return !Character.isLetter(c);
            }
        }).omitEmptyStrings().split(cnt).forEach(c -> {
            ret.add(c);
        });
        return ret;
    }
}
