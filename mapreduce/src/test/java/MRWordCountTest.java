import biz.WordCount;
import common.FileUtil;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MRWordCountTest {

    private final String ARTICLES_DIR = "src/main/resources/articles";
    private final String TEMP_FILE_DIR = "src/main/resources/temp";
    private final String OUT_FILE_DIR = "src/main/resources/out";

    @Before
    public void setUp() {
        FileUtil.delete(CommonFile.MR_MERGE_OUT);
        FileUtil.delete(CommonFile.mrTempFile(0, 0));
        FileUtil.dirFiles(TEMP_FILE_DIR).forEach(f -> FileUtil.delete(f));
        FileUtil.dirFiles(OUT_FILE_DIR).forEach(f -> FileUtil.delete(f));
    }

    @After
    public void clean() {
        FileUtil.delete(CommonFile.MR_MERGE_OUT);
        FileUtil.delete(CommonFile.mrTempFile(0, 0));
        FileUtil.dirFiles(TEMP_FILE_DIR).forEach(f -> FileUtil.delete(f));
        FileUtil.dirFiles(OUT_FILE_DIR).forEach(f -> FileUtil.delete(f));
    }

    @Test
    public void testWordCountSeq() {
        List<String> paths = FileUtil.dirFiles(ARTICLES_DIR);
        WordCount wordCount = new WordCount();
        new Single().run(
            wordCount.mapFunc,
            wordCount.reduceFunc,
            paths
        );
        String expect = FileUtil.readFile(CommonFileTest.MR_EXPECT_OUT);
        String act = FileUtil.readFile(CommonFile.MR_MERGE_OUT);
        Assert.assertEquals(expect, act);
    }
}
