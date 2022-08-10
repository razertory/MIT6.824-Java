import biz.WordCount;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.FileUtil;

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
        compareOutFile();
    }

    /**
     * 分布式测试
     *
     * @throws Exception
     */
    @Test
    public void testWordCountDistributed() throws Exception {
        WordCount wordCount = new WordCount();
        new Distributed().run(wordCount.mapFunc, wordCount.reduceFunc, paths, 4);
        compareOutFile();
    }

    /**
     * 失败 1 个 worker
     *
     * @throws Exception
     */
    @Test
    public void testWordCountDistributedWithOneFail() throws Exception {
        WordCount wordCount = new WordCount();
        Distributed distributed = new Distributed();
        Runnable r = () -> {
            try {
                Thread.sleep(1000L);
                distributed.workerThreads.get(2).interrupt();
                distributed.workers.get(2).shutDown("this is a test shutDown");
            } catch (Exception e) {
            }
        };
        new Thread(r).start();
        distributed.run(wordCount.mapFunc, wordCount.reduceFunc, paths, 4);
        compareOutFile();
    }

    /**
     * 失败多个 workers
     *
     * @throws Exception
     */
    @Test
    public void testWordCountDistributedWithManyFails() throws Exception {
        WordCount wordCount = new WordCount();
        Distributed distributed = new Distributed();
        Runnable r = () -> {
            try {
                Thread.sleep(1000L);
                distributed.workerThreads.get(2).interrupt();
                distributed.workers.get(2).shutDown("this is a test shutDown");
                distributed.workerThreads.get(1).interrupt();
                distributed.workers.get(1).shutDown("this is a test shutDown");
            } catch (Exception e) {
            }
        };
        new Thread(r).start();
        distributed.run(wordCount.mapFunc, wordCount.reduceFunc, paths, 4);
        compareOutFile();
    }

    private void compareOutFile() {
        String expect = FileUtil.readFile(CommonFileTest.MR_EXPECT_OUT);
        String act = FileUtil.readFile(CommonFile.MR_MERGE_OUT);
        Assert.assertEquals(expect, act);
    }
}
