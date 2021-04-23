package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain;

/**
 * Describes the time dimension of the data collection. Used by DARA as time dimension and harvested
 * by the VFDB.
 * @see https://mdr.iqb.hu-berlin.de/#/catalog/56cc4164-6731-7d54-c97f-ad9bd24bf1b7
 */
public class TimeMethods {
  public static final String LONGITUDINAL = "Longitudinal";
  public static final String LONGITUDINAL_COHORTEVENTBASED = "Longitudinal.CohortEventBased";
  public static final String LONGITUDINAL_TRENDREPEATEDCROSSSECTION =
      "Longitudinal.TrendRepeatedCrossSection";
  public static final String LONGITUDINAL_PANEL = "Longitudinal.Panel";
  public static final String LONGITUDINAL_PANEL_CONTINOUS = "Longitudinal.Panel.Continuous";
  public static final String LONGITUDINAL_PANEL_INTERVAL = "Longitudinal.Panel.Interval";
  public static final String TIMESERIES = "TimeSeries";
  public static final String TIMESERIES_CONTINOUS = "TimeSeries.Continuous";
  public static final String TIMESERIES_DISCRETE = "TimeSeries.Discrete";
  public static final String CROSSSECTION = "CrossSection";
  public static final String CROSSSECTIONADHOCFOLLOWUP = "CrossSectionAdHocFollowUp";
  public static final String Other = "Other";
}
