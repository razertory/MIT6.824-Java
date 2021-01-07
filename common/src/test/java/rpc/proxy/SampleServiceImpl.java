/*
 * SampleServiceImpl.java

 */

package rpc.proxy;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class SampleServiceImpl implements SampleService{

    public String foo(String greet) {
        return "foo" + greet;
    }
}
