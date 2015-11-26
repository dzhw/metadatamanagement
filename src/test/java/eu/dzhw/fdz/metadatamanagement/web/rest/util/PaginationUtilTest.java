/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.rest.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;

import eu.dzhw.fdz.metadatamanagement.domain.User;

/**
 * @author Daniel Katzberg
 *  No integration test. No need for application context.
 */
public class PaginationUtilTest {

  @Test
  public void testGeneratePaginationHttpHeaders() throws URISyntaxException {
    // Arrange
    PaginationUtil paginationUtil = new PaginationUtil();

    HttpHeaders httpHeaders = PaginationUtil.generatePaginationHttpHeaders(new Page<User>() {

      @Override
      public int getNumber() {
        return 1;
      }

      @Override
      public int getSize() {
        return 10;
      }

      @Override
      public int getNumberOfElements() {
        return 0;
      }

      @Override
      public List<User> getContent() {
        return null;
      }

      @Override
      public boolean hasContent() {
        return false;
      }

      @Override
      public Sort getSort() {
        return null;
      }

      @Override
      public boolean isFirst() {
        return false;
      }

      @Override
      public boolean isLast() {
        return false;
      }

      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public boolean hasPrevious() {
        return false;
      }

      @Override
      public Pageable nextPageable() {
        return null;
      }

      @Override
      public Pageable previousPageable() {
        return null;
      }

      @Override
      public Iterator<User> iterator() {
        return null;
      }

      @Override
      public int getTotalPages() {
        return 10;
      }

      @Override
      public long getTotalElements() {
        return 100;
      }

      @Override
      public <S> Page<S> map(Converter<? super User, ? extends S> converter) {
        return null;
      }
    }, "localhost");

    // Act
    String link = httpHeaders.get(HttpHeaders.LINK)
      .get(0);

    // Assert
    assertThat(paginationUtil, not(nullValue()));
    assertThat(httpHeaders, not(nullValue()));
    assertThat(link.contains("<localhost?page=2&size=10>"), is(true)); // first link
    assertThat(link.contains("<localhost?page=0&size=10>"), is(true)); // next
    assertThat(link.contains("<localhost?page=9&size=10>"), is(true)); // prev
    assertThat(link.contains("<localhost?page=0&size=10>"), is(true)); // last
  }

}
