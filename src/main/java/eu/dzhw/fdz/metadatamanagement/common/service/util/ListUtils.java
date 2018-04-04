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
}
