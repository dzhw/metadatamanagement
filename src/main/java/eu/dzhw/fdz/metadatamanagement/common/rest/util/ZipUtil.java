package eu.dzhw.fdz.metadatamanagement.common.rest.util;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

/**
 * This class zip and unzip Zip-Files.
 * 
 * @author Daniel Katzberg
 *
 */
public class ZipUtil {

  private ZipUtil() {}
  
  /**
   * Reads a file from a zip without unpacking.
   * @param pathToFile The path to the file in the zip file.
   * @return The content of the file as a String.
   * @throws IOException Handles IO Exception.
   */
  public static String readFileFromZip(Path pathToFile) throws IOException {
    StringBuffer stringBuffer = new StringBuffer();
    try (Stream<String> streamString = 
        Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8).lines()) {
      streamString.forEach(s -> stringBuffer.append(s + System.getProperty("line.separator")));
    }
    return stringBuffer.toString();
  }
  
  /**
   * Saves or updates a File within the zip file.
   * @param pathToFile The path to the file with will get new content.
   * @param newFileContent The content for the file as a string.
   * @throws IOException Handles IOExceptions.
   */
  public static void writeFileToZip(Path pathToFile, String newFileContent) throws IOException {
    try (Writer writer = Files.newBufferedWriter(
        pathToFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
      writer.write(newFileContent);
      writer.flush();
    }
  }
}
