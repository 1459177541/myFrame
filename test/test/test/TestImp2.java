package test;

import frame.proxy.annotation.AfterReturn;
import frame.proxy.annotation.Before;

public interface TestImp2 {
	@Before(methodClassName = {"aopTest"})
	@AfterReturn(methodClassName = {"aopTest"})
	public void print(String... arg) ;

}
