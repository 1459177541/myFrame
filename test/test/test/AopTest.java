package test;

import frame.proxy.action.AfterReturnAction;
import frame.proxy.action.BeforeAction;

public class AopTest implements AfterReturnAction, BeforeAction {

	@Override
	public void beforeAction(Object target, Object... args) {
		System.out.println("AopTest beforeAction...");
	}

	@Override
	public void afterReturnAction(Object target, Object... args) {
		System.out.println("AopTest afterReturnAction...");
	}

}
