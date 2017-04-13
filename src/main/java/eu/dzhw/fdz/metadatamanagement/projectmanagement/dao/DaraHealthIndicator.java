package eu.dzhw.fdz.metadatamanagement.projectmanagement.dao;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;

/**
 * Health Indicator for Dara.
 * 
 * @author Daniel Katzberg
 *
 */
public class DaraHealthIndicator extends AbstractHealthIndicator {
  
  private DaraDao daraDao;
  
  /**
   * The health check is external and needs a dara rest resource for checking health. 
   * 
   * @param daraDao The rest resource for calling the rest api of dara.
   */
  public DaraHealthIndicator(DaraDao daraDao) {
    this.daraDao = daraDao;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.boot.actuate.health
   * .AbstractHealthIndicator#doHealthCheck(org.springframework.boot.actuate.health.Health.Builder)
   */
  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    
    //check dara health
    if (daraDao.isDaraHealth()) {
      builder.up();
    } else {
      builder.down();
    }
  }

}
