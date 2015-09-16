package eu.dzhw.fdz.metadatamanagement.data.common;

/**
 * This class cast types in a safety way from one type to an another one.
 * 
 * @author Daniel Katzberg 
 */
public class CastTypesUtils {
  
  /**
   * Casts a long value to an integer value. Checks the borders of integer for handling a incorrect
   * casting.
   * 
   * @param longValue a given value from the primitive type long.
   * @return a casted integer value.
   */
  public static int castLongToInt(long longValue) {
    if (longValue < Integer.MIN_VALUE || longValue > Integer.MAX_VALUE) {
      throw new IllegalArgumentException(
          longValue + " long value can't be cast to int. It's outside the int definition.");
    }
    return (int) longValue;
  }

}
