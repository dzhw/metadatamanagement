package eu.dzhw.fdz;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProcessFrontendTranslationsMojoTest {

    ProcessFrontendTranslationsMojo mojo;

    @BeforeEach
    void setUp() {
        mojo = new ProcessFrontendTranslationsMojo();
    }

    @Test
    void convertToJson_testSingleLineComments() {
        String content = "var translations = {\n    //jscs:disable \n    'foo': 'bar https://blub.com/bla/bli'\n};";
        Optional<JsonNode> result = this.mojo.convertToJson(content);
        assertTrue(result.isPresent());
        assertEquals("{\"foo\":\"bar https://blub.com/bla/bli\"}", result.get().toString());
    }

    @Test
    void convertToJson_testSuperfluousCommas() {
        String content = "var translations = {\n    'foo': {\n        'fizz': 'buzz',\n    }\n};";
        Optional<JsonNode> result = this.mojo.convertToJson(content);
        assertTrue(result.isPresent());
        assertEquals("{\"foo\":{\"fizz\":\"buzz\"}}", result.get().toString());
    }
}