package cn.edu.jxau.lang;

public class Waiter implements BeanSelfProxyAware {

	private Waiter proxy;

	public Waiter() {

	}

	public void greetTo(String name) {
		System.out.printf("Waiter.greetTo(%s)\n", name);
		proxy.serveTo(name);
	}

	public void serveTo(String name) {
		System.out.printf("Waiter.serveTo(%s)\n", name);
	}

	public void setSelfProxy(Object proxy) {
		this.proxy = (Waiter) proxy;
	}
}