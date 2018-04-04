package eu.dzhw.fdz.metadatamanagement.common.repository.caching;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The invocation context of the called method, used as key for caching.
 * 
 * @author René Reitmann
 */
public class InvocationContext {

  public static final String TEMPLATE = "%s.%s(%s)";

  private final Class<?> targetClass;
  private final String targetMethod;
  private final Object[] args;

  /**
   * Create a method invocation context.
   * 
   * @param targetClass The class of the object the method is being invoked on.
   * @param targetMethod The method being invoked.
   * @param args The paramter of the method.
   */
  @SuppressFBWarnings(value = "EI_EXPOSE_REP2")
  public InvocationContext(Class<?> targetClass, String targetMethod, Object[] args) {
    this.targetClass = targetClass;
    this.targetMethod = targetMethod;
    this.args = args;
  }

  @Override
  public boolean equals(Object that) {
    return EqualsBuilder.reflectionEquals(this, that);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return String.format(TEMPLATE, targetClass.getName(), targetMethod, Arrays.toString(args));
  }
}
