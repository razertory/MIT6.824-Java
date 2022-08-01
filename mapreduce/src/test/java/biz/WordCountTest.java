package biz;

import common.KeyValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WordCountTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCount() {
        String cnt = "Apart from counting words and characters, our online editor can help you to improve word choice and writing style, and, optionally, help you to detect grammar mistakes and plagiarism. To check word count, simply place your cursor into the text box above and start typing. You'll see the number of characters and words increase or decrease as you type, delete, and edit them. You can also copy and paste text from another program over into the online editor above. The Auto-Save feature will make sure you won't lose any changes while editing, even if you leave the site and come back later. Tip: Bookmark this page now.";
        WordCount wordCount = new WordCount();
        List<KeyValue> values = wordCount.mapFunc.mapF("", cnt);
        Map<String, List<String>> kvsMap = new HashMap<>();
        values.forEach(kv -> {
            List<String> keyValues = kvsMap.getOrDefault(kv.getKey(),
                new ArrayList<>());
            keyValues.add(kv.getValue());
            kvsMap.put(kv.getKey(), keyValues);
        });
        Map<String, String> retMap = new HashMap<>();
        kvsMap.forEach((k, v) -> {
            String r = wordCount.reduceFunc.reduceF(k, v);
            retMap.put(k, r);
        });
        Assert.assertEquals("2", retMap.get("words"));
        Assert.assertEquals("1", retMap.get("counting"));
        Assert.assertEquals("9", retMap.get("and"));
    }
}