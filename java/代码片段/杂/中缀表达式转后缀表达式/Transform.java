import java.util.Scanner;
import java.util.ArrayList;
public class Transform
{
	public static void addAndSubtract(MyStack<Character> stack,String postfixExp)
	{
		char ch=' ';
			if(stack.peek()!=null)
			    ch=stack.peek();
		while(ch=='*'||ch=='/')
		{
			postfixExp+=stack.pop();
			if(stack.peek()!=null)
			        ch=stack.peek();
		}		
	}
	public static void main(String[] args)
	{
		Scanner input=new Scanner(System.in);
		String infixExp=input.nextLine().trim();
		infixExp+='#';
		String postfixExp="";
		MyStack<Character> stack=new MyStack<Character>();
		for(int i=0;i<infixExp.length();i++)
		{
			char ch=infixExp.charAt(i);
			if(ch>='0'&&ch<='9')
				postfixExp+=ch;
			else if(ch=='(')
				stack.push(ch);
			else if(ch==')')
			{
				if(stack.isEmpty())
				{
					System.out.println("Error--56");
					System.exit(0);
				}
		        else
		        {
			        char temp=' ';
			        if(stack.peek()!=null)
			            temp=stack.peek();
		 	        while(temp!='(')
			        {				
			 	        postfixExp+=stack.pop();
				        if(stack.peek()!=null)
			                temp=stack.peek();;
			        }
			        stack.pop();
				}
	      	}
			else if(ch=='*'||ch=='/')
			{
				stack.push(ch);
			}
			else if(ch=='+'||ch=='-')
			{
				char temp=' ';
			    if(stack.peek()!=null)
			        temp=stack.peek();
		        while(temp=='*'||temp=='/')
		        {
			        postfixExp+=stack.pop();
			        if(stack.peek()!=null)
			            temp=stack.peek();
	        	}		
				stack.push(ch);
			}
		    else if(ch=='#')
			{
				while(!stack.isEmpty())
					postfixExp+=stack.pop();
			}	
            else 
                continue;				
		}
		System.out.println(infixExp+"---"+postfixExp);
	 }
}