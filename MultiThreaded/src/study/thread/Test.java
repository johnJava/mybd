package study.thread;

public class Test {

	public static void main(String[] args) {
		System.out.println("test========");
		Gate gate = new Gate();
		new UserThread(gate, "Alice", "Alaska").start();
		new UserThread(gate, "Bobby", "Brazil").start();
		new UserThread(gate, "Chris", "Canada").start();
		new UserThread(gate, "Dhris", "Danada").start();
		new UserThread(gate, "Ehris", "Eanada").start();
		Object obj = new Object();
		obj.getClass();
	}

}
