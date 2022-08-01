/*
 * Master.java

 */

import common.Cons;
import common.MRArg;
import common.MRTask;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import rpc.io.RpcNode;
import util.LogUtil;


/**
 * @author razertory
 * @date 2021/1/1
 */
public class Master extends RpcNode {


    private List<String> files;
    private Integer reduceNum;
    private Set<Integer> workerPorts = new HashSet<>();
    private ConcurrentLinkedDeque<MRTask> tasks = new ConcurrentLinkedDeque<>();
    private ConcurrentLinkedDeque<MRTask> reduceTasks = new ConcurrentLinkedDeque<>();
    private Set<String> reduceOutFiles = new HashSet<>();
    private Set<Integer> doneMapTasks = new HashSet<>();

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void run() throws Exception {
        serve();
        checkWorkers();
    }

    private void checkWorkers() {
        Runnable runnable = () -> {
            while (true) {
                for (Integer port : workerPorts) {
                    Object ret = call(port, "rpcPing", new Object[]{port + ""});
                    if (ret == null) {
                        LogUtil.log("ok to find fail worker");
                    }
                }
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                }
            }
        };
        new Thread(runnable).start();
    }

    public Master(List<String> files, Integer reduceNum) {
        this.reduceNum = reduceNum;
        this.files = files;
        for (int i = 0; i < files.size(); i++) {
            String file = files.get(i);
            tasks.offer(new MRTask(i, Cons.TASK_TYPE_MAP, Cons.TASK_STATUS_TODO, file, reduceNum,
                files.size(), ""));
        }
        for (int i = 0; i < reduceNum; i++) {
            reduceTasks.offer(
                new MRTask(i, Cons.TASK_TYPE_REDUCE, Cons.TASK_STATUS_TODO, "", reduceNum,
                    files.size(), CommonFile.reduceOutFile(i)));
        }
    }

    /**
     * 指派任务
     *
     * @return
     */
    public synchronized MRArg requireTask(Integer port) {
        workerPorts.add(port);
        if (tasks.isEmpty()) {
            return MRArg.empty(allFinished());
        }
        MRTask mrTask = tasks.poll();
        return new MRArg(mrTask);
    }

    /**
     * 任务完成
     *
     * @param workerId
     * @param taskType
     * @return
     */
    public synchronized MRArg doneTask(Integer workerId, final Integer taskType) {
        if (taskType.equals(Cons.TASK_TYPE_MAP)) {
            doneMapTasks.add(workerId);
        }
        if (!tasks.isEmpty()) {
            MRTask mrTask = tasks.poll();
            return new MRArg(mrTask);
        }
        if (taskType.equals(Cons.TASK_TYPE_REDUCE)) {
            reduceOutFiles.add(CommonFile.reduceOutFile(workerId));
        }
        if (allFinished()) {
            return MRArg.empty(true);
        }
        Boolean mapDone = doneMapTasks.size() == files.size();
        if (!reduceTasks.isEmpty() && mapDone) {
            MRTask mrTask = reduceTasks.poll();
            return new MRArg(mrTask);
        }
        return MRArg.empty(allFinished());
    }

    private synchronized Boolean allFinished() {
        Boolean done = this.reduceOutFiles.size() == this.reduceNum;
        if (done) {
            countDownLatch.countDown();
        }
        return done;
    }
}
