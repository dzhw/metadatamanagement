/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

/**
 * This helper class supports reflection for unit tests.
 * 
 * @author Daniel Katzberg
 *
 */
@Slf4j
public class UnitTestReflectionHelper {
  /**
   * This method will return by reflection a declared method. The method has an accessible of true.
   * 
   * @param clazz a given class
   * @param methodName the name of the method of the given class
   * @return the reflection method object.
   */
  public static Method getDeclaredMethodForTestInvocation(Class<?> clazz, String methodName) {

    log.debug("Class: " + clazz.getSimpleName() + " with method " + methodName);

    // Look for declaredMethod
    Method method = null;
    for (Method eachMethod : clazz.getDeclaredMethods()) {

      log.debug("Class: " + clazz.getSimpleName() + " with check method |" + eachMethod.getName()
          + "|=|" + methodName + "|");

      if (eachMethod.getName()
        .equals(methodName)) {
        method = eachMethod;
        break;
      }
    }

    // check for found method
    assertThat(method, not(nullValue()));

    // Set accessible
    method.setAccessible(true);

    return method;
  }

  /**
   * This method will return by reflection a declared field. The field has an accessible of true.
   * 
   * @param clazz a given class
   * @param fieldName the name of the field of the given class
   * @return the reflection field object.
   * @throws SecurityException
   * @throws NoSuchFieldException
   */
  public static Field getDeclaredFieldForTestInvocation(Class<?> clazz, String fieldName)
      throws NoSuchFieldException, SecurityException {
    
    log.debug("Class: " + clazz.getSimpleName() + " with field " + fieldName);
    
    // check for found method
    Field field = clazz.getDeclaredField(fieldName);
    assertThat(field, not(nullValue()));

    // Set accessible
    field.setAccessible(true);

    return field;
  }

}
