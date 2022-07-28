import com.alibaba.fastjson.JSON;
import common.KeyValue;
import common.Util;
import func.MapFunc;
import java.util.Arrays;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class CommonMapTest {

    @Before
    public void clean() {
        Util.delete("src/main/resources/temp/mr-iterm-1-0");
    }

    @After
    public void after() {
        Util.delete("src/main/resources/temp/mr-iterm-1-0");
    }

    @org.junit.Test
    public void doMap() {
        CommonMap commonMap = new CommonMap();
        MapFunc mapFunc = (file, cnt) -> Arrays.asList(new KeyValue("1", "1"));
        commonMap.doMap(mapFunc, 1, "src/main/resources/articles/pg-being_ernest.txt", 1);
        String ret = Util.readFile("src/main/resources/temp/mr-iterm-1-0");
        KeyValue exp = new KeyValue("1", "1");
        Assert.assertEquals(exp, JSON.parseObject(ret, KeyValue.class));
    }
}