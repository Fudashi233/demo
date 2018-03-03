package analyze;

import java.io.IOException;
import java.util.List;

@Note(date="20170426")
@Message(date="20121215",content="hello world")
public abstract class Student<T extends Number,E> extends Person<T,E> implements Comparable<Student>{
    
    
    @FieldNote
    private List<Student> friends;
    private volatile int count;
    private List<Student>[] firendsClass;
    @FieldNote
    private T a;
    private E b;
    
    @ConAnnotation
    public Student() throws NullPointerException,IllegalArgumentException{
        
    }
    
    @ConAnnotation
    public Student(T t,E e) throws IOException,IllegalArgumentException {
        
    }
    
    public Student(int i,long j,Student k) {
        
    }
    
    public List<Student> getFriends() {
        return friends;
    }
    @Override
    public int compareTo(Student o) {
        return 0;
    }
    
    public abstract List foo();
}
