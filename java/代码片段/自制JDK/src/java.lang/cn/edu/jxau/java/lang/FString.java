package cn.edu.jxau.java.lang;

import java.util.Arrays;
import java.util.Objects;

public class FString implements CharSequence, Comparable<FString> {

    private final char[] value; // 字符串值
    private int hash;

    public FString() {
        value = new char[0];
    }

    public FString(String str) {
        this(str.toCharArray());
    }

    public FString(FString fStr) {

        this.value = fStr.value;
        this.hash = fStr.hash;
    }

    public FString(char[] value) {
        this(value, 0, value.length);
    }

    public FString(char[] value, int off, int len) {

        if (off < 0) {
            throw new IllegalArgumentException("参数off不能是负数");
        }
        if (len < 0) {
            throw new IllegalArgumentException("参数len不能是负数");
        }
        if (off > value.length - len) {
            throw new IllegalArgumentException(String.format("若从%d开始,value中剩余元素不足%d", off, len));
        }
        this.value = Arrays.copyOfRange(value, off, off + len);
    }

    @Override
    public boolean equals(Object str) {

        if (this == str) {
            return true;
        }
        if (str instanceof FString) {
            FString anotherStr = (FString) str;
            if (value.length == anotherStr.length()) {
                char[] v1 = value;
                char[] v2 = anotherStr.value;
                for (int i = 0; i < v1.length; i++) {
                    if (v1[i] != v2[i]) {
                        return false; // 长度相同 && 内容不同
                    }
                }
                return true; // 长度相同 && 内容相同
            }
        }
        return false; // 长度不同 || 内容不同
    }

    @Override
    public int hashCode() {

        if (hash == 0 && value.length != 0) {
            for (int i = 0; i < value.length; i++) { // s[0]*31^(n-1) +
                                                     // s[1]*31^(n-2) + ... +
                                                     // s[n-1]
                hash = hash * 31 + value[i];
            }
        }
        return hash;
    }

    @Override
    public String toString() {

        StringBuilder strBuilder = new StringBuilder();
        for (char ch : value) {
            strBuilder.append(ch);
        }
        return strBuilder.toString();
    }

    public boolean startsWith(FString prefix) {

        Objects.requireNonNull(prefix, "参数prefix为null");
        return startsWith(prefix, 0);
    }

