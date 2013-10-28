package com.echonest.knowledge.hashr;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
// Deprecated as of Lucene 3.6 (more than 2 versions ago)
//import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class HashFilter extends TokenFilter {

    protected final static Logger log = LoggerFactory.getLogger(HashFilter.class);

    private CharTermAttribute termAtt;
    
//    private TermToBytesRefAttribute termAtt;

    private OffsetAttribute offAtt;

    private int prevOff;

    public HashFilter(TokenStream input) {
        super(input);
        termAtt = (CharTermAttribute) addAttribute(CharTermAttribute.class);
//		termAtt = (TermToBytesRefAttribute) addAttribute(TermToBytesRefAttribute.class);
        offAtt = (OffsetAttribute) addAttribute(OffsetAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if(input.incrementToken()) {
            //
            // Save the state for this token, since we want the position in
            // the next token.
            State s = captureState();
            if(input.incrementToken()) {

                //
                // Parse the position from the next token.  Wasteful, but what you
                // gonna do, unless we want to implement our own parseint.
//                String ps = termAtt.term();
            	String ps = new String(termAtt.buffer(), 0, termAtt.length());
//            	String ps = new String(termAtt.termBuffer(), 0, token.termLength())
                int posn;
                try {
                    posn = Integer.parseInt(ps);
                } catch(NumberFormatException ex) {
                    throw new IOException(String.format("Bad offset %s", ps), ex);
                }
                restoreState(s);

                //
                // A finger print extends from the previous position to this one.
                offAtt.setOffset(prevOff, posn);
                prevOff = posn;
                return true;
            }
        }
        return false;
    }
}