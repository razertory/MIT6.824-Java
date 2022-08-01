package util;

import java.util.Date;

/**
 * @author gusu
 * @date 2021/1/7
 */
public class LogUtil {

    public static void log(Object s) {
        Date date = new Date();
        System.out.println(date.toString() + " " + s);
    }
}
