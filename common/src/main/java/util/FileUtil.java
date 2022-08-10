/*
 * Util.java
 * Copyright 2022 Razertory, all rights reserved.
 * GUSU PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author gusu
 * @date 2022/7/27
 */
public class FileUtil {

    private static final String KEEP_FILE = ".keep";

    public static String readFile(String file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (Exception e) {
            LogUtil.log("fail to read file, " + file);
            return null;
        }
    }

    public static List<String> dirFiles(String dir) {
        try {
            return Files.list(Paths.get(dir))
                .map(Path::toFile)
                .filter(File::isFile)
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }

    public static Stream<String> stream(String file) {
        try {
            return Files.lines(Paths.get(file));
        } catch (Exception e) {
            return null;
        }
    }

    public static void append(String path, String cnt) {
        String toAppend = cnt + "\n";
        try {
            Files.write(
                Paths.get(path),
                toAppend.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        } catch (Exception e) {
            LogUtil.log("fail to write file, " + path + "err: " + e);
            e.printStackTrace();
        }
    }

    public static void write(String path, String cnt) {
        try {
            Files.write(
                Paths.get(path),
                cnt.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE);
        } catch (Exception e) {
            LogUtil.log("fail to write file, " + path + "err: " + e);
        }
    }

    /**
     * 忽略 keep 文件删除
     *
     * @param path
     */
    public static void delete(String path) {
        if (path.contains(KEEP_FILE)) {
            return;
        }
        try {
            Files.delete(Paths.get(path));
        } catch (Exception e) {
            LogUtil.log("ok to delete: " + path);
        }
    }

}
