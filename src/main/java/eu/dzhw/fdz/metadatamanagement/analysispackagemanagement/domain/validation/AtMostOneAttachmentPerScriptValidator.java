package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.Script;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ScriptAttachmentMetadata;

/**
 * Validate that there is at most one {@link ScriptAttachmentMetadata} per {@link Script}.
 */
public class AtMostOneAttachmentPerScriptValidator
    implements ConstraintValidator<AtMostOneAttachmentPerScript, ScriptAttachmentMetadata> {
  @Autowired
  private GridFsOperations operations;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(AtMostOneAttachmentPerScript constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(ScriptAttachmentMetadata attachmentMetadata,
      ConstraintValidatorContext context) {
    Query query = new Query(GridFsCriteria.whereMetaData("scriptUuid")
        .is(attachmentMetadata.getScriptUuid()).andOperator(GridFsCriteria
            .whereMetaData("analysisPackageId").is(attachmentMetadata.getAnalysisPackageId())));

    return this.operations.findOne(query) == null;
  }

}
