package test;

import frame.proxy.annotation.AfterReturn;
import frame.proxy.annotation.Before;
import frame.proxy.annotation.Check;

public interface TestImp2 {
	@Before(methodClassName = {"aopTest"})
	@AfterReturn(methodClassName = {"aopTest"})
	@Check(methodClassName = {"aopTest"}, UnCheckMethodClassName = "aopTest")
	public void print(String... arg) ;

}
