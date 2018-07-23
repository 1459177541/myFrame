package FactoryAndAOP;


import proxy.annotation.AfterReturn;
import proxy.annotation.Before;
import proxy.annotation.Check;

public interface AOP02 {
	@Before(methodClassName = { "aopTest" })
	@AfterReturn(methodClassName = { "aopTest" })
	@Check(methodClassName = { "aopTest" }, UnCheckMethodClassName = "aopTest")
    void print(String... arg);

}
