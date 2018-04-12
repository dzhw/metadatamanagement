package eu.dzhw.fdz.metadatamanagement.common.repository.caching;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;

import lombok.extern.slf4j.Slf4j;


/**
 * Aspect activating caching per request.
 *
 * @author René Reitmann
 */
@Aspect
@Slf4j
public class CacheAspect {

  private RequestScopeCache requestScopeCache;

  @Pointcut("within(eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository+) && "
      + "execution(* findById(..))")
  public void repositoryFindByIdPointcut() {}
  
  @Pointcut("execution(* eu.dzhw.fdz.metadatamanagement.*.repository"
      + ".CacheableRepositoryMethods.*(..))")
  public void cacheableInterfacesPointcut() {}

  public CacheAspect(RequestScopeCache requestScopeCache) {
    this.requestScopeCache = requestScopeCache;
  }

  /**
   * Memoize current call.
   * 
   * @param pjp joint point
   * @return result
   * @throws Throwable call failure
   */
  private Object cache(ProceedingJoinPoint pjp) throws Throwable {
    if (RequestContextHolder.getRequestAttributes() == null) {
      // we are not running in a REST request therefore do not cache
      return pjp.proceed();
    }
    InvocationContext invocationContext = new InvocationContext(
        pjp.getSignature().getDeclaringType(), pjp.getSignature().getName(), pjp.getArgs());
    Object result = requestScopeCache.get(invocationContext);
    if (RequestScopeCache.NONE == result) {
      result = pjp.proceed();
      log.debug("Caching result {}, for method invocation: {}", result, invocationContext);
      requestScopeCache.put(invocationContext, result);
    } else {
      log.debug("Using cached result: {}, for method invocation: {}", result, invocationContext);
    }
    return result;
  }
  
  @Around("repositoryFindByIdPointcut()")
  public Object aroundFindOneMethods(ProceedingJoinPoint pjp) throws Throwable {
    return this.cache(pjp);
  }
  
  @Around("cacheableInterfacesPointcut()")
  public Object aroundCacheableInterfacesMethods(ProceedingJoinPoint pjp) throws Throwable {
    return this.cache(pjp);
  }
}
