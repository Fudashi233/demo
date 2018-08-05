private static void jettyStart() {

    int port = 8081;
    Server server = new Server(port);
    WebAppContext webAppContext = new WebAppContext("webapp", "/");
    webAppContext.setDescriptor("webapp/WEB-INF/web.xml");
    webAppContext.setResourceBase("src/main/webapp");
    webAppContext.setDisplayName("Jetty");
    webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());
    webAppContext.setConfigurationDiscovered(true);
    webAppContext.setParentLoaderPriority(true);
    try {
        server.setHandler(webAppContext);
        server.start();
        System.out.println("server is  start, port is " + port + "............");
        server.join();
    } catch (Exception e) {
        e.printStackTrace();
    }
}