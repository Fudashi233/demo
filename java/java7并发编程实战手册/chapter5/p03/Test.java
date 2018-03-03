package chapter5.p03;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Test {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		Document document = new Document(100,10,'A');
		DocumentTask task = new DocumentTask(document,'A');
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		forkJoinPool.execute(task);
		forkJoinPool.shutdown();
		System.out.println(task.get());
	}
}

class DocumentTask extends RecursiveTask<Integer> {

	private int first;
	private int last;
	private Document document;
	private char ch;//target char
	private static final int THRESHOLD = 5;
	public DocumentTask(Document document,char ch) {
		
		this.document = document;
		this.ch = ch;
		first = 0;
		last = document.getSize()-1;
	}
	
	private DocumentTask(Document document,char ch,int first,int last) {
		
		this.document = document;
		this.ch = ch;
		this.first = first;
		this.last = last;
	}
	
	@Override
	protected Integer compute() {
		
		Integer result = null;
		if((last-first+1) <= THRESHOLD) {
			result = count();
		} else {
			int mid = (first+last)/2;
			ForkJoinTask<Integer> f1 = new DocumentTask(document,ch,first,mid); 
			ForkJoinTask<Integer> f2 = new DocumentTask(document,ch,mid+1,last);
			invokeAll(f1,f2);
			try {
				result = f1.get()+f2.get();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}
	
	/**
	 * 计算document中，从first到last中字符ch的个数
	 */
	private int count() {
		
		int count = 0;
		for(int i=first;i<=last;i++) {
			count+=count(document.get(i));
		}
		return count;
	}
	
	/**
	 * 计算charArr中，字符ch的个数
	 */
	private int count(char[] charArr) {
		
		int count = 0;
		for(int i=0;i<charArr.length;i++) {
			if(charArr[i]==ch) {
				count++;
			}
		}
		return count;
	}
}

class Document {
	
	private static final char[] INFO = {'A','B','C','D','E','F','G','H'}; 
	private char[][] matrix;
	private int count;
	public Document(int size,int length,char ch) {
		
		matrix = new char[size][length];
		for(int i=0;i<size;i++) {
			for(int j=0;j<length;j++) {
				matrix[i][j] = INFO[(int)(Math.random()*INFO.length)];
				if(matrix[i][j]==ch) {
					count++;
				}
			}
		}
		System.out.printf("the document has %d %c\n",count,ch);
	}
	
	public char[][] getMatrix() {
		return matrix;
	}
	
	public char[] get(int i) {
		return matrix[i];
	}
	
	public int getSize() {
		return matrix.length;
	}
}