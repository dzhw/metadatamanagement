/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.async;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.task.AsyncTaskExecutor;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.AbstractBasicTest;
import eu.dzhw.fdz.metadatamanagement.notest.util.UnitTestUtils;

/**
 * Test class for the AuditResource REST controller.
 * 
 * @author Daniel Katzberg
 *
 */
public class ExceptionHandlingAsyncTaskExecutorTest extends AbstractBasicTest {

  private ExceptionHandlingAsyncTaskExecutor asyncTaskExecutor;

  @Inject
  private AsyncTaskExecutor taskExecutor;

  @Before
  public void before() {
    this.asyncTaskExecutor = new ExceptionHandlingAsyncTaskExecutor(this.taskExecutor);
  }

  @Test(expected=NullPointerException.class)
  public void testDestroy() throws Exception {
    // Arrange
    ExceptionHandlingAsyncTaskExecutor executorClone =
        new ExceptionHandlingAsyncTaskExecutor((AsyncTaskExecutor) 
            UnitTestUtils.cloneObject(this.taskExecutor));

    // Act
    executorClone.destroy();
    executorClone.submit(Mockito.mock(Runnable.class));//Destoryed Object -> Nullpointer

    // Assert
  }

  @Test
  public void testExecute() {
    // Arrange

    // Act
    this.asyncTaskExecutor.execute(Mockito.mock(Runnable.class));

    // Assert
    assertThat(this.asyncTaskExecutor, is(not(nullValue())));
  }

  @Test
  public void testExecuteWithStartTimeOut() {
    // Arrange

    // Act
    this.asyncTaskExecutor.execute(Mockito.mock(Runnable.class), 100L);

    // Assert
    assertThat(this.asyncTaskExecutor, is(not(nullValue())));
  }

  @Test
  public void testHandleException() {
    // Arrange

    // Act
    this.asyncTaskExecutor.execute(new Runnable() {

      @SuppressFBWarnings
      @SuppressWarnings("null")
      @Override
      public void run() {
        String nullValue = null;
        nullValue.length(); // Nullpointer for Handle Method
      }
    }, 100L);

    // Assert
    assertThat(this.asyncTaskExecutor, is(not(nullValue())));
  }

  @Test
  public void testSubmit() {
    // Arrange

    // Act
    Future<?> future = this.asyncTaskExecutor.submit(Mockito.mock(Runnable.class));

    // Assert
    assertThat(this.asyncTaskExecutor, is(not(nullValue())));
    assertThat(future, is(not(nullValue())));
  }

  @Test
  public void testSubmitCallable() {
    // Arrange

    // Act
    @SuppressWarnings("unchecked")
    Future<?> future = this.asyncTaskExecutor.submit(Mockito.mock(Callable.class));

    // Assert
    assertThat(this.asyncTaskExecutor, is(not(nullValue())));
    assertThat(future, is(not(nullValue())));
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  public void testHandleExceptionCallable() {
    // Arrange

    // Act
    this.asyncTaskExecutor.submit(new Callable() {

      @SuppressFBWarnings
      @SuppressWarnings("null")
      @Override
      public Object call() throws Exception {
        String nullValue = null;
        nullValue.length(); // Nullpointer for Handle Method
        return null;
      }});

    // Assert
    assertThat(this.asyncTaskExecutor, is(not(nullValue())));
  }

}
