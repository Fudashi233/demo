package cn.edu.jxau.hotDeploy;

public class Test {
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
        IllegalAccessException, InterruptedException {
      
        for(int i=0;i<100;i++) {
            Service service = HotDeployService.getServiceInstance();
            service.service();
            Thread.sleep(1000);
        }
    }
}
