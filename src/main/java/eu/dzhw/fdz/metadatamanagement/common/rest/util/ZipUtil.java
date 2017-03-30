package eu.dzhw.fdz.metadatamanagement.common.rest.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;

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
    return IOUtils.toString(Files.readAllBytes(pathToFile),StandardCharsets.UTF_8.name());
  }
  
  
  /**
   * Saves or updates a File within the zip file.
   * @param pathToFile The path to the file with will get new content.
   * @param newFileContent The content for the file as a string.
   * @throws IOException Handles IOExceptions.
   */
  public static void writeFileToZip(Path pathToFile, String newFileContent) throws IOException {
    Files.write(pathToFile, newFileContent.getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.CREATE);
  }
}
