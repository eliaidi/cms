package com.wk.cms.publish.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PublishAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(PublishAspect.class);
	
	@Pointcut("execution(  * com.wk.cms.publish.server.PublishServer.publish(..) ) ")
	private void t(){}
	
	@Around("t()")
	public void record(ProceedingJoinPoint jp){
		
		long st = System.currentTimeMillis();
		Throwable ex = null;
		Object result = null;
		doBefore(jp);
		
		try {
			result = jp.proceed();
		} catch (Throwable e) {
			ex = e;
		}finally{
			long et = System.currentTimeMillis();
			doAfter(jp,et-st,result,ex);
		}
	}

	private void doAfter(ProceedingJoinPoint jp, long l, Object result, Throwable ex) {
		// TODO Auto-generated method stub
		
		LOGGER.debug("对象【"+jp.getArgs()[0]+"】发布完成~~");
	}

	private void doBefore(ProceedingJoinPoint jp) {
		// TODO Auto-generated method stub
		Object[] args = jp.getArgs();
		LOGGER.debug("开始发布，发布对象【"+args[0]+"】，发布类型【"+args[1]+"】");
		LOGGER.debug("当前线程：："+Thread.currentThread());
	}
}
