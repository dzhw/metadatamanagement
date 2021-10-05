package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;

/**
 * Remember the previous version of a analysis package per request in order to update elasticsearch
 * correctly.
 * 
 * @author René Reitmann
 */
@Component
@RequestScope
public class AnalysisPackageChangesProvider extends DomainObjectChangesProvider<AnalysisPackage> {

}
