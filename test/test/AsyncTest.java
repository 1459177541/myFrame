package test;

import org.junit.Test;

import util.asynchronized.AsynResult;
import util.asynchronized.AsynResultExecutor;
import util.asynchronized.StaticAsyncExecuter;

public class AsyncTest {
	@Test
	public void test1() throws Exception{
		AsynResultExecutor<Integer> e1 = new AsynResultExecutor<>(new AsynResult<Integer>() {
			@Override
			protected Integer execute() {
				int cont = 0;
				for (int i = 1; i <= 1000; i++) {
					cont+=i;
					if (i%100==0) {
						System.out.println(Thread.currentThread().getName()+": "+cont);
					}
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return cont;
			}
		});
		AsynResultExecutor<Double> e2 = new AsynResultExecutor<>(new AsynResult<Double>() {
			@Override
			protected Double execute() {
				double cont = 1;
				for (int i = 1; i <= 100; i++) {
					cont*=i;
					if (i%10==0) {
						System.out.println(Thread.currentThread().getName()+": "+cont);
					}
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return cont;
			}
		});
		
		AsynResult<String> e3 = StaticAsyncExecuter.startResult("hello world", a->{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return a.toUpperCase();
		});
		
		e1.start();
		e2.start();
		System.err.println("wait...");
		System.err.println("e1 complete: "+e1.getResult());
		System.err.println("e2 complete: "+e2.getResult());
		System.err.println("e3 complete: "+StaticAsyncExecuter.getResult(e3));
		
	}

}
