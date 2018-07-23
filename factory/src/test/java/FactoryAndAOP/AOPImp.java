package FactoryAndAOP;


import frame.Bean;

import java.util.stream.Stream;

/**
 * 测试工厂类
 * 
 * @author 杨星辰
 *
 */
@Bean(name = "AOPImp")
public class AOPImp implements AOP01, AOP02 {

	@Override
	public void print() {
		System.err.println("hello world");
	}

	@Override
	public void print(String... arg) {
		Stream.of(arg).forEach(System.err::print);
		System.err.println();
	}

	@Override
	public void print(int arg, int arg2) {
		System.err.println(arg + "+" + arg2 + "=" + (arg + arg2));
	}

	@Override
	public int add(int arg, int arg2) {
		System.err.println("AOPImp.add execute");
		return arg + arg2;
	}
}
