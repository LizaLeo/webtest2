package ee.itcollege.webtest.aspect;

import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.ejb.criteria.expression.function.CurrentTimeFunction;
import org.springframework.stereotype.Service;

import ee.itcollege.webtest.entity.Person;

@Service
@Aspect
public class DatabaseLoggingAspect {

	private static final Logger logger = Logger.getLogger(DatabaseLoggingAspect.class);
	private static final Logger slowLogger = Logger.getLogger("slowQueries");

	@Pointcut("@annotation(ee.itcollege.webtest.annotation.SlowQueries)")
	public void slowAnnot() {
	}
	
	@Pointcut("target(ee.itcollege.webtest.dao.PersonDao)")
	public void personDao() {
	}

	@Around("execution(* getAll()) && personDao()")
	public Object uninvitedGuest(ProceedingJoinPoint jp) throws Throwable {
		logger.warn(jp.getTarget());

		List<Person> persons = (List<Person>) jp.proceed();

		Person mati = new Person();
		mati.setName("Mati");
		persons.add(mati);

		return persons;
	}
	
	@Around("slowAnnot()")
	public Object slow(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("SLOOOOOOOOOOOOOOOOW" + jp.getTarget());
		return jp.proceed();
	}

	@Around("execution(public * ee.itcollege.webtest.dao.*.*(..)) || slowAnnot()")
	public Object logTime(ProceedingJoinPoint jp) throws Throwable {

		logger.debug("Running around aspect on " + jp.getSignature());

		long time = System.currentTimeMillis();
		System.out.println(jp.getSignature());
		try {
			return jp.proceed();
		} finally {
			time = System.currentTimeMillis() - time;
			if (time > 100) {
				String log = String.format("%s took %dms", jp.getSignature(), time);
				slowLogger.warn(log);
			}
		}
	}

}
