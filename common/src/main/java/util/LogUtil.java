package util;

import java.util.Date;

/**
 * @author gusu
 * @date 2021/1/7
 */
public class LogUtil {

    public static void log(Object... args) {
        String now = new Date().toString();
        StringBuffer sb = new StringBuffer();
        for (Object arg : args) {
            sb.append(arg);
        }
        String log = now + "-" + sb;
        FileUtil.append("logs/log.log", log);
        System.out.println(log);
    }
}
