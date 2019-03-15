package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.AssigneeGroup;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import org.springframework.data.rest.core.config.Projection;

/**
 * Projection for {@link DataAcquisitionProject} that contains a summary about
 * a single project. Included data:
 * <ul>
 *   <li>Name of the data acquisition project</li>
 *   <li>Release status</li>
 *   <li>Assigned group</li>
 *   <li>Project configuration</li>
 * </ul>
 */
@Projection(name = "dataAcquisitionProjectStateSummary", types = DataAcquisitionProject.class)
public interface DataAcquisitionProjectStateSummary {

  String getId();

  Release getRelease();

  AssigneeGroup getAssigneeGroup();

  Configuration getConfiguration();
}
