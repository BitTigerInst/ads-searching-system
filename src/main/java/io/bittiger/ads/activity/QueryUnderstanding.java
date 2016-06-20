package io.bittiger.ads.activity;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.miscellaneous.RemoveDuplicatesTokenFilter;
import org.apache.lucene.analysis.miscellaneous.StemmerOverrideFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static sun.jvm.hotspot.oops.CellTypeState.top;

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

    public List<String> parseQuery(String userInput) {

        List<String> res = new ArrayList<String>();

        Reader reader = new StringReader(userInput);
        Analyzer analyzer = new StandardAnalyzer();

        TokenStream tokenStream = analyzer.tokenStream(userInput, reader);
        tokenStream = new RemoveDuplicatesTokenFilter(tokenStream);

        try{
            tokenStream.reset();
            CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
            while(tokenStream.incrementToken()) {
                res.add(term.toString());
            }
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    public List<String> parseQ(String userInput) {

        List<String> res = new ArrayList<String>();
        StandardTokenizer analyzer = new StandardTokenizer();

        //define your attribute factory (or use the default)
        AttributeFactory factory = AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY;

        /* Create the tokenizer and prepare it for reading
        StandardTokenizer tokenizer =
                new StandardTokenizer(factory);
        tokenizer.setReader(new StringReader(userInput));

        */
        StandardTokenizer tokenizer =
                new StandardTokenizer(factory);
        tokenizer.setReader(new StringReader(userInput));

        RemoveDuplicatesTokenFilter removeDup = new RemoveDuplicatesTokenFilter(tokenizer);

        CharTermAttribute charTermAttribute = removeDup.addAttribute(CharTermAttribute.class);
        charTermAttribute

        try{
            Analyzer.TokenStreamComponents tsc = removeDup.incrementToken();
TokenStream ts = tsc.getTokenStream();
            // NOTE: Here I'm adding a single expected attribute to handle string tokens,
//  but you would probably want to do something more meaningful/elegant
         //   CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);

            while(top.incrementToken()) {
                // Grab the term
                String term = charTermAttribute.toString();
                res.add(term);
            }
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    public String transfomTerm(String userInput) {


        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser(userInput, analyzer);
        Query query;
        try {
             query = parser.parse(userInput);
        }catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
        return query.toString();
    }
}
