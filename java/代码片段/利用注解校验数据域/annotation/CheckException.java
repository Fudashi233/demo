package annotation;

public class CheckException extends Exception {
    
    public CheckException() {
        super();
    }
    
    public CheckException(String msg) {
        super(msg);
    }
    
    public CheckException(Throwable throwable) {
        super(throwable);
    }
    
    public CheckException(String msg,Throwable throwable) {
        super(msg,throwable);
    }
}
