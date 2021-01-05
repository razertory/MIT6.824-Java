/*
 * ProxyFactory.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gusu
 * @date 2021/1/1
 */
public class BeanFactory {
    private static Map<String, Object> beanMap = new HashMap();

    public static void putBean(String name, Object object) {
        beanMap.put(name, object);
    }
    public static Object getBean(String string) {
        return beanMap.get(string);
    }
}
