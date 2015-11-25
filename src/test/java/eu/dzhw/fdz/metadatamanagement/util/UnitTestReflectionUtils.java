/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author Daniel Katzberg
 * 
 *         (inspired by
 *         <a>http://stackoverflow.com/questions/14268981/modify-a-class-definitions-annotation-
 *         string-parameter-at-runtime</a>
 *
 */
public class UnitTestReflectionUtils {
  
  /**
   * Changes a value of a annotation.
   * 
   * @throws SecurityException
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   */
  @SuppressWarnings("unchecked")
  public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue)
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
      IllegalAccessException {

    // get field
    Object proxyHandler = Proxy.getInvocationHandler(annotation);
    Field field = proxyHandler.getClass()
      .getDeclaredField("memberValues");
    field.setAccessible(true);

    // Get values of the annotatio and check for correct type.
    Map<String, Object> annotationValues = (Map<String, Object>) field.get(proxyHandler);
    Object oldValue = annotationValues.get(key);
    if (oldValue.getClass() != newValue.getClass()) {
      throw new IllegalArgumentException();
    }

    // no exception until now? -> change annation.
    annotationValues.put(key, newValue);
    return oldValue;
  }
}
