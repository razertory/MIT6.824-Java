/*
 * Distributed.java
 * Copyright 2022 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

import biz.WordCount;
import common.Cons;
import common.WorkerAttr;
import func.MapFunc;
import func.ReduceFunc;
import java.util.List;

/**
 * @author gusu
 * @date 2022/7/28
 */
public class Distributed {
    public void run(MapFunc mapFunc, ReduceFunc reduceFunc, List<String> paths, Integer mapNum,
        Integer reduceNum) {
        Master master = new Master(paths, mapNum, reduceNum);
        master.setPort(Cons.MASTER_HOST);
        Worker worker = new Worker();
        worker.setPort(Cons.MASTER_HOST + 1);
        worker.serve();
        master.serve();
        worker.start(mapFunc, reduceFunc);
    }
}
