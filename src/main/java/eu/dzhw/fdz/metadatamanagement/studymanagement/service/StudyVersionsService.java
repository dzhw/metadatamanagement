package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for retrieving an initializing the study history.
 * 
 * @author René Reitmann
 */
@Service
@Slf4j
public class StudyVersionsService {
  private Javers javers;

  private StudyRepository studyRepository;

  @Autowired
  public StudyVersionsService(Javers javers, StudyRepository studyRepository) {
    this.javers = javers;
    this.studyRepository = studyRepository;
  }

  /**
   * Init Javers with all current studies if there are no study commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForStudies() {
    List<CdoSnapshot> snapshots =
        javers.findSnapshots(QueryBuilder.byClass(Study.class).limit(1).build());
    // only init if there are no studies yet
    if (snapshots.isEmpty()) {
      log.debug("Going to init javers with all current studies");
      studyRepository.streamAllBy().forEach(study -> {
        javers.commit(study.getLastModifiedBy(), study);
      });
    }
  }

  /**
   * Get the previous 10 versions of the study.
   * 
   * @param studyId The id of the study
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous study versions or null if no study found
   
   */
  public List<Study> findPreviousStudyVersions(String studyId, int limit, int skip) {
    Study study = studyRepository.findOne(studyId);
    
    if (study == null) {
      return null;
    }
    
    QueryBuilder jqlQuery = QueryBuilder.byInstance(study)
        .withScopeDeepPlus(100)
        .limit(limit).skip(skip);

    List<Shadow<Study>> previousVersions = javers.findShadows(jqlQuery.build());

    return previousVersions.stream().map(shadow -> shadow.get()).collect(Collectors.toList());
  }
}
