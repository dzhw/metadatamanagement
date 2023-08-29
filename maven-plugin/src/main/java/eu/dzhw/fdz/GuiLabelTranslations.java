package eu.dzhw.fdz;

/**
 * Result object for parsing all model
 * domain specific translation files.
 * @since Aug 2023
 * @since Aug 2023
 * @author <a href="mailto:tvillwock@codematix.de">Tilo Villwock</a>
 */
public class GuiLabelTranslations {

    /** The model domain the label translations are associated with. */
    private final ModelDomain modelDomain;
    /** The German label translations. */
    private String germanLabels;
    /** The English label translations. */
    private String englishLabels;

    public GuiLabelTranslations(ModelDomain modelDomain) {
        this.modelDomain = modelDomain;
    }

    public ModelDomain getModelDomain() {
        return modelDomain;
    }

    public String getGermanLabels() {
        return germanLabels;
    }

    public String getEnglishLabels() {
        return englishLabels;
    }

    public void setGermanLabels(String germanLabels) {
        this.germanLabels = germanLabels;
    }

    public void setEnglishLabels(String englishLabels) {
        this.englishLabels = englishLabels;
    }
}
