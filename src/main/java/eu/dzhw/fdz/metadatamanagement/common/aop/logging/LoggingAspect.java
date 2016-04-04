package eu.dzhw.fdz.metadatamanagement.common.aop.logging;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Aspect
public class LoggingAspect {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Pointcut(
      "within(eu.dzhw.fdz.metadatamanagement.repository..*)"
      + " || within(eu.dzhw.fdz.metadatamanagement.service..*)"
      + " || within(eu.dzhw.fdz.metadatamanagement.web.rest..*)")
  public void loggingPointcut() {}

  /**
   * Log exception occuring in repositories and services.
   * @param joinPoint The joinpoint
   * @param exception the exception thrown
   */
  @AfterThrowing(pointcut = "loggingPointcut()", throwing = "exception")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
    log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature()
        .getDeclaringTypeName(),
        joinPoint.getSignature()
          .getName(),
          exception.getCause(), exception);

  }

  /**
   * Log any method call of repositories and services.
   * 
   * @param joinPoint the aop joinpoint
   * @return the result of the called method
   * @throws Throwable the exception thrown by the method
   */
  @Around("loggingPointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    if (log.isDebugEnabled()) {
      log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature()
          .getDeclaringTypeName(),
          joinPoint.getSignature()
            .getName(),
          Arrays.toString(joinPoint.getArgs()));
    }
    try {
      Object result = joinPoint.proceed();
      if (log.isDebugEnabled()) {
        log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature()
            .getDeclaringTypeName(),
            joinPoint.getSignature()
              .getName(),
            result);
      }
      return result;
    } catch (IllegalArgumentException e) {
      log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
          joinPoint.getSignature()
            .getDeclaringTypeName(),
          joinPoint.getSignature()
            .getName());

      throw e;
    }
  }
}
