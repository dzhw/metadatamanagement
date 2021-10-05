package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Software Packages which can be used in {@link Script}s.
 * @author René Reitmann
 */
public class SoftwarePackages {
  public static final String STATA = "Stata";
  public static final String SPSS = "SPSS";
  public static final String R = "R";
  public static final String PYTHON = "Python";
  public static final String LATENT_GOLD = "Latent GOLD";
  public static final String AMOS = "AMOS";
  public static final String MAXQDA = "MAXQDA";
  public static final String MPLUS = "Mplus";
  public static final String MATLAB = "Matlab";
  public static final String JULIA = "Julia";
  public static final String JASP = "JASP";
  public static final String PSPP = "PSPP";
  public static final String ALMO = "Almo";
  public static final String PRISM = "PRISM";
  public static final String SAS = "SAS";
  public static final String STATISTICA = "STATISTICA";
  public static final String MATHEMATICA = "Mathematica";
  public static final String GRETL = "gretl";
  public static final String JAMOVI = "Jamovi";
  public static final String RAPIDMINER = "RapidMiner";
  public static final String GAUSS = "GAUSS";
  public static final String SPARQL = "SPARQL";

  public static final List<String> ALL = Collections.unmodifiableList(Arrays.asList(STATA, SPSS, R,
      PYTHON, LATENT_GOLD, AMOS, MAXQDA, MPLUS, MATLAB, JULIA, JASP, PSPP, ALMO, PRISM, SAS,
      STATISTICA, MATHEMATICA, GRETL, JAMOVI, RAPIDMINER, GAUSS, SPARQL));
}
