package eu.dzhw.fdz.metadatamanagement.common.repository.caching;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.rits.cloning.Cloner;

/**
 * Request wide caching of method invocations. Have a look at {@link CacheAspect}.
 * 
 * @author René Reitmann
 */
@Component
@RequestScope
public class RequestScopeCache {
 
  public static final Object NONE = new Object();
  
  @Autowired
  private Cloner cloner;
  
  private final Map<InvocationContext, Object> cache = new HashMap<InvocationContext, Object>();

  public Object get(InvocationContext invocationContext) {
    return cache.containsKey(invocationContext) ? cache.get(invocationContext) : NONE;
  }

  public void put(InvocationContext methodInvocation, Object result) {
    cache.put(methodInvocation, cloner.deepClone(result));
  }
}
