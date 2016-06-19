package io.bittiger.ads.activity;

import org.junit.Test;

import java.io.IOException;

public class QueryUnderstandingTest {

    @Test
    public void test() throws IOException {
        String query = "I love my family. I have a new house. Look at that bird!";
        String[] res = QueryUnderstanding.getInstance().parseQuery(query);

        for (String str : res) {
            System.out.println(str);
        }
    }
}