    public boolean startsWith(FString prefix, int off) {

        // 参数校验 //
        Objects.requireNonNull(prefix, "参数prefix不能为null");
        if (off < 0) {
            throw new IllegalArgumentException("参数off不能是负数");
        }

        // 特殊情况处理 //
        if (this.length() - off < prefix.length()) { //
            return false;
        }

        // 比较 //
        char[] v1 = this.value;
        char[] v2 = prefix.value;
        for (int i = 0; i < v2.length; i++) {
            if (v1[i + off] != v2[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(FString suffix) {

        Objects.requireNonNull(suffix, "参数subfix为null");
        return startsWith(suffix, this.length() - suffix.length());
    }

    public FString concat(FString str) {

        Objects.requireNonNull(str, "参数str为null");
        if (str.length() == 0) {
            return str;
        }
        char[] ret = Arrays.copyOf(value, this.length() + str.length());
        System.arraycopy(str.value, 0, ret, this.length(), str.length());
        return new FString(ret);
    }

    @Override
    public int compareTo(FString str) {

        char[] v1 = value;
        char[] v2 = str.value;
        int len1 = v1.length;
        int len2 = v2.length;
        int lim = Math.min(len1, len2);
        for (int i = 0; i < lim; i++) {
            if (v1[i] != v2[i]) {
                return v1[i] - v2[i];
            }
        }
        return len1 - len2;
    }

    @Override
    public int length() {
        return value.length;
    }

    @Override
    public char charAt(int index) {

        if (index < 0 || index >= this.length()) {
            throw new IllegalArgumentException(String.format("参数index = %d越界", index));
        }
        return value[index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new FString(value, start, end);
    }

    public FString replace(char oldChar, char newChar) {

        if (oldChar != newChar) {
            int len = this.length();
            char[] ret = new char[len];
            int i = 0;
            while (i < len && oldChar != value[i]) {
                ret[i] = value[i];
                i++;
            }
            for (; i < len; i++) {
                ret[i] = value[i] == oldChar ? newChar : value[i];
            }
            return new FString(ret);
        }
        return this;
    }

    public FString trim() {

        int start = 0; // 第一个非空字符的索引
        int end = this.length(); // 最后一个非空字符的索引

        // 寻找第一个非空字符 //
        while (start < this.length() && value[start] == ' ') {
            start++;
        }
        if (start == this.length()) {
            return new FString(""); // 全是空字符
        }

        // 寻找最后非空字符 //
        while (value[end - 1] == ' ') {
            end--;
        }
        return new FString(this.substring(start, end));
    }

    public FString substring(int start) {

        if (start < 0) {
            throw new IllegalArgumentException("参数start不能是负数");
        }
        return substring(start, this.length());
    }

    public FString substring(int start, int end) {

        if (start < 0) {
            throw new IllegalArgumentException("参数start不能是负数");
        }
        if (end < 0) {
            throw new IllegalArgumentException("参数end不能是负数");
        }
        if (end > this.length()) {
            throw new IllegalArgumentException("参数end大于字符串长度");
        }
        return (start == 0 && end == this.length()) ? this : new FString(value, start, end - start);
    }

    public char[] toCharArray() {

        char[] ret = new char[this.length()];
        System.arraycopy(this.value, 0, ret, 0, this.length());
        return ret;
    }
    
    
    public int indexOf(FString str) {
        return indexOf(this.value, 0, this.length(), str.value, 0, str.length(), 0);
    }
    
    /**
     * @param source 源字符串
     * @param sourceOff
     * @param sourceLen
     * @param target 目标字符串
     * @param targetOff
     * @param targetLen
     * @param start 从source字符串的start处开始搜索target字符串
     * @return
     */
    private int indexOf(char[] source, int sourceOff, int sourceLen,
            char[] target, int targetOff, int targetLen, int start) {
        
        // 参数校验 //
        if(sourceOff < 0) { // sourceOff、sourceLen等参数不能是负数
            throw new IllegalArgumentException("参数sourceOff不能是负数");
        }
        if(sourceLen < 0) {
            throw new IllegalArgumentException("参数sourceLen不能是负数");
        }
        if(targetOff < 0) {
            throw new IllegalArgumentException("参数targetOff不能是负数");
        }
        if(targetLen < 0) {
            throw new IllegalArgumentException("参数targetLen不能是负数");
        }
        if(start < 0) {
            throw new IllegalArgumentException("参数start不能是负数");
        }
        if(start < sourceOff || start>= sourceLen) { //start的范围是[sourceOff,sourceLen);
            throw new IllegalArgumentException("start的范围不在[sourceOff,sourceLen)");
        }
        
        // 特殊情况处理 //
        if(targetLen == 0) {
            return sourceLen;
        }
        
        // 检索 //
        char firstChar = target[targetOff];
        int max = sourceOff + sourceLen - targetLen; //每次检索最多比较max次
        for(int i = sourceOff + start; i<=max ;i++) {
            
            while(i<=max && source[i] != firstChar) { //检索firstChar的位置
                i++;
            }
            if(i<=max) {
                int j = i+1; // for source
                int k = targetOff+1; // for target
                while(j<sourceLen && k<targetLen && source[j] == target[k]) {
                    j++;
                    k++;
                }
                if(k==targetLen) { //target在source中
                    return i;
                }
            }
        }
        return -1;
    }
    
    public boolean regionMatches(int off, FString other, int otherOff,
            int otherLen) {
        
        if(off < 0) {
            throw new IllegalArgumentException("参数off不能是负数");
        }
        if(otherOff < 0) {
            throw new IllegalArgumentException("参数otherOff不能是负数");
        }
        if(otherLen < 0) {
            throw new IllegalArgumentException("参数len不能是负数");
        }
        if(otherLen > other.length() - otherOff) {
            throw new IllegalArgumentException(String.format("如果other字符串从%d开始，剩余的元素不足%d",otherOff,otherLen));
        }
        char[] v1 = this.value;
        char[] v2 = other.value;
        for(int i=0;i<otherLen;i++) {
            if(v1[off++] != v2[otherOff++]) {
                return false;
            }
        }
        return true;
    }
}
