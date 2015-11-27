package ee.itcollege.webtest.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.ejb.criteria.expression.function.CurrentTimeFunction;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class DatabaseLoggingAspect {
	
	private static final Logger logger = Logger.getLogger(DatabaseLoggingAspect.class);
	private static final Logger slowLogger = Logger.getLogger("slowQueries");
	
	//@Pointcut("execution(public *(..))")
	//public void pub() {}
	
	@Around("execution(public * ee.itcollege.webtest.dao.*.*(..))")
	public Object doSmth(ProceedingJoinPoint jp) throws Throwable{
		
		logger.debug("Running around aspect on " + jp.getSignature());
		
		long time = System.currentTimeMillis();
		System.out.println(jp.getSignature());
		try {
			return jp.proceed();
		} finally {
			time = System.currentTimeMillis() - time;
			if (time > 100) {
				String log = String.format("%s took %dms",jp.getSignature(), time);
				slowLogger.warn(log);				
			}
		}
	}

}
