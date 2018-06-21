package test;

import java.util.Arrays;

import factory.ConfigFactory;
import factory.FactoryBuilder;
import frame.config.FactoryConfig;

/**
 * 测试类
 * 
 * @author 杨星辰
 *
 */
public class FactoryTest implements TestImp, TestImp2 {
	public static void main(String[] args) {
		FactoryBuilder fb = new FactoryBuilder(new FactoryConfig() {
			@Override
			public void initConfig() {
				config.put("Test", FactoryTest.class);
				config.put("aopTest", AopTest.class);
			}
		});
		ConfigFactory f = fb.get();
		Object t = f.get("Test");
		await();
		((TestImp) t).print();
		await();
		((TestImp2) t).print("Hello World");
		await();
		((TestImp2) t).print("GOODBYE", " ", "WORLD");
		await();
		((TestImp2) t).print(new String[] {});
		await();
		((TestImp) t).print(5, 7);
		await();
		((TestImp2) t).print(((TestImp) t).add(5, 7)+"");
		await();
	}
	
	private static void await() {
		System.out.println(
				"----------------------------------------------------------------------------------------------------------------------------");
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
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
		System.err.println(arg + "+" + arg2 + "=" + (arg + arg2));
	}

	@Override
	public int add(int arg, int arg2) {
		System.err.println("Test.add execute");
		return arg + arg2;
	}
}
