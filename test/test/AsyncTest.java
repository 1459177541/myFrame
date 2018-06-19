package test;

import org.junit.Test;

import util.asynchronized.AsynEvent;
import util.asynchronized.AsynExecutor;

public class AsyncTest {
	@Test
	public void test1() throws Exception{
		AsynExecutor<Integer> e1 = new AsynExecutor<>(new AsynEvent<Integer>() {
			@Override
			protected Integer execute() {
				int cont = 0;
				for (int i = 1; i <= 10000; i++) {
					cont+=i;
					if (i%500==0) {
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
		AsynExecutor<Integer> e2 = new AsynExecutor<>(new AsynEvent<Integer>() {
			@Override
			protected Integer execute() {
				int cont = 1;
				for (int i = 1; i <= 1000; i++) {
					cont*=i;
					if (i%100==0) {
						System.out.println(Thread.currentThread().getName()+": "+cont);
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return cont;
			}
		});
		
		e1.start();
		e2.start();
		
		System.err.println("e1 complete: "+e1.getResult());
		System.err.println("e2 complete: "+e2.getResult());
		
	}

}
