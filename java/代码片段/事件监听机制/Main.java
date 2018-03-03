package cn.edu.jxau.lang;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		Button button = new Button();
		button.addEventListener(new ClickListener() {
			@Override
			public void action(ClickEvent event) {
				System.out.println("click event 1:" + event.getInfo());
			}
		});
		button.addEventListener(new ClickListener() {
			@Override
			public void action(ClickEvent event) {
				System.out.println("click event 2:" + event.getInfo());
			}
		});
		button.addEventListener(new DoubleClickListener() {
			@Override
			public void action(DoubleClickEvent event) {
				System.out.println("double click event 1:" + event.getInfo());
			}
		});
		button.addEventListener(new DoubleClickListener() {
			@Override
			public void action(DoubleClickEvent event) {
				System.out.println("double click event 2:" + event.getInfo());
			}
		});
		button.addEventListener(new ClickListener() {
			@Override
			public void action(ClickEvent event) {
				System.out.println("click event 3:" + event.getInfo());
			}
		});
		button.click();
//		button.doubleclick();
//		button.any();
	}
}