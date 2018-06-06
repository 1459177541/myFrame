package test;

import factory.Factory;
import frame.config.FactoryConfig;
import factory.BeanFactory;

public class Test {
	public static void main(String[] args) {
		Factory f = new BeanFactory(()->FactoryConfig.config.put("Test", Test.class));
//		int s = f.get(int.class);
		Test t =(Test) f.get("Test");
		t.print();
	}
	public void print() {
		System.out.println("hllo world");
	}
}
