
import common.FileUtil;
import java.util.Arrays;
import java.util.Collections;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommonFileTest {

    public static final String MR_EXPECT_OUT = "src/main/resources/expect/word-count.txt";
    public static final String MR_EXPECT_REDUCE = "src/main/resources/expect/word-count-reduce.txt";

    @Before
    public void setUp() {
        FileUtil.delete(CommonFile.MR_MERGE_OUT);
    }

    @After
    public void tearDown() {
        FileUtil.delete(CommonFile.MR_MERGE_OUT);
    }

    @Test
    public void mergeReduceFile() {
        CommonFile.mergeReduceOutFiles(Collections.singletonList(MR_EXPECT_REDUCE));
        String expect = FileUtil.readFile(MR_EXPECT_OUT);
        String act = FileUtil.readFile(CommonFile.MR_MERGE_OUT);
        Assert.assertEquals(expect, act);
    }
}