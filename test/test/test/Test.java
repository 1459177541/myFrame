package test;

import factory.Factory;
import factory.ProxyFactory;
import frame.config.FactoryConfig;

public class Test implements TestImp{
	public static void main(String[] args) {
		Factory f = new ProxyFactory(new FactoryConfig() {
			@Override
			public void initConfig() {
				config.put("Test", Test.class);
				config.put("aopTest", AopTest.class);
			}
		});
//		int s = f.get(int.class);
		TestImp t =(TestImp) f.get("Test");
		t.print();
	}

	public void print() {
		System.out.println("hello world");
	}
}
