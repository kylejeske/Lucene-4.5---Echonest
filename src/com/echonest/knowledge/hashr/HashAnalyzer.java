package com.echonest.knowledge.hashr;

import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

/**
 * An analyzer for a fingerprint hash field that has the form:
 *
 * <pre>
 * <hash> <offset> <hash> <offset>...
 * </pre>
 */
public class HashAnalyzer extends Analyzer {

    protected final static Logger logger = Logger.getLogger(
            HashAnalyzer.class.getName());

    @Override
    public TokenStreamComponents createComponents(String string, Reader reader) {
    	Tokenizer source = new WhitespaceTokenizer(null, reader);
    	HashFilter filter = new HashFilter(source);
    	return new TokenStreamComponents(source, filter);
    }
}