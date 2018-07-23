package FactoryAndAOP;


import proxy.annotation.AfterReturn;
import proxy.annotation.Before;

public interface AOP01 {
	@Before(methodClassName = { "aopTest" })
	@AfterReturn(methodClassName = { "aopTest" })
    void print();

	@Before(methodClassName = { "aopTest" })
	@AfterReturn(methodClassName = { "aopTest" })
    void print(int arg, int arg2);

	@Before(methodClassName = { "aopTest" })
	@AfterReturn(methodClassName = { "aopTest" })
    int add(int arg, int arg2);

}
