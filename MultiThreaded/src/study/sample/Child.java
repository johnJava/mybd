package study.sample;

public class Child extends Parent {
	public void childStart(){
		super.start();
	}

	public void start() {
		System.out.println("Child---->");
	}

}
