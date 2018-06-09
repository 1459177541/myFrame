package test;

import frame.proxy.ProxyImp;
import frame.proxy.annotation.AfterReturn;
import frame.proxy.annotation.Before;

public interface TestImp extends ProxyImp {
	@Before(methodName = {"aopTest"})
	@AfterReturn(methodName = {"aopTest"})
	public void print() ;
}
