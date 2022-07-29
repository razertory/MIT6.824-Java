import biz.WordCount;
import common.Cons;
import common.FileUtil;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFileChooser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MRWordCountTest {

    private final String ARTICLES_DIR = "src/main/resources/articles";
    private final String TEMP_FILE_DIR = "src/main/resources/temp";
    private final String OUT_FILE_DIR = "src/main/resources/out";
    private final List<String> paths = FileUtil.dirFiles(ARTICLES_DIR);

    @Before
    public void setUp() {
        FileUtil.dirFiles(TEMP_FILE_DIR).forEach(f -> FileUtil.delete(f));
        FileUtil.dirFiles(OUT_FILE_DIR).forEach(f -> FileUtil.delete(f));
    }

    @After
    public void clean() {
        FileUtil.dirFiles(TEMP_FILE_DIR).forEach(f -> FileUtil.delete(f));
        FileUtil.dirFiles(OUT_FILE_DIR).forEach(f -> FileUtil.delete(f));
    }

    @Test
    public void testWordCountSeq() {
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

    @Test
    public void testWordCountDistributed() throws Exception {
        WordCount wordCount = new WordCount();
        new Distributed().run(wordCount.mapFunc, wordCount.reduceFunc, paths, 2, 1);
        String expect = FileUtil.readFile(CommonFileTest.MR_EXPECT_OUT);
        String act = FileUtil.readFile(CommonFile.MR_MERGE_OUT);
        Assert.assertEquals(expect, act);
    }
}
