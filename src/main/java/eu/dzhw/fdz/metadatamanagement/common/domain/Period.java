package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.io.Serializable;
import java.time.LocalDate;

import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidPeriod;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Class representing period in time.
 * 
 * @author René Reitmann
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.common.domain.builders")
@ValidPeriod
public class Period implements Serializable {
  private static final long serialVersionUID = -4040296722435163942L;

  private LocalDate start;
   
  private LocalDate end;

  public LocalDate getStart() {
    return start;
  }

  public void setStart(LocalDate start) {
    this.start = start;
  }

  public LocalDate getEnd() {
    return end;
  }

  public void setEnd(LocalDate end) {
    this.end = end;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((end == null) ? 0 : end.hashCode());
    result = prime * result + ((start == null) ? 0 : start.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;      
    }
    if (obj == null) {      
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;      
    }
    Period other = (Period) obj;
    if (end == null) {
      if (other.end != null) {
        return false;        
      }
    } else if (!end.equals(other.end)) {
      return false;      
    }
    if (start == null) {
      if (other.start != null) {
        return false;        
      }
    } else if (!start.equals(other.start)) {      
      return false;
    }
    return true;
  }
}
