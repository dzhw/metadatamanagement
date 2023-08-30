package eu.dzhw.fdz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates a Java class for each model domain's Angular translation files.<br>
 * <br>
 * This goal can be invoked in a standalone fashion by executing:
 * <pre>
 *     mvn mdm:process-frontend-translations
 * </pre>
 * <br>
 * This goal is a port of a Grunt task that used to be part of the build pipeline.
 * @see <a href="https://github.com/dzhw/metadatamanagement/issues/1322">#1322: Add GUI labels to search documents</a>
 * @see <a href="https://github.com/dzhw/metadatamanagement/blob/c44826b943bcf77a79c65ac423c38abde6b91e6c/Gruntfile.js#L678">Grunt Task "createJavaSourceCodeFromTranslations"</a>
 * @since Aug 2023
 * @author <a href="mailto:tvillwock@codematix.de">Tilo Villwock</a>
 */
@Mojo(name = "process-frontend-translations", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ProcessFrontendTranslationsMojo extends AbstractMojo {

    /** The template for the generated Java class file */
    private static final String TEMPLATE = """
        package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;
                
        import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
                
        /**
         * Generated GUI labels from translations-de.js and translations-en.js.
         */
        public class %%MODEL_DOMAIN%%DetailsGuiLabels {
          public static final I18nString GUI_LABELS = new I18nString(
            "%%DE_TRANSLATIONS%%",
            "%%EN_TRANSLATIONS%%"
          );
        }""";

    /** The source directory in which Angular translation files should be searched for */
    @Parameter(property = "mdm.sourceDir", defaultValue = "./mdm-frontend/src/app/legacy/", required = true)
    private String sourceDir;

    /**
     * An optional parameter for supplying a single file to process. It can be
     * used by adding configuration to the project's POM or by adding an
     * argument on the commandline:
     * <pre>
     *     mvn mdm:process-frontend-translations -Dmdm.sourceFile=/path/to/file
     * </pre>
     * Supplying this parameter will override any configuration made for the source directory.
     */
    @Parameter(property = "mdm.sourceFile")
    private String sourceFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // recursively search the source directory for all files matching the translation file pattern
        super.getLog().info("Searching for Angular translation modules...");
        List<Path> files = null;
        try (Stream<Path> fileStream = Files.walk(new File(this.sourceFile != null ? this.sourceFile : this.sourceDir).toPath(), FileVisitOption.FOLLOW_LINKS)) {
            files = fileStream
                .filter(p -> p.getFileName().toString().matches("translations.+\\.js$"))
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new MojoExecutionException(e, "Failed to read files from source directory.", e.getMessage());
        }

        if (files.isEmpty()) {
            throw new MojoFailureException(String.format(
                "No Angular translation files found. Is the source directory '%s' correct?", this.sourceDir));
        }

        // process all translation files by determining the associated model domain,
        // determining its intended language and parsing the JavaScript variable as
        // an JSON object in order to extract all label entries
        Map<ModelDomain, GuiLabelTranslations> domainTranslations = new HashMap<>();
        for (Path file : files) {
            String filepath = file.toString();
            super.getLog().debug(String.format("Processing '%s'", filepath));
            Optional<ModelDomain> modelDomainResult = ModelDomain.from(filepath);
            if (modelDomainResult.isEmpty()) {
                continue;
            }
            ModelDomain modelDomain = modelDomainResult.get(); // e.g. DataPackage, AnalysisPackage, etc.
            String language = getLanguage(filepath); // 'de' or 'en'
            String labels = extractGuiLabels(filepath); // all labels concatenated to a string
            if (!domainTranslations.containsKey(modelDomain)) {
                domainTranslations.put(modelDomain, new GuiLabelTranslations(modelDomain));
            }
            switch (language) {
                case "de" -> domainTranslations.get(modelDomain).setGermanLabels(labels);
                case "en" -> domainTranslations.get(modelDomain).setEnglishLabels(labels);
                default -> throw new MojoExecutionException(String.format("Unexpected language: '%s'", language));
            }
            super.getLog().info(String.format("[%s:%s] %s", modelDomain, language, filepath));
        }

        // create the target directory if necessary
        String dirpath = "./target/generated-sources/translations" +
            "/eu/dzhw/fdz/metadatamanagement/searchmanagement/documents";
        Path dir = new File(dirpath).toPath();
        if (!Files.exists(dir)) {
            try {
                super.getLog().info(String.format("Creating target directory at '%s'", dirpath));
                Files.createDirectories(new File(dirpath).toPath());
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to create target directory: " + dirpath, e);
            }
        }

        // write a Java class file for each model domain to the target
        // directory by replacing the placeholders with the label strings
        // in their respective language
        super.getLog().info(String.format("Writing Java class files to '%s'", dirpath));
        for (GuiLabelTranslations translations : domainTranslations.values()) {
            String content = TEMPLATE
                .replace("%%MODEL_DOMAIN%%", translations.getModelDomain().name())
                .replace("%%DE_TRANSLATIONS%%", translations.getGermanLabels())
                .replace("%%EN_TRANSLATIONS%%", translations.getEnglishLabels());
            String filename = translations.getModelDomain() + "DetailsGuiLabels.java";
            String filepath = String.format("%s/%s", dirpath, filename);
            try {
                Files.writeString(new File(filepath).toPath(), content);
                super.getLog().info(String.format("✔ %s", filename));
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to write file: " + filepath, e);
            }
        }
    }

    /**
     * Collects all label translation values into a single list.
     * @param parent the JSON object node for all label translations
     * @return the list of all label translation values
     * @throws MojoFailureException
     */
    private List<String> collectLabelValues(ObjectNode parent) throws MojoFailureException {
        List<String> values = new ArrayList<>();
        final Iterator<JsonNode> elements = parent.elements();
        while (elements.hasNext()) {
            JsonNode node = elements.next();
            if (node.isObject()) {
                values.addAll(this.collectLabelValues((ObjectNode) node));
            } else if (node.isTextual()) {
                values.add(node.asText().replaceAll("\"", "")); // remove double quotes from translations
            } else {
                throw new MojoFailureException(node, "Unexpected node type encountered",
                    String.format("Node is neither a string nor an object: %s", node.toString()));
            }
        }
        return values;
    }

    /**
     * Convert JavaScript variable declaration into a JSON object.
     * The Angular modules usually look something like this:
     * <pre>
     * 'use strict';
     *
     * angular.module('metadatamanagementApp').config([
     *   '$translateProvider',
     *
     *   function($translateProvider) {
     *     var translations = { // <-- content starts here
     *       //jscs:disable
     *       'analysis-package-management': {
     *         'detail': {
     *           'label': {
     *           ...
     *     }; // <-- content ends here
     * </pre>
     * @param content the Angular module file content
     * @return a wrapped JSON ObjectNode if successful, otherwise an empty Optional
     */
    Optional<JsonNode> convertToJson(String content) {
        String translations = null;
        try {
            int start = content.indexOf("translations");
            int end = content.indexOf("};", start);
            translations = content
                .substring(start, end + 1)
                .replaceFirst("translations\\s*=\\s*", "") // remove Javascript var declaration
                .replaceAll("\"", "\\\\\"") // escape double quotes first
                .replaceAll("'", "\"") // replace single quotes with double quotes for JSON parsing
                .replaceAll("(?m)^\\s*//.+\n", "") // remove single line comments
                .replaceAll(",\\s*}", "}") // remove superfluous commas
                .replaceAll(",\\s*]", "]"); // remove superfluous commas
            return Optional.of(new ObjectMapper().readTree(translations));
        } catch (JsonProcessingException e) {
            super.getLog().error("Unable to parse JSON");
            super.getLog().error(e.getMessage());
            if (translations != null) {
                String[] lines = translations.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    super.getLog().error(String.format("%05d | %s", i + 1, lines[i]));
                }
            }
            return Optional.empty();
        }
    }

    /**
     * Reads the Angular translations module, attempts to parse
     * the JavaScript variable as a JSON object and extracts all
     * label translation values into a single concatenated string.
     * @param filepath the Angular translation module
     * @return a concatenated String of label translation values
     * @throws MojoExecutionException if the Angular module can't
     * be read or parsed or the label translations are missing
     * @throws MojoFailureException if the label JSON node contains
     * anything else than strings and child object nodes
     */
    private String extractGuiLabels(String filepath) throws MojoExecutionException, MojoFailureException {
        String translations = null;
        try {
            String content = Files.readString(new File(filepath).toPath());
            Optional<JsonNode> result = this.convertToJson(content);
            if (result.isEmpty()) {
                throw new MojoExecutionException("Parsing JSON data failed");
            }
            JsonNode node = result.get().iterator().next().path("detail").path("label");
            if (node.isMissingNode()) {
                super.getLog().error(result.get().toPrettyString());
                throw new MojoExecutionException("Unable to find label translations in JSON object");
            }
            return String.join(" ", this.collectLabelValues((ObjectNode) node));
        } catch (IOException e) {
            super.getLog().debug(translations);
            super.getLog().error(e);
            throw new MojoExecutionException(e, "Unable to read contents of file",
                String.format("The following file could not be read in order to extract GUI labels: %s", filepath));
        }
    }

    /**
     * Returns the language identifier based on the file suffix.
     * @param filepath the Angular translations module filepath
     * @return the language identifier
     * @throws MojoFailureException if the file neither ends in
     * 'de.js' nor in 'en.js'
     */
    private String getLanguage(String filepath) throws MojoFailureException {
        if (filepath.endsWith("de.js")) {
            return "de";
        } else if (filepath.endsWith("en.js")) {
            return "en";
        } else {
            throw new MojoFailureException(filepath, "Filename has unexpected suffix", String.format(
                "A translation file is expected to end in 'de.js' or 'en.js'. This one does not: %s", filepath));
        }
    }
}
