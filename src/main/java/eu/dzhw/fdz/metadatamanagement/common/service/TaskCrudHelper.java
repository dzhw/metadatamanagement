package eu.dzhw.fdz.metadatamanagement.common.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Component which implements CRUD functions for all {@link Task}s.
 *
 * @author René Reitmann
 */
@Component
public class TaskCrudHelper extends GenericDomainObjectCrudHelper<Task, TaskRepository> {
  public TaskCrudHelper(TaskRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, null, null, null);
  }
}
