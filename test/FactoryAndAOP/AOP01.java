package FactoryAndAOP;

import frame.proxy.annotation.AfterReturn;
import frame.proxy.annotation.Before;

public interface AOP01 {
	@Before(methodClassName = { "aopTest" })
	@AfterReturn(methodClassName = { "aopTest" })
	public void print();

	@Before(methodClassName = { "aopTest" })
	@AfterReturn(methodClassName = { "aopTest" })
	public void print(int arg, int arg2);

	@Before(methodClassName = { "aopTest" })
	@AfterReturn(methodClassName = { "aopTest" })
	public int add(int arg, int arg2);

}