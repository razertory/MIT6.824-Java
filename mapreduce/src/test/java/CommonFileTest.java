
import common.FileUtil;
import java.util.Arrays;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommonFileTest {

    public static final String MR_EXPECT_OUT = "src/main/resources/result/expect/word-count.txt";

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
        CommonFile.mergeReduceOutFiles(Arrays.asList("src/main/resources/result/seq-reduce.txt"));
        String expect = FileUtil.readFile(MR_EXPECT_OUT);
        String act = FileUtil.readFile(CommonFile.MR_MERGE_OUT);
        Assert.assertEquals(expect, act);
    }
}