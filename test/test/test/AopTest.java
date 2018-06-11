package test;

import java.util.Arrays;

import frame.proxy.action.AfterReturnAction;
import frame.proxy.action.BeforeAction;
import frame.proxy.action.CheckAction;

/**
 * 测试AOP，实现AOP方法
 * @author 杨星辰
 *
 */
public class AopTest implements AfterReturnAction, BeforeAction, CheckAction {

	@Override
	public void beforeAction(Object target, Object... args) {
		System.out.print("AopTest beforeAction: target = "+target+"    arg =");
		checkArgs(args);
	}

	@Override
	public void afterReturnAction(Object target, Object... args) {
		System.out.print("AopTest afterReturnAction: target = "+target+"    arg =");
		checkArgs(args);
	}

	@Override
	public boolean checkAction(Object target, Object... args) {
		System.out.print("AopTest checkAction: return = "+(0!=((Object[])args[0]).length)+"    target = "+target+"    arg =");
		checkArgs(args);
		return 0!=((Object[])args[0]).length;
	}
	
	private void checkArgs(Object...args) {
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
