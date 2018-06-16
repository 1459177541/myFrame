package test;

import java.util.Arrays;

import factory.ConfigFactory;
import factory.FactoryBuilder;
import frame.config.FactoryConfig;

/**
 * 测试类
 * @author 杨星辰
 *
 */
public class Test implements TestImp, TestImp2{
	public static void main(String[] args) {
//		Factory f = new ProxyFactory(new ConfigDefaultFactory(new FactoryConfig() {
//			@Override
//			public void initConfig() {
//				config.put("Test", Test.class);
//				config.put("aopTest", AopTest.class);
//			}
//		}));
		FactoryBuilder fb = new FactoryBuilder(new FactoryConfig() {
			@Override
			public void initConfig() {
				config.put("Test", Test.class);
				config.put("aopTest", AopTest.class);
			}
		});
		ConfigFactory f = fb.get();
		Object t = f.get("Test");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		((TestImp)t).print();
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		((TestImp2)t).print("Hello World");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		((TestImp2)t).print("GOODBYE"," ","WORLD");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		((TestImp2)t).print(new String[]{});
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		((TestImp)t).print(5, 7);
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		System.err.println(((TestImp)t).add(5, 7));
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
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

	@Override
	public int add(int arg, int arg2) {
		System.err.println("Test.add execute");
		return arg+arg2;
	}
}
