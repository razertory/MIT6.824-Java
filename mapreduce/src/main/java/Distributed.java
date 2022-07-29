/*
 * Distributed.java
 * Copyright 2022 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

import biz.WordCount;
import com.google.common.collect.Range;
import common.Cons;
import common.WorkerAttr;
import func.MapFunc;
import func.ReduceFunc;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author gusu
 * @date 2022/7/28
 */
public class Distributed {

    public void run(MapFunc mapFunc, ReduceFunc reduceFunc, List<String> paths, Integer mapNum,
        Integer reduceNum) throws Exception {
        Master master = new Master(paths, mapNum, reduceNum);
        master.setPort(Cons.MASTER_HOST);
        master.serve();

        final CountDownLatch countDownLatch = new CountDownLatch(mapNum);

        for (int i = 0; i < mapNum; i++) {
            Runnable runnable = () -> {
                newWorker().start(mapFunc, reduceFunc);
                countDownLatch.countDown();
            };
            new Thread(runnable).start();
        }
        countDownLatch.await();
        List<String> files = master.getReducers().keySet().stream()
            .map(id -> CommonFile.reduceOutFile(id)).collect(
                Collectors.toList());
        CommonFile.mergeReduceOutFiles(files);
    }

    private Worker newWorker() {
        Integer port = (int) ((Math.random() * (15000 - 14000)) + 14000);
        Worker worker = new Worker();
        worker.setPort(port);
        return worker;
    }
}
