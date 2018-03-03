package annotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Test {
    
    public static void main(String[] args) 
            throws CheckException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        
        System.out.println(toCodePointArray(new String(new char[]{'\udc28'})).length);
    }
    
    public static int[] toCodePointArray(String str) { // Example 1-2
        int len = str.length();          // the length of str
        int[] acp;                       // an array of code points
        int surrogatePairCount = 0;      // the count of surrogate pairs

        for (int i = 1; i < len; i++) {
            if (Character.isSurrogatePair(str.charAt(i - 1), str.charAt(i))) {
                surrogatePairCount++;
                i++;
            }
        }
        acp = new int[len - surrogatePairCount];
        for (int i = 0, j = 0; i < len; i++) {
            char ch0 = str.charAt(i);         // the current char
            if (Character.isHighSurrogate(ch0) && i + 1 < len) {
                char ch1 = str.charAt(i + 1); // the next char
                if (Character.isLowSurrogate(ch1)) {
                    acp[j++] = Character.toCodePoint(ch0, ch1);
                    i++;
                    continue;
                }
            }
            acp[j++] = ch0;
        }
        return acp;
    }
}
