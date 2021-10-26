/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.mailmanagement.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.Future;

import javax.mail.Message;

import eu.dzhw.fdz.metadatamanagement.authmanagement.domain.AuthUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.icegreen.greenmail.store.FolderException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;

/**
 * @author Daniel Katzberg
 *
 */
public class MailServiceTest extends AbstractTest {

  @Autowired
  private MailService mailService;

  @AfterEach
  public void cleanUp() throws FolderException {
    greenMail.purgeEmailFromAllMailboxes();
  }

  @Test
  public void testSendActivationEmail() throws Exception {
    // Arrange
    User user = User.builder().langKey("de")
      .email("anyMail@localhost.com")
      .build();

    // Act
    Future<Void> futureVoid = this.mailService.sendActivationEmail(new AuthUser(user));
    futureVoid.get();
    Message[] messages = greenMail.getReceivedMessages();

    // Arrange
    assertThat(futureVoid.isDone(), is(true));
    assertThat(messages.length, is(1));
  }

}
