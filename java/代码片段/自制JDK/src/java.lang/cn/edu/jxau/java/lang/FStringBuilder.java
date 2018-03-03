package cn.edu.jxau.java.lang;

import java.lang.reflect.Method;
import java.util.Arrays;

public class FStringBuilder implements Appendable, CharSequence {

    public static void main(String[] args) {
        
        String str = "我爱中国";
        System.out.println(str.length());
        System.out.println(str.codePointCount(0,str.length()));
//        Method[] methodArr = FStringBuilder.class.getDeclaredMethods();
//        for(Method method : methodArr) {
//            System.out.println(method.isSynthetic()+"   "+method);
//        }
    }
    
    private char[] value; // builder的值
    private int count; // builder中的字符个数
    private static final int DEF_CAPACITY = 256; // default capacity

    public FStringBuilder() {
        this(DEF_CAPACITY);
    }

    public FStringBuilder(int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException("参数capacity必须是正数");
        }
        value = new char[capacity];
    }

    public FStringBuilder(String str) {
        throw new UnsupportedOperationException();
    }

    public int capacity() {
        return value.length;
    }

    @Override
    public int length() {
        return count;
    }

    /**
     * 内部使用的扩容方法
     * @param miniCapacity
     */
    private void ensureCapacity(int miniCapacity) {

        if (miniCapacity > value.length) { // 内存不足
            expandCapacity(miniCapacity);
        }
    }

    private void expandCapacity(int miniCapacity) {

        // 参数校验 //
        if (miniCapacity <= 0) {
            throw new IllegalArgumentException("参数miniCapacity必须是正数");
        }

        // 扩容 //
        int newCapacity = value.length * 2;
        newCapacity = Math.max(newCapacity, miniCapacity);
        value = Arrays.copyOf(value, newCapacity);
    }

    public void trimSize() {
        value = Arrays.copyOf(value, count);
    }

    public void setLength(int newLength) {

        if (newLength < 0) {
            throw new IllegalArgumentException(String.format("参数newLnegth=%d,它不能是负数", newLength));
        }
        if (count < newLength) {
            Arrays.fill(value, count, newLength, '\0'); // 用'\0'填充count至newLength的空间
        }
        count = newLength;
    }

    @Override
    public char charAt(int index) {

        if (index < 0) {
            throw new IllegalArgumentException(String.format("参数index=%d,它不能是负数", index));
        }
        if (index >= value.length) {
            throw new IllegalArgumentException(String.format("参数index=%d,它大于等于value.length", index));
        }
        return value[index];
    }

    public void getChars(int srcStart, int srcEnd, char[] dst, int dstStart) {

        // 参数校验 //
        if (srcStart < 0) {
            throw new IllegalArgumentException(String.format("参数srcStart=%d，它不能是负数", srcStart));
        }
        if (srcEnd < 0) {
            throw new IllegalArgumentException(String.format("参数srcEnd=%d，它不能是负数", srcEnd));
        }
        if (dstStart < 0) {
            throw new IllegalArgumentException(String.format("参数dstStart=%d，它不能是负数", dstStart));
        }
        if (srcEnd < srcStart) {
            throw new IllegalArgumentException(
                    String.format("参数srcStart=%d，srcEnd=%d，srcStart大于srcEnd", srcStart, srcEnd));
        }

        // 将value中的对应元素拷贝到dst数组中 //
        System.arraycopy(value, srcStart, dst, dstStart, srcEnd - srcStart);
    }

    public void setCharAt(int index, char ch) {

        if (index < 0) {
            throw new IllegalArgumentException(String.format("参数index=%d，它不能是负数", index));
        }
        if (index >= value.length) {
            throw new IllegalArgumentException(String.format("参数index=%d，它大于或者等于value.length", index));
        }
        value[index] = ch;
    }

    public FStringBuilder append(Object obj) {

        if (obj == null) {
            appendNull();
        } else {
            append(String.valueOf(obj.toString()));
        }
        return this;
    }

    public FStringBuilder append(String str) {

        if (str == null) {
            return appendNull();
        }
        int len = str.length();
        ensureCapacity(count + len);
        System.arraycopy(str.toCharArray(), 0, value, count, len);
        count += len;
        return this;
    }

    public FStringBuilder appendNull() {

        ensureCapacity(count + 4);
        append('n');
        append('u');
        append('l');
        append('l');
        return this;
    }

    @Override
    public FStringBuilder append(char ch) {

        ensureCapacity(count + 1);
        value[count++] = ch;
        return this;
    }
    

    @Override
    public CharSequence subSequence(int start, int end) {
        return new String(value, start, end-start);
    }

    @Override
    public Appendable append(CharSequence csq) {
        
        if(csq == null) {
            return appendNull();
        }
        return append(csq.toString());
    }

    @Override
    public FStringBuilder append(CharSequence csq, int start, int end) {
        
        csq = csq.subSequence(start, end);
        append(csq);
        return this;
    }

    @Override
    public String toString() {
        return new String(value, 0, count);
    }
}
