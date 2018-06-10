package test;

import java.util.Arrays;

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
		System.out.println();
		t.print(new String[] {"Hello World"});
		System.out.println();
		t.print(new String[] {"GOODBYE"," ","WORLD"});
		System.out.println();
		t.print(5, 7);
	}
	
	@Override
	public void print() {
		System.err.println("hello world");
	}
	
	@Override
	public void print(String... arg) {
		Arrays.asList(arg).forEach(System.err::print);
		System.err.println();
	}

	@Override
	public void print(int arg, int arg2) {
		System.err.println(arg+"+"+arg2+"="+(arg+arg2));
	}
}
