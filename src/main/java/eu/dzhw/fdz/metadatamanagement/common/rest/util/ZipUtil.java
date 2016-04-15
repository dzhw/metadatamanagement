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
 * @author dkatzberg
 *
 */
public class ZipUtil {

  private ZipUtil() {}

  /**
   * Yeah.
   * 
   * @return Yeah.
   * @throws IOException Yeah.
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
   * Yeah.
   * 
   * @return Yeah.
   * @throws IOException Yeah.
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
