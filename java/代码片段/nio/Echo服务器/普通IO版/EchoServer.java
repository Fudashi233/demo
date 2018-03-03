package cn.edu.jxau.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class EchoServer {

	@Test
	public void client() throws IOException {

		System.out.println("client:establish");
		PrintWriter writer = null;
		BufferedReader reader = null;
		try (Socket socket = new Socket();) {
			socket.connect(new InetSocketAddress("127.0.0.1", 8080));
			writer = new PrintWriter(socket.getOutputStream(),true);
			writer.println("Hello!");
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("client:" + reader.readLine());
		} catch (IOException e) {
			throw new IOException("客户端异常", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw new IOException("输入流关闭失败", e);
			} finally {
				writer.close();
			}
		}
		System.out.println("client:close");
	}

	@Test
	public void server() throws IOException {

		System.out.println("server:establish");
		try (ServerSocket serverSocket = new ServerSocket(8080);) {
			ExecutorService executor = Executors.newCachedThreadPool();
			while (true) {
				executor.execute(new Task(serverSocket.accept()));
			}
		} catch (IOException e) {
			System.out.println("server:close");
			throw new IOException("服务端异常", e);
		}
	}
}

class Task implements Runnable {

	private Socket socket;

	public Task(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try (InputStreamReader inReader = new InputStreamReader(socket.getInputStream());
				BufferedReader reader = new BufferedReader(inReader);
				PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

			long start = System.currentTimeMillis();
			String line = null;
			while ((line = reader.readLine()) != null) {
				writer.println(line);
				writer.flush();
			}
			long end = System.currentTimeMillis();
			System.out.println("server:spend:" + (end - start));
		} catch (IOException e) {
			throw new RuntimeException("服务端异常，任务处理失败", e);
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("套接字关闭失败", e);
			}
		}
	}
}