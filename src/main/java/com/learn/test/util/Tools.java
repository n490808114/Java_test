package com.learn.test.util;

import java.util.Iterator;

public class Tools {
    /**
     * Strings.join方法
     * @param pieces String List
     * @param separator 间隔字符串
     * @return 合成的字符串
     */
    public static String join( Iterable<String> pieces, String separator ) {
        StringBuilder buffer = new StringBuilder();

        for (Iterator<String> iter = pieces.iterator(); iter.hasNext(); ) {
            buffer.append( iter.next() );

            if ( iter.hasNext() ) {
                buffer.append( separator );
            }
        }

        return buffer.toString();
    }
}
