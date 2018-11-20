package eu.dzhw.fdz.metadatamanagement.common.service.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Common Utility functions for dealing with lists.
 *
 * @author René Reitmann
 */
public class ListUtils {
  /**
   * Create a new list containing a unified list of the elements
   * of of the two input lists.
   * @param listOne A list
   * @param listTwo Another list
   * @return A unified combined list.
   */
  public static <T> List<T> combineUniquely(List<T> listOne, List<T> listTwo) {
    List<T> result = new ArrayList<>();
    if (listOne != null) {
      result.addAll(listOne);
    }
    if (listTwo != null) {
      result.removeAll(listTwo);
      result.addAll(listTwo);
    }

    return result;
  }

  /**
   * Get the difference between two lists
   * @param baseList base list
   * @param secondList elements which should not be contained in the base list
   * @return A new list containing elements from the first list, but not elements also contained in
   *     the second list.
   */
  public static <T> List<T> diff(List<T> baseList, List<T> secondList) {
    List<T> left = new ArrayList<>(baseList);
    List<T> right = new ArrayList<>(secondList);
    left.removeAll(right);
    return left;
  }
}
