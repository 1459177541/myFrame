package test;

import factory.Factory;
import factory.BeanFactory;

public class Test {
	public static void main(String[] args) {
		Factory f = new BeanFactory();
//		int s = f.get(int.class);
		Test t = f.get(Test.class);
		t.print();
	}
	public void print() {
		System.out.println("hllo world");
	}
}
