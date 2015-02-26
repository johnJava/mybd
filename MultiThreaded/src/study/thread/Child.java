package study.thread;

import java.util.Date;

public class Child extends Parent {
	
	public static void main(String[] args) {
		Child c = new Child();
		c.test1();
		//c.test();
	}

	public void test1() {
		super.obj=new Date();
		this .test();
		System.out.println(super.getClass().getSuperclass().getName());
	}


	public void test() {
		System.out.println("Child=================");
	}

}
