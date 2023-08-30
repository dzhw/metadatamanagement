package eu.dzhw.fdz;

import java.util.Optional;

/**
 * The model domain types and their filter expressions.
 * @since Aug 2023
 * @author <a href="mailto:tvillwock@codematix.de">Tilo Villwock</a>
 */
public enum ModelDomain {
    DataPackage(".+datapackagemanagement.+"),
    AnalysisPackage(".+analysispackagemanagement.+"),
    Survey(".+surveymanagement.+"),
    Concept(".+conceptmanagement.+"),
    Instrument(".+instrumentmanagement.+"),
    Question(".+questionmanagement.+"),
    DataSet(".+datasetmanagement.+"),
    Variable(".+variablemanagement.+"),
    RelatedPublication(".+relatedpublicationmanagement.+");

    public final String expression;
    ModelDomain(String expression) {
        this.expression = expression;
    }

    /**
     * A convenience method to return the model
     * domain based on the translation file's path.
     * @param filepath the translation file's path
     * @return the wrapped model domain if successful
     * or an empty Optional otherwise.
     */
    static Optional<ModelDomain> from(String filepath) {
        for (ModelDomain domain : ModelDomain.values()) {
            if (filepath.matches(domain.expression)) {
                return Optional.of(domain);
            }
        }
        return Optional.empty();
    }
}
