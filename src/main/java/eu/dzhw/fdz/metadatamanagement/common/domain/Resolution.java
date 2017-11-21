package eu.dzhw.fdz.metadatamanagement.common.domain;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**  
 * This class is a representation of the resolution of images.
 * 
 * @author Daniel Katzberg
 *
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Resolution {
  
  @NotNull(message = "common.error.resolution.width-x.not-null")
  private Integer widthX;
  
  @NotNull(message = "common.error.resolution.height-y.not-null")
  private Integer heightY;  
}
