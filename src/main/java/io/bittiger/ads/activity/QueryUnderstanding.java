package io.bittiger.ads.activity;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

;

public class QueryUnderstanding {
    private static QueryUnderstanding instance = null;

    protected QueryUnderstanding() {
    }

    public static QueryUnderstanding getInstance() {
        if (instance == null) {
            instance = new QueryUnderstanding();
        }
        return instance;
    }

    public String[] parseQuery(String query) {

        Reader r = new StringReader(query);

        List<String> words = new ArrayList<String>();

        TokenStream stream = new StopAnalyzer().tokenStream("", r);

        try {
            stream.reset();

            CharTermAttribute termAtt = stream.getAttribute(CharTermAttribute.class);

            while (stream.incrementToken()) {
                String text = termAtt.toString();
                words.add(text);
//                System.out.println(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return deduplicate(words);
    }

    private String[] deduplicate(List<String> words) {
        Set<String> hashset = new HashSet<String>();
        List<String> output = new ArrayList<String>();
        for (String str : words) {
            if (!hashset.contains(str)) {
                output.add(str);
                hashset.add(str);
            }
        }
        String[] out = new String[output.size()];
        return output.toArray(out);
    }
}
