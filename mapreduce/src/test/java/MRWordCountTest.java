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
        FileUtil.delete(CommonFile.MR_MERGE_OUT);
        FileUtil.delete(CommonFile.mrTempFile(0, 0));
        FileUtil.dirFiles(TEMP_FILE_DIR).forEach(f -> FileUtil.delete(f));
        FileUtil.dirFiles(OUT_FILE_DIR).forEach(f -> FileUtil.delete(f));
    }

    @After
    public void clean() {
//        FileUtil.delete(CommonFile.MR_MERGE_OUT);
//        FileUtil.delete(CommonFile.mrTempFile(0, 0));
//        FileUtil.dirFiles(TEMP_FILE_DIR).forEach(f -> FileUtil.delete(f));
//        FileUtil.dirFiles(OUT_FILE_DIR).forEach(f -> FileUtil.delete(f));
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
    public void testMaster() {
    }

    @Test
    public void testWordCountDistributed() {
        WordCount wordCount = new WordCount();

        Master master = new Master(paths, 1, 1);
        master.setPort(Cons.MASTER_HOST);

        Worker worker = new Worker();
        worker.setPort(Cons.MASTER_HOST + 1);

        master.serve();
        worker.serve();

        worker.start(wordCount.mapFunc, wordCount.reduceFunc);

        String expect = FileUtil.readFile(CommonFileTest.MR_EXPECT_OUT);
        String act = FileUtil.readFile(CommonFile.MR_MERGE_OUT);
        Assert.assertEquals(expect, act);
    }
}
