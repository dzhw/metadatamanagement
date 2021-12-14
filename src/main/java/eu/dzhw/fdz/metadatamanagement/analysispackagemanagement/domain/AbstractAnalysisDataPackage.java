package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Analysis data which is part of an analysis package. There are three concrete implementations.
 * 
 * @author René Reitmann
 */
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@Type(value = DataPackage.class, name = "dataPackage"),
    @Type(value = ExternalDataPackage.class, name = "externalDataPackage"),
    @Type(value = CustomDataPackage.class, name = "customDataPackage")})
public abstract class AbstractAnalysisDataPackage implements Serializable {

  private static final long serialVersionUID = -2179924716031946800L;
}
