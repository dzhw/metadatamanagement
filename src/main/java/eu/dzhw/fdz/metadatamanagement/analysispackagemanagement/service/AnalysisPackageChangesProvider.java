package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.Script;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ScriptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;

/**
 * Remember the previous version of a analysis package per request in order to update elasticsearch
 * correctly or in order to delete {@link ScriptAttachmentMetadata}.
 * 
 * @author René Reitmann
 */
@Component
@RequestScope
public class AnalysisPackageChangesProvider extends DomainObjectChangesProvider<AnalysisPackage> {

  /**
   * Return a list of all {@link Script}s which have been removed from the given 
   * {@link AnalysisPackage}.
   * 
   * @param analysisPackageId The id of the {@link AnalysisPackage}
   * @return list of deleted {@link Script}s
   */
  public List<Script> getDeletedScripts(String analysisPackageId) {
    if (oldDomainObjects.get(analysisPackageId) == null
        || oldDomainObjects.get(analysisPackageId).getScripts() == null) {
      return new ArrayList<>();
    }
    if (newDomainObjects.get(analysisPackageId) == null
        || newDomainObjects.get(analysisPackageId).getScripts() == null) {
      return oldDomainObjects.get(analysisPackageId).getScripts();
    }
    List<Script> deletedScripts =
        new ArrayList<>(oldDomainObjects.get(analysisPackageId).getScripts());
    List<Script> newScripts = newDomainObjects.get(analysisPackageId).getScripts();
    // check if deleted script is really deleted else remove it
    for (Script script : newScripts) {
      Script notDeletedScript = deletedScripts.stream()
          .filter(deletedScript -> deletedScript.getUuid().equals(script.getUuid())).findAny()
          .orElse(null);
      if (notDeletedScript != null) {
        deletedScripts.remove(notDeletedScript);
      }
    }
    return deletedScripts;
  }
}
