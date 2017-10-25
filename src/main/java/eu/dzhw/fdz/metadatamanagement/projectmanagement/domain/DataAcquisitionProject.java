package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation.SetHasBeenReleasedBeforeOnlyOnce;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Data Acquisition Project collects all data which are going to be published by our RDC.
 *
 * @author Daniel Katzberg
 */
@Document(collection = "data_acquisition_projects")
@SetHasBeenReleasedBeforeOnlyOnce(message = "data-acquisition-project."
    + "error.data-acquisition-project."
    + "has-been-released-before.set-has-been-released-before-only-once")
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class DataAcquisitionProject extends AbstractRdcDomainObject {

  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "data-acquisition-project.error.data-acquisition-project.id.not-empty")
  @Pattern(regexp = "^[a-z0-9]*$",
      message = "data-acquisition-project.error.data-acquisition-project.id.pattern")
  @Size(max = StringLengths.SMALL,
      message = "data-acquisition-project.error.data-acquisition-project.id.size")
  private String id;
  
  @NotNull(message = "data-acquisition-project.error.data-acquisition-project."
      + "has-been-released-before.not-null")
  private Boolean hasBeenReleasedBefore;
  
  @Valid
  private Release release;
}
