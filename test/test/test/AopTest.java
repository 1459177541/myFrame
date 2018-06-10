package test;

import java.util.Arrays;

import frame.proxy.action.AfterReturnAction;
import frame.proxy.action.BeforeAction;

public class AopTest implements AfterReturnAction, BeforeAction {

	@Override
	public void beforeAction(Object target, Object... args) {
		System.out.print("AopTest beforeAction: target="+target+" arg=");
		if (null!=args) {
			Arrays.asList(args).forEach(es->Arrays.asList(es).forEach(e->{
				if (e instanceof Object[]) {
					System.out.print(" "+Arrays.toString((Object[])e));
				}else {
					System.out.print(" "+e);
				}
			}));
			System.out.println();
		}else {
			System.out.println("null");
		}
	}

	@Override
	public void afterReturnAction(Object target, Object... args) {
		System.out.print("AopTest afterReturnAction: target="+target+" arg=");
		if (null!=args) {
			Arrays.asList(args).forEach(es->Arrays.asList(es).forEach(e->{
				if (e instanceof Object[]) {
					System.out.print(" "+Arrays.toString((Object[])e));
				}else {
					System.out.print(" "+e);
				}
			}));
			System.out.println();
		}else {
			System.out.println("null");
		}
	}

}
