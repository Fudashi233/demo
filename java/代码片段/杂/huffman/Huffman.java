import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;
public class Huffman
{
	static class Tree implements Comparable<Tree>
	{
		public Node root;
		public Tree()
		{
			root=null;
		}
		public Tree(Tree t1,Tree t2)
		{
			root=new Node();
			root.left=t1.root;
			root.right=t2.root;
			root.weight=t1.root.weight+t2.root.weight;
		}
		public Tree(int weight,char element)
		{
			root=new Node(weight,element);
		}
		public int compareTo(Tree t)
		{
			return t.root.weight-root.weight; ////// So wit
		}
	}
	public static String[] getCode(Node root)
	{
		if(root==null)
			return null;
		String[] codes=new String[26];
		assignCode(root,codes);
		return codes;
	}
	public static void assignCode(Node root,String[] codes)
	{
		if(root.left!=null)
		{
			root.left.code=root.code+"0";
			assignCode(root.left,codes);
			
			root.right.code=root.code+"1";
			assignCode(root.right,codes);
		}
		else
			codes[root.element-'A']=root.code;
	} 
	public static Tree getHuffman(int[] counts)
	{
		Heap<Tree> heap=new Heap<Tree>();
		for(int i=0;i<counts.length;i++)
			if(counts[i]>0)
			    heap.add(new Tree(counts[i],(char)('A'+i)));
		while(heap.getSize()>1)
		{
			Tree t1=heap.remove();
			Tree t2=heap.remove();
			heap.add(new Tree(t1,t2));
		}
		return heap.remove();
	}
	public static int[] getFrequency(String text)
	{
		String st=text.toUpperCase();
		int[] counts=new int[26];
		for(int i=0;i<st.length();i++)
		{
			char ch=st.charAt(i);
			if(ch>='A'&&ch<='Z')
				counts[ch-'A']++;
		}
		return counts;
	}
	public static void main(String[] args)
	{
		String text="welcome";
		int[] counts=getFrequency(text);
		Tree huffman=getHuffman(counts);
		String[] codes=getCode(huffman.root);
		for(int i=0;i<counts.length;i++)
		{
			if(counts[i]>0)
		        System.out.println((char)(i+'A')+"---"+counts[i]+"---"+codes[i]);
		}
		JFrame frame=new JFrame("Windows");
		Pane p=new Pane(huffman.root);
		frame.add(p);
		frame.setSize(600,400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
} 
class Node
{
	public int weight;
	public char element;
	public Node left;
	public Node right;
	public String code;
    public Node()
	{
		weight=0;
		element=' ';
		left=null;
		right=null;
		code="";
	}	
	public Node(int w,char e)
	{
		weight=w;
		element=e;
		left=null;
		right=null;
		code="";
	}
}
class Pane extends JPanel
{
	public int radius;
    public int vGap;
    public Node root;	
	public Pane(Node r)
	{
		radius=20;
		vGap=50;
		root=r;
    }
	protected void paintComponent(Graphics g)
	{
        super.paintComponent(g);
	    displayNode(g,root,this.getWidth()/2,30,this.getWidth()/4);
    }
    public void displayNode(Graphics g,Node root,int x,int y,int hGap)
	{
		g.drawOval(x-radius,y-radius,radius*2,radius*2);
		g.drawString(String.valueOf(root.element),x-4,y+6);
		if(root.left!=null)
		{
			connectLeft(g,x,y,x-hGap,y+vGap);
			displayNode(g,root.left,x-hGap,y+vGap,hGap/2);
		}
		if(root.right!=null)
		{
			connectRight(g,x,y,x+hGap,y+vGap);
			displayNode(g,root.right,x+hGap,y+vGap,hGap/2);
		}
	}
    public void connectLeft(Graphics g,int x1,int y1,int x2,int y2)
    {
		double d=Math.sqrt(vGap*vGap+(x1-x2)*(x1-x2));
	    int incrementX=(int)(radius*(x1-x2)/d);
		int incrementY=(int)(radius*vGap/d);
		x1=x1-incrementX;
		y1=y1+incrementY;
		x2=x2+incrementX;
		y2=y2-incrementY;
		g.drawLine(x1,y1,x2,y2);
    }
    public void connectRight(Graphics g,int x1,int y1,int x2,int y2)
    {
  	    double d=Math.sqrt(vGap*vGap+(x2-x1)*(x2-x1));
		int incrementX=(int)(radius*(x2-x1)/d);
		int incrementY=(int)(radius*vGap/d);
		x1=x1+incrementX;
		y1=y1+incrementY;
		x2=x2-incrementX;
		y2=y2-incrementY;
		g.drawLine(x1,y1,x2,y2);
    }
}