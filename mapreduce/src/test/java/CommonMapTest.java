import common.KeyValue;
import func.MapFunc;
import java.util.Arrays;
import java.util.List;

public class CommonMapTest {

    @org.junit.Test
    public void doMap() {
        CommonMap commonMap = new CommonMap();
        MapFunc mapFunc = (file, cnt) -> {
            return Arrays.asList(new KeyValue("1", "1"));
        };
        commonMap.doMap(mapFunc, 1, "src/main/resources/articles/pg-being_ernest.txt", 1);
    }
}