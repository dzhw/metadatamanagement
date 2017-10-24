package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The DataType enumeration.
 */
public class StorageTypes {

  public static final String LOGICAL = "logical";
  public static final String INTEGER = "integer";
  public static final String DOUBLE = "double";
  public static final String COMPLEX = "complex";
  public static final String CHARACTER = "character";
  public static final String RAW = "raw";
  public static final String LIST = "list";
  public static final String NULL = "null";
  public static final String CLOSURE = "closure";
  public static final String SPECIAL = "special";
  public static final String BUILTIN = "builtin";
  public static final String ENVIRONMENT = "environment";
  public static final String S4 = "S4";
  public static final String SYMBOL = "symbol";
  public static final String PROMISE = "promise";
  public static final String PAIRLIST = "pairlist";
  public static final String LANGUAGE = "language";
  public static final String CHAR = "char";
  public static final String DOTDOTDOT = "...";
  public static final String ANY = "any";
  public static final String EXPRESSION = "expression";
  public static final String EXTERNALPTR = "externalptr";
  public static final String BYTECODE = "bytecode";
  public static final String WEAKREF = "weakref";
  public static final Set<String> ALL =
      Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
          LOGICAL, INTEGER, DOUBLE, COMPLEX, CHARACTER, RAW, LIST, NULL, 
          CLOSURE, SPECIAL, BUILTIN, ENVIRONMENT, S4, SYMBOL, PROMISE, PAIRLIST, 
          LANGUAGE, CHAR, DOTDOTDOT, ANY, EXPRESSION, EXTERNALPTR, BYTECODE,
          WEAKREF)));
}
