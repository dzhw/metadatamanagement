package eu.dzhw.fdz.metadatamanagement.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * A DTO for the Survey entity.
 */
public class SurveyDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotNull
  @Size(max = 256)
  private String title;

  private LocalDate begin;

  private LocalDate endDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LocalDate getBegin() {
    return begin;
  }

  public void setBegin(LocalDate begin) {
    this.begin = begin;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    SurveyDto surveyDto = (SurveyDto) object;

    if (!Objects.equals(id, surveyDto.id)) {
      return false;
    }  

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "SurveyDTO{" + "id=" + id + ", title='" + title + "'" + ", begin='" + begin + "'"
        + ", endDate='" + endDate + "'" + '}';
  }
}
