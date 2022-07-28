/*
 * Util.java
 * Copyright 2022 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package common;

import com.alibaba.fastjson.JSON;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import util.LogUtil;

/**
 * @author gusu
 * @date 2022/7/27
 */
public class Util {

    public static String readFile(String file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (Exception e) {
            LogUtil.log("fail to read file, " + file);
            return null;
        }
    }

    public static void append(String path, KeyValue kv) {
        try {
            Files.write(
                Paths.get(path),
                JSON.toJSONString(kv).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        } catch (Exception e) {
            LogUtil.log("fail to write file, " + path);
        }
    }

    public static void delete(String path) {
        try {
            Files.delete(Paths.get(path));
        } catch (Exception e) {
        }
    }

}
