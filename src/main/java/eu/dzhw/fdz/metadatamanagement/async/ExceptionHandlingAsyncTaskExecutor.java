package eu.dzhw.fdz.metadatamanagement.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * Handles asycn tasks.
 *
 */
public class ExceptionHandlingAsyncTaskExecutor
    implements AsyncTaskExecutor, InitializingBean, DisposableBean {

  private final Logger log = LoggerFactory.getLogger(ExceptionHandlingAsyncTaskExecutor.class);

  private final AsyncTaskExecutor executor;

  public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
    this.executor = executor;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.core.task.TaskExecutor#execute(java.lang.Runnable)
   */
  @Override
  public void execute(Runnable task) {
    executor.execute(task);
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.core.task.AsyncTaskExecutor#execute(java.lang.Runnable, long)
   */
  @Override
  public void execute(Runnable task, long startTimeout) {
    executor.execute(createWrappedRunnable(task), startTimeout);
  }

  private <T> Callable<T> createCallable(final Callable<T> task) {
    return () -> {
      try {
        return task.call();
      } catch (Exception e) {
        handle(e);
        throw e;
      }
    };
  }

  private Runnable createWrappedRunnable(final Runnable task) {
    return () -> {
      try {
        task.run();
      } catch (Exception e) {
        handle(e);
      }
    };
  }

  protected void handle(Exception error) {
    log.error("Caught async exception", error);
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.core.task.AsyncTaskExecutor#submit(java.lang.Runnable)
   */
  @Override
  public Future<?> submit(Runnable task) {
    return executor.submit(createWrappedRunnable(task));
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.core.task.AsyncTaskExecutor#submit(java.util.concurrent.Callable)
   */
  @Override
  public <T> Future<T> submit(Callable<T> task) {
    return executor.submit(createCallable(task));
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.beans.factory.DisposableBean#destroy()
   */
  @Override
  public void destroy() throws Exception {
    if (executor instanceof DisposableBean) {
      DisposableBean bean = (DisposableBean) executor;
      bean.destroy();
    }
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (executor instanceof InitializingBean) {
      InitializingBean bean = (InitializingBean) executor;
      bean.afterPropertiesSet();
    }
  }
}
