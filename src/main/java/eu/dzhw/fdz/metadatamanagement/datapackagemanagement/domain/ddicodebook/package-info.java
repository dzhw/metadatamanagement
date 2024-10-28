/**
 * Configuration for JAXB.
 */

@XmlSchema(
  xmlns = {
    @XmlNs(prefix = "", namespaceURI = "ddi:codebook:2_5"),
    @XmlNs(prefix = "xs", namespaceURI = "http://www.w3.org/2001/XMLSchema")
  },
  namespace = "ddi:codebook:2_5",
  elementFormDefault = XmlNsForm.QUALIFIED)

package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
