package test;

import org.junit.Test;

import util.asynchronized.AsynAbstractResult;
import util.asynchronized.AsynExecutor;

public class AsyncTest {
	@Test
	public void test1() throws Exception{
		AsynExecutor<Integer> e1 = new AsynExecutor<>(new AsynAbstractResult<Integer>() {
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
		AsynExecutor<Double> e2 = new AsynExecutor<>(new AsynAbstractResult<Double>() {
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
		
		AsynAbstractResult<String> e3 = AsynExecutor.start("hello world", a->{
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
		System.err.println("e3 complete: "+AsynExecutor.getResult(e3));
		
	}

}
