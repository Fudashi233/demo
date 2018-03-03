package cn.edu.jxau.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;

public class EchoServer {

	// ---------------------------
	// client
	// ---------------------------
	@Test
	public void client() throws IOException {

		System.out.println("client:establish");
		PrintWriter writer = null;
		BufferedReader reader = null;
		try (Socket socket = new Socket();) {
			socket.connect(new InetSocketAddress("127.0.0.1", 8080));
			writer = new PrintWriter(socket.getOutputStream(), true);
			writer.print("H");
			LockSupport.parkNanos(1000 * 1000 * 1000);
			writer.print("e");
			LockSupport.parkNanos(1000 * 1000 * 1000);
			writer.print("l");
			LockSupport.parkNanos(1000 * 1000 * 1000);
			writer.print("l");
			LockSupport.parkNanos(1000 * 1000 * 1000);
			writer.print("o");
			LockSupport.parkNanos(1000 * 1000 * 1000);
			writer.println("!");
			LockSupport.parkNanos(1000 * 1000 * 1000);
			System.out.println("client:数据发送完毕");
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

	// ---------------------------
	// server
	// ---------------------------
	@Test
	public void server() throws IOException {

		System.out.println("server:establish");
		Map<Socket, Long> map = new HashMap<>();
		ExecutorService executor = Executors.newCachedThreadPool();
		Selector selector = SelectorProvider.provider().openSelector();
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false); // 非阻塞模式
		serverSocketChannel.socket().bind(new InetSocketAddress(8080));
		System.out.println("*" + serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT));
		for (;;) {
			selector.select(); // 阻塞，直到有通道准备好
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey sk = iterator.next();
				System.out.println(sk + "\t" + sk.isValid() + "\t" + sk.isAcceptable() + "\t" + sk.isReadable() + "\t"
						+ sk.isWritable() + "\t" + sk.isConnectable());
				iterator.remove();
				if (!sk.isValid()) {
					continue;
				}
				if (sk.isAcceptable()) {
					accept(selector, sk);
				} else if (sk.isReadable()) {
					Socket socket = ((SocketChannel) sk.channel()).socket();
					if (!map.containsKey(socket)) {
						map.put(socket, System.currentTimeMillis());
					}
					read(selector, sk, executor);
				} else if (sk.isWritable()) {
					write(sk);
					Socket socket = ((SocketChannel) sk.channel()).socket();
					long spend = System.currentTimeMillis() - map.remove(socket);
					System.out.println("server:spend：" + spend);
				}
			}
		}
	}

	private void accept(Selector selector, SelectionKey selectionKey) throws IOException {

		try {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
			SocketChannel socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ).attach(new EchoClient());
			System.out
					.println("server:accept connect form " + socketChannel.socket().getInetAddress().getHostAddress());
		} catch (IOException e) {
			throw new IOException("服务端异常:accept", e);
		}
	}

	private void read(Selector selector, SelectionKey selectionKey, ExecutorService executor) throws IOException {

		try {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			ByteBuffer byteBuf = ByteBuffer.allocate(512);
			int len = socketChannel.read(byteBuf);
			System.out.println("server read:" + len);
			if (len < 0) {
				socketChannel.close();
				return;
			}
			byteBuf.flip();
			executor.execute(new Task(selector, selectionKey, byteBuf));
		} catch (IOException e) {
			throw new IOException("服务端异常:read", e);
		}
	}

	private void write(SelectionKey selectionKey) throws IOException {

		try {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			EchoClient echoClient = (EchoClient) selectionKey.attachment();
			ByteBuffer byteBuf = echoClient.poll();
			int len = socketChannel.write(byteBuf);
			System.out.println("server write:" + len);
			if (len < 0) {
				socketChannel.close();
				return;
			}
			if (echoClient.isEmpty()) {
				selectionKey.interestOps(SelectionKey.OP_READ);
			}
		} catch (IOException e) {
			throw new IOException("服务端异常:write", e);
		}
	}
}

class EchoClient {

	private Queue<ByteBuffer> queue;

	public EchoClient() {
		queue = new LinkedList<>();
	}

	public void offer(ByteBuffer byteBuf) {
		queue.offer(byteBuf);
	}

	public ByteBuffer poll() {
		return queue.poll();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
}

class Task implements Runnable {

	private Selector selector;
	private SelectionKey selectionKey;
	private ByteBuffer byteBuf;

	public Task(Selector selector, SelectionKey selectionKey, ByteBuffer byteBuf) {
		this.selector = selector;
		this.selectionKey = selectionKey;
		this.byteBuf = byteBuf;
	}

	@Override
	public void run() {

		EchoClient echoClient = (EchoClient) selectionKey.attachment();
		echoClient.offer(byteBuf);
		selectionKey.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
		selector.wakeup();
	}
}