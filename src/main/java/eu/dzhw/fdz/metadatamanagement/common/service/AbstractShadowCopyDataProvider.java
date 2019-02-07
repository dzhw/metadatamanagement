package eu.dzhw.fdz.metadatamanagement.common.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Template for functionality shared between all {@link ShadowCopyDataProvider}.
 * @param <T> AbstractShadowableRdcDomainObject type the implementing {@link ShadowCopyDataProvider}
 *           will handle.
 */
public abstract class AbstractShadowCopyDataProvider<T extends AbstractShadowableRdcDomainObject>
    implements ShadowCopyDataProvider {

  private final Class handledClass;

  public AbstractShadowCopyDataProvider(Class<T> handledClass) {
    this.handledClass = handledClass;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final void saveShadowCopies(
      List<? extends AbstractShadowableRdcDomainObject> shadowCopies) {

    List copies = shadowCopies.stream().map(copy -> {
      if (handledClass.isInstance(copy)) {
        return handledClass.cast(copy);
      } else {
        throw createExceptionForUnsupportedType(copy);
      }
    })
        .collect(Collectors.toList());

    internalSave(copies);
  }

  @Override
  @SuppressWarnings("unchecked")
  public final AbstractShadowableRdcDomainObject createShadowCopy(
      AbstractShadowableRdcDomainObject source, String version) {

    if (handledClass.isInstance(source)) {
      AbstractShadowableRdcDomainObject copy = internalCopy((T) source, version);
      copy.setShadow(true);
      return copy;
    } else {
      throw createExceptionForUnsupportedType(source);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public final Stream<AbstractShadowableRdcDomainObject> getMasters(
      String dataAcquisitionProjectId) {

    return (Stream) internalGetMasters(dataAcquisitionProjectId);
  }

  @Override
  @SuppressWarnings("unchecked")
  public final Stream<AbstractShadowableRdcDomainObject> getLastShadowCopies(
      String dataAcquisitionProjectId) {

    return (Stream) internalGetLastShadowCopies(dataAcquisitionProjectId);
  }

  protected abstract void internalSave(List<T> shadowCopies);

  protected abstract AbstractShadowableRdcDomainObject internalCopy(T source, String version);

  protected abstract Stream<T> internalGetMasters(String dataAcquisitionProjectId);

  protected abstract Stream<T> internalGetLastShadowCopies(String dataAcquisitionProjectId);

  private IllegalArgumentException createExceptionForUnsupportedType(
      AbstractShadowableRdcDomainObject object) {
    return new IllegalArgumentException(this.getClass().getSimpleName()
        + " cannot handle objects of type " + object.getClass());
  }
}
