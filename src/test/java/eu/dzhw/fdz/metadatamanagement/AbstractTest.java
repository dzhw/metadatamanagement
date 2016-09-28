package eu.dzhw.fdz.metadatamanagement;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;

/**
 * This class is a basic class for the most unit tests.
 * 
 * @author Daniel Katzberg
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles(Constants.SPRING_PROFILE_UNITTEST)
@WebAppConfiguration
public abstract class AbstractTest {

}
