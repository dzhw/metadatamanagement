package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

public record CodeBook(String name, String address) {
  public CodeBook(String name, String address) {
    this.name = name;
    this.address = address;
    }
}
