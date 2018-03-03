import java.util.Scanner;
public class Count
{
	public static boolean check(String st)
	{
		MyStack<Character> stack=new MyStack<Character>();
		for(int i=0;i<st.length();i++)
		{
			char ch=st.charAt(i);
		    if(ch=='('||ch=='[')
				stack.push(ch);
		    else if(ch==')')
			{
				if(stack.isEmpty())
					return false;
				else if(stack.peek()=='[')
					return false;
				else 
					stack.pop();
			}
			else if(ch==']')
			{
				if(stack.isEmpty())
					return false;
				else if(stack.peek()=='(')
					return false;
				else 
					stack.pop();
			}
		}
		if(stack.isEmpty())
			return true;
		else 
			return false;
		
	}
	public static void process(MyStack<Integer> operandStack,MyStack<Character> operatorStack)
	{
		int a=operandStack.pop();
		int b=operandStack.pop();
		int c=0;
		char operator=operatorStack.pop();
		switch(operator)
		{
			case '+':c=b+a;break;
			case '-':c=b-a;break;
			case '*':c=b*a;break;
			case '/':c=b/a;break;
			default:{
			            System.out.println("Error---17");
                        System.exit(0);						
           			}
		}
		operandStack.push(c);
	}
	public static void addAndSubtract(MyStack<Integer> operandStack,MyStack<Character> operatorStack)
	{
		int ch=' ';
		if(!operatorStack.isEmpty())
			ch=operatorStack.peek();
		while((ch=='+'||ch=='-'||ch=='*'||ch=='/')&&!operatorStack.isEmpty())/////////
		{
			process(operandStack,operatorStack);
	        if(operatorStack.peek()==null)
				break;
			ch=operatorStack.peek();
		}
	}
	public static void multiplyAndDivide(MyStack<Integer> operandStack,MyStack<Character> operatorStack)
	{
		int ch=' ';
		if(!operatorStack.isEmpty())
			ch=operatorStack.peek();
        while((ch=='*'||ch=='/')&&!operatorStack.isEmpty())//qu chu empty
		{
			process(operandStack,operatorStack);
	        if(operatorStack.peek()==null)
				break;
			ch=operatorStack.peek();
		}
	}
	public static void rightBracket(MyStack<Integer> operandStack,MyStack<Character> operatorStack)
	{
		int ch=' ';
		if(!operatorStack.isEmpty())
			ch=operatorStack.peek();
		while(ch!='(')
		{
			process(operandStack,operatorStack);
			if(operatorStack.peek()==null)
				break;
			ch=operatorStack.peek();
		}
	}
	public static void rightSquareBracket(MyStack<Integer> operandStack,MyStack<Character> operatorStack)
	{
		int ch=' ';
		if(!operatorStack.isEmpty())
			ch=operatorStack.peek();
		while(ch!='[')
		{
			process(operandStack,operatorStack);
			if(operatorStack.isEmpty())
				break;
			ch=operatorStack.peek();
		}
	}
	public static void main(String[] args)
	{
		/* int result=0;
		Scanner input=new Scanner(System.in);
		MyStack<Integer> operandStack=new MyStack<Integer>();
		MyStack<Character> operatorStack=new MyStack<Character>();
		String st=input.nextLine().trim();
		st=st+'#';
		for(int i=0;i<st.length();i++)
		{
			char ch=st.charAt(i);
			if(ch=='+'||ch=='-')
			{
				addAndSubtract(operandStack,operatorStack);
				operatorStack.push(ch);
			}
			else if(ch=='*'||ch=='/')
			{
				multiplyAndDivide(operandStack,operatorStack);
				operatorStack.push(ch);
			}
			else if(ch=='(')
			{
				operatorStack.push(ch);
			}
			else if(ch==')')
			{
				rightBracket(operandStack,operatorStack);
				operatorStack.pop();
			}
			else if(ch=='[')
			{
				operatorStack.push(ch);
			}
			else if(ch==']')
			{
				rightSquareBracket(operandStack,operatorStack);
				operatorStack.pop();
			}
			else if(ch>='0'&&ch<='9')
			{
				operandStack.push(ch-'0');
			}
			else if(ch=='#')
			{
				while(!operatorStack.isEmpty())
				{
					process(operandStack,operatorStack);
				}
				result=operandStack.peek();
				operandStack.clear();
				operatorStack.clear();
			}
			else
			{
				continue;
			}
		}
		System.out.println(result); */
		Scanner input=new Scanner(System.in);
		String st=input.nextLine();
		System.out.println(check(st));
	}
}