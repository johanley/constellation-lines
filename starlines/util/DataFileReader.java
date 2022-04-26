package starlines.util;

import static starlines.util.LogUtil.log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** 
 Helper class for input and output files.
 To use this class, you must set a system property named 'project-root'. 
*/
public final class DataFileReader {

  /** 
   Returns the full name of an input file. 
   You must set a system property named 'project-root'. 
  */
  public static String inputFile(String fileName) {
    return directory("input", fileName);
  }
  
  /** 
   Returns the full name of an output file. 
   You must set a system property named 'project-root'. 
  */
  public static String outputFile(String fileName) {
    return directory("output", fileName);
  }
  
  /**
   Read a text file and return it as a List of (untrimmed) Strings.
   @param fileName the full name of UTF-8 text file. 
  */
  public List<String> readFile(String fileName) {
    log("Reading file:"  + fileName);
    List<String> result = new ArrayList<>();
    try {
      Path path = Paths.get(fileName);
      result = Files.readAllLines(path, Consts.ENCODING);
    }
    catch(IOException ex) {
      log("CANNOT OPEN FILE: " + fileName);
    }
    return result;
  }
  
  private static String directory(String inout, String filename) {
    String rootDir = System.getProperty("project-root");
    if (Util.isBlank(rootDir)) {
      throw new RuntimeException("You must set a System property named 'project-root', and point it to the root directory of this project.");
    }
    String sep = File.separator;
    return rootDir + sep + inout + sep + filename;
  }
}