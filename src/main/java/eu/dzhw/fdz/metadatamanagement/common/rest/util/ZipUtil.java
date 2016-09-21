package eu.dzhw.fdz.metadatamanagement.common.rest.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class zip and unzip Zip-Files.
 * 
 * @author Daniel Katzberg
 *
 */
public class ZipUtil {

  private ZipUtil() {}

  /**
   * This method unzips a zip file and return a HashMap representation. The key is the name of the
   * file, including the path within the zip file.
   * 
   * @return Returns a HashMap representation of the zip file.
   * @throws IOException Catches the potential IO Error by the unzipper
   */
  public static Map<String, String> unzip(MultipartFile multiPartFile) throws IOException {

    // Key = Name, Value = Tex File
    Map<String, String> texTemplates = new HashMap<>();

    // unzip the tar.gz file
    try (ZipArchiveInputStream zipArchiveInputStream =
        new ZipArchiveInputStream(multiPartFile.getInputStream())) {

      // Iterate over all files in the archive, until there are no more files
      ZipArchiveEntry currentEntry = zipArchiveInputStream.getNextZipEntry();
      while (currentEntry != null) {

        // ignore directories, read file, save in hashmap
        if (!currentEntry.isDirectory()) {
          String fileContent = IOUtils.toString(zipArchiveInputStream, StandardCharsets.UTF_8);
          texTemplates.put(currentEntry.getName(), fileContent);
        }
        currentEntry = zipArchiveInputStream.getNextZipEntry();
      }
    }

    return texTemplates;
  }

  /**
   * This method zips a zip file and return a ByteArrayOutputStream representation. The basic
   * element for the zip file is a HashMap. The keys for the hashmap are the names with pathes for
   * the files. The value of the hashmap is the file as byte Array.
   * 
   * @return Returns a HashMap representation of the zip file.
   * @throws IOException Catches the potential IO Error by the unzipper
   */
  public static ByteArrayOutputStream zip(Map<String, byte[]> filledTemplates) throws IOException {
    
    // Create zip output stream
    ByteArrayOutputStream byteArrayOutputStreamArchive = new ByteArrayOutputStream();
    try (ZipArchiveOutputStream zipOutputStream =
        new ZipArchiveOutputStream(byteArrayOutputStreamArchive)) {

      // add all files to zip files
      for (Entry<String, byte[]> entry : filledTemplates.entrySet()) {
        ZipArchiveEntry zipAchiveEntry = new ZipArchiveEntry(entry.getKey());
        zipAchiveEntry.setSize(entry.getValue().length);
        zipOutputStream.putArchiveEntry(zipAchiveEntry);
        zipOutputStream.write(entry.getValue());
        zipOutputStream.closeArchiveEntry();
      }
    }
    
    // Return the zip file as byteArray output stream.
    return byteArrayOutputStreamArchive;
  }

}
