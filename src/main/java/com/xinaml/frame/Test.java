package com.xinaml.frame;


import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    final static String ip = "58.67.193.173";
    final static Integer host = 8888;
    final static String str = "##0462ST=32;CN=2061;PW=123456;MN=1111;CP=&&DataTime=20180416000000;001-Cou=0.00,001-Min=7.16,001-Avg=7.17,001-Max=7.17;B01-Cou=748.84,B01-Min=204.50,B01-Avg=208.01,B01-Max=214.20;011-Cou=17.37,011-Min=23.20,011-Avg=23.20,011-Max=23.20;B11-Min=21941081.00,B11-Avg=21941455.50,B11-Max=21941830.00;060-Cou=2.40,060-Min=3.11,060-Avg=3.20,060-Max=3.31;101-Cou=0.05,101-Min=0.0620,101-Avg=0.0620,101-Max=0.0620;065-Cou=3.00,065-Min=4.01,065-Avg=4.01,065-Max=4.01&&90C1";

    public static void main(String[] args) throws Exception {

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        writeData();
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static void writeData() throws Exception {
        if (isRunning(ip, host)) {
            Socket socket = new Socket(ip, host);
            socket.setSoTimeout(1000);
            try {
                if (isServerClose(socket)==false) {
                    //2、获取输出流，向服务器端发送信息
                    OutputStream os = socket.getOutputStream();//字节输出流
                    PrintWriter pw = new PrintWriter(os);//将输出流包装成打印流
                    pw.write("hello：yours；psw：123");
                    pw.flush();
                    socket.shutdownOutput();
                    //3、获取输入流，并读取服务器端的响应信息
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String info = null;
                    while ((info = br.readLine()) != null) {
                        System.out.println("server return data：" + info);
                    }
                    //4、关闭资源
                    br.close();
                    is.close();
                    pw.close();
                    os.close();
                    socket.close();
                }
            } catch (Exception e) {
                System.out.println("shut down now...");
//                System.exit(-1);
            }

        } else {
            System.out.println("ip or port is invalid!");
            System.exit(-1);
        }
    }

    public static Boolean isServerClose(Socket socket) {
        try {
            socket.sendUrgentData(0xFF);
            return false;
        } catch (Exception se) {
            return true;
        }
    }

    public static boolean isRunning(String host, int port) {
        Socket sClient = null;
        try {
            SocketAddress saAdd = new InetSocketAddress(host.trim(), port);
            sClient = new Socket();
            sClient.connect(saAdd, 1000);
        } catch (UnknownHostException e) {
            return false;
        } catch (SocketTimeoutException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (sClient != null) {
                    sClient.close();
                }
            } catch (Exception e) {
            }
        }
        return true;
    }
}
