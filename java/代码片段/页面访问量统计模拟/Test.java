package cn.edu.jxau.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;


public class Test {
    
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {

        Page page = new Page();
        Request req1 = new Request("127.0.0.1");
        Request req2 = new Request("127.0.0.2");
        Request req3 = new Request("127.0.0.3");
        Request req4 = new Request("127.0.0.4");
        page.access(req1);
        page.access(req1);
        page.access(req2);
        page.access(req3);
        page.access(req1);
        page.access(req2);
        page.access(req3);
        page.access(req4);
        System.out.println(page);
        Thread.sleep(3000);
        page.access(req1);
        page.access(req1);
        page.access(req2);
        page.access(req3);
        page.access(req1);
        page.access(req2);
        page.access(req3);
        page.access(req4);
        System.out.println(page);
    }
}


