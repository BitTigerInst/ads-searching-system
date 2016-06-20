package io.bittiger.ads.activity;

import java.util.*;
import org.junit.Test;


public class QueryUnderstandingTest {

    @Test
    public void queryUnderstandingTest() {

        List<String> res = QueryUnderstanding.getInstance().parseQ("I love my family***. I have a horse, look at that bird.");
        System.out.println(res);
        List<String> res1 = QueryUnderstanding.getInstance().parseQuery("I love ***my family. I have a horse, look at that bird.");
     //   System.out.println(QueryUnderstanding.getInstance().transfomTerm("I l****ove my family. I have a horse, look at that bird."));
        System.out.println(res1);
    }

}
