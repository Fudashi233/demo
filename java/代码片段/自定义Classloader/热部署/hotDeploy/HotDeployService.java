package cn.edu.jxau.hotDeploy;

import java.io.File;

public class HotDeployService {
    
    private static final String CLASS_NAME = "cn.edu.jxau.hotDeploy.ServiceImpl";
    private static final String CLASS_FILE_PATH = "C:\\Users\\PC-Clive\\Desktop\\classes\\ServiceImpl.class";
    private static volatile Service service;
    
    private HotDeployService() {
        throw new UnsupportedOperationException("不可调用构造函数实例化HotDeployService对象");
    }
    
    static {
        monitor();
    }
    
    /**
     * 获取service实例
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    public static Service getServiceInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        if(service != null) {
            return service;
        } else {
            synchronized(HotDeployService.class) {
                if(service == null) {
                    service = newServiceInstance();
                }
                return service;
            }
        }
    }
    
    /**
     * 创建一个service实例
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static Service newServiceInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        MyClassLoader classLoader = new MyClassLoader(CLASS_FILE_PATH);
        Class<?> clazz = classLoader.loadClass(CLASS_NAME);
        return (Service) clazz.newInstance();
    }
    
    /**
     * 实现热部署的关键。每100ms依据文件最后的
     * 修改时间判断是否重新加载字节码文件。
     */
    private static void monitor() {
        
        new Thread() {
            private long lastModified = new File(CLASS_FILE_PATH).lastModified();
            
            @Override
            public void run() {
                try {
                    while(true) {
                        Thread.sleep(100);
                        long newLastModified = new File(CLASS_FILE_PATH).lastModified();
                        if(newLastModified != lastModified) { //字节码文件修改，重写加载字节码文件
                            reloadService();
                            lastModified = newLastModified;
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    private static void reloadService() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println("class file reload");
        service = newServiceInstance();
    }
}














