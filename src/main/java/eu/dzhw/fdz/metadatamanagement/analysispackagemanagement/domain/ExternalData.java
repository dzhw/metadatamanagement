package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import org.javers.core.metamodel.annotation.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * External data describes data sets of an {@link AnalysisPackage} which cannot be ordered in our
 * RDC.
 * 
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@ValueObject
public class ExternalData extends AbstractAnalysisData {
  private static final long serialVersionUID = 7675341390552887966L;

  // TODO add max length
  private String url;
}
