package eu.dzhw.fdz.metadatamanagement.aop.logging;

import eu.dzhw.fdz.metadatamanagement.config.Constants;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Aspect
public class LoggingAspect {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Inject
  private Environment env;

  @Pointcut("within(eu.dzhw.fdz.metadatamanagement.repository..*) || "
      + "within(eu.dzhw.fdz.metadatamanagement.service..*) || "
      + "within(eu.dzhw.fdz.metadatamanagement.web.rest..*)")
  public void loggingPointcut() {}

  /**
   * This method will be called after a throwable object is thrown. It will be log the exception.
   * 
   * @param joinPoint a joinpoint
   * @param error a throwable object
   */
  @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
    if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
      log.error("Exception in {}.{}() with cause = {}",
          joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
          error.getCause(), error);
    } else {
      log.error("Exception in {}.{}() with cause = {}",
          joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
          error.getCause());
    }
  }

  /**
   * This method logs aroud a given join point.
   * @param joinPoint a given join point
   * @return the thrown error
   * @throws Throwable thrown any kind of thrownable exceptions.
   */
  @Around("loggingPointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    if (log.isDebugEnabled()) {
      log.debug("Enter: {}.{}() with argument[s] = {}",
          joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
          Arrays.toString(joinPoint.getArgs()));
    }
    try {
      Object result = joinPoint.proceed();
      if (log.isDebugEnabled()) {
        log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(), result);
      }
      return result;
    } catch (IllegalArgumentException e) {
      log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
          joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

      throw e;
    }
  }
}
