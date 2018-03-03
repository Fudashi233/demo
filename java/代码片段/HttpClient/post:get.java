    private static void get(String url, String paramStr) throws IOException {

        // request //
        URL realUrl = new URL(url + "?" + paramStr);
        URLConnection urlConnection = realUrl.openConnection();
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "Keep-Alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        urlConnection.connect();

        // response //
        //Map<String,List<String>> fieldMap = urlConnection.getHeaderFields();
        //for(Map.Entry<String,List<String>> entry : fieldMap.entrySet()) {
        //    System.out.println(entry.getKey());
        //    System.out.println("\t"+entry.getValue());
        //}

        try (InputStream in = urlConnection.getInputStream()) {
            int tmp = 0;
            while ((tmp = in.read()) >= 0) {
                System.out.print((char) tmp);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    private static void post(String url, String paramStr) throws IOException {

        // request //
        URL realUrl = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) realUrl.openConnection();
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "Keep-Alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        urlConnection.setDoOutput(true);
        //urlConnection.setDoInput(true);
        //urlConnection.setRequestMethod("POST");
        try (OutputStream ouputStream = urlConnection.getOutputStream();
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ouputStream);
             PrintWriter printWriter = new PrintWriter(outputStreamWriter)) {
            printWriter.println(paramStr);
        }

        // response //
        Map<String, List<String>> fieldMap = urlConnection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : fieldMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println("\t" + entry.getValue());
        }
    }