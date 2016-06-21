package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.builders.ElasticsearchUpdateQueueItemBuilder;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;

public class ElasticsearchUpdateQueueServiceTest extends AbstractTest {
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository itemRepository;

  @Before
  public void prepare() {
    // remove side effects from other tests
    itemRepository.deleteAll();
  }
  
  @After
  public void cleanUp() {
    itemRepository.deleteAll();
  }

  @Test
  public void testEnqueuingOfSingleDocument() {
    elasticsearchUpdateQueueService.enqueue("testId", ElasticsearchType.variables,
        ElasticsearchUpdateQueueAction.DELETE);

    assertThat(itemRepository.count(), is(1L));

    elasticsearchUpdateQueueService.processQueue();

    assertThat(itemRepository.count(), is(0L));
  }

  @Test
  public void testEnqueuingOfMultipleDocuments() {
    List<String> documentIds = Arrays.asList("id1", "id2", "id3");

    elasticsearchUpdateQueueService.enqueue(documentIds, ElasticsearchType.variables,
        ElasticsearchUpdateQueueAction.DELETE);

    assertThat(itemRepository.count(), is(3L));

    elasticsearchUpdateQueueService.processQueue();

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
        new ElasticsearchUpdateQueueItemBuilder().withAction(ElasticsearchUpdateQueueAction.DELETE)
          .withDocumentId("testId")
          .withDocumentType(ElasticsearchType.variables)
          .withUpdateStartedAt(LocalDateTime.now()
            .minusMinutes(11))
          .withUpdateStartedBy("test")
          .build();

    itemRepository.insert(outdatedItem);

    elasticsearchUpdateQueueService.processQueue();

    assertThat(itemRepository.count(), is(0L));
  }

}
