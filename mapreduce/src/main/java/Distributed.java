/*
 * Distributed.java
 * Copyright 2022 Razertory, all rights reserved.
 * GUSU PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

import common.Cons;
import func.MapFunc;
import func.ReduceFunc;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import util.LogUtil;

/**
 * @author gusu
 * @date 2022/7/28
 */
public class Distributed {

    public List<Thread> workerThreads = new ArrayList<>();
    public List<Worker> workers = new ArrayList<>();

    public void run(MapFunc mapFunc, ReduceFunc reduceFunc, List<String> paths,
        Integer reduceNum) throws Exception {
        Master master = new Master(paths, reduceNum);
        master.setPort(Cons.MASTER_HOST);
        final CountDownLatch countDownLatch = master.getCountDownLatch();
        master.run();

        for (int i = 0; i < paths.size(); i++) {
            final Worker worker = newWorker();
            Runnable runnable = () -> {
                worker.work(mapFunc, reduceFunc);
            };
            Thread t = new Thread(runnable);
            workerThreads.add(t);
            workers.add(worker);
            t.start();
        }
        countDownLatch.await();

        List<String> files = new ArrayList<>();
        for (int i = 0; i < reduceNum; i++) {
            files.add(CommonFile.reduceOutFile(i));
        }
        CommonFile.mergeReduceOutFiles(files);
    }

    private Worker newWorker() {
        Integer port = (int) ((Math.random() * (15000 - 14000)) + 14000);
        Worker worker = new Worker();
        worker.setPort(port);
        try {
            worker.serve();
        } catch (Exception e) {
        }
        return worker;
    }
}
