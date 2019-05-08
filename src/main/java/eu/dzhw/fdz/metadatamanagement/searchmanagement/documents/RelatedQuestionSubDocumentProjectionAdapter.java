package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.RelatedQuestionSubDocumentProjection;

/**
 * Adapter for writing and reading {@link RelatedQuestionSubDocumentProjection} to/from
 * elasticsearch.
 * 
 * @author René Reitmann
 */
public class RelatedQuestionSubDocumentProjectionAdapter
    extends TypeAdapter<RelatedQuestionSubDocumentProjection> {

  @Override
  public void write(JsonWriter out, RelatedQuestionSubDocumentProjection value) throws IOException {
    out.beginObject().name("instrumentId").value(value.getInstrumentId()).name("questionId")
        .value(value.getQuestionId()).endObject();
  }

  @Override
  public RelatedQuestionSubDocumentProjection read(JsonReader in) throws IOException {
    RelatedQuestionSubDocument relatedQuestion = new RelatedQuestionSubDocument();
    in.beginObject();
    String fieldname = null;
    while (in.hasNext()) {
      JsonToken token = in.peek();

      if (token.equals(JsonToken.NAME)) {
        // get the current token
        fieldname = in.nextName();
      }

      if ("instrumentId".equals(fieldname)) {
        // move to next token
        token = in.peek();
        relatedQuestion.setInstrumentId(in.nextString());
      }

      if ("questionId".equals(fieldname)) {
        // move to next token
        token = in.peek();
        relatedQuestion.setQuestionId(in.nextString());
      }
    }
    in.endObject();
    return relatedQuestion;
  }
}
