package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;

public class ElasticsearchUpdateQueueServiceTest extends AbstractTest {
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ElasticsearchUpdateQueueItemRepository itemRepository;
  
  @After
  public void cleanUp() {
    elasticsearchUpdateQueueService.clearQueue();
  }

  @Test
  public void testEnqueuingOfSingleDocument() {
    elasticsearchUpdateQueueService.enqueue("testId", ElasticsearchType.variables,
        ElasticsearchUpdateQueueAction.DELETE);

    assertThat(itemRepository.count(), is(1L));

    elasticsearchUpdateQueueService.processAllQueueItems();

    assertThat(itemRepository.count(), is(0L));
  }

  @Test
  public void testEnqueuingOfDuplicateEntryIsIgnored() {
    elasticsearchUpdateQueueService.enqueue("testId", ElasticsearchType.variables,
        ElasticsearchUpdateQueueAction.DELETE);

    assertThat(itemRepository.count(), is(1L));

    elasticsearchUpdateQueueService.enqueue("testId", ElasticsearchType.variables,
        ElasticsearchUpdateQueueAction.DELETE);

    assertThat(itemRepository.count(), is(1L));
  }

  @Test
  public void testLockedButOutdatedQueueItemIsProcessed() {
    ElasticsearchUpdateQueueItem outdatedItem =
        ElasticsearchUpdateQueueItem.builder().action(ElasticsearchUpdateQueueAction.DELETE)
          .documentId("testId")
          .documentType(ElasticsearchType.variables)
          .updateStartedAt(LocalDateTime.now()
            .minusMinutes(11))
          .updateStartedBy("test")
          .build();

    itemRepository.insert(outdatedItem);

    elasticsearchUpdateQueueService.processAllQueueItems();

    assertThat(itemRepository.count(), is(0L));
  }

}
