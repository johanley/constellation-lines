package starlines;

import static starlines.util.Consts.ENCODING;
import static starlines.util.LogUtil.log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import starlines.input.ConstellationLines;
import starlines.input.OSBrightStarCatalogRecord;
import starlines.input.ParseOSBrightStarCatalog;
import starlines.tweaks.CorrectErrors;
import starlines.tweaks.TranslateAdHocToHR;
import starlines.util.DataFileReader;
import starlines.util.Util;

/** Top-level class that generates all outputs. */
public final class MakeAll {

  /** 
   Run the script that generates all output files for constellation lines.
   
   You must set a system property named 'project-root', which points to the base directory of this project on your system.
   That tells the code where to find its inputs, and where to place its outputs. 
  */
  public static void main(String... args) throws IOException {
    log("Making files for constellation lines, with well-known identifiers.");
    MakeAll script = new MakeAll();
    script.readCatalogWithSynonyms();
    
    script.outputWithHR();
    script.outputWithHIP();
    script.outputWithHD();
    
    script.checkMagnitudeLimit();
    log("Done.");
  }
  
  /** 
   The source file uses an ad hoc identifier that doesn't reference a well-know catalog.
   The ad hoc identifier can be easily translated into an HR identifier. 
   This is here referred to as 'normalizing' the data.
   In addition, some corrections to the data are also applied.
  */
  Map<String, List<List<Integer>>> readInputDataForConstellationLinesAndNormalizeToHR() {
    log("Reading input file that has ad hoc star identifiers.");
    ConstellationLines lines = new ConstellationLines();
    lines.readData();
    Map<String, List<List<Integer>>> result = lines.all();
    
    log("Normalize the ad hoc identifiers to HR identifiers.");
    TranslateAdHocToHR adhocToHR = new TranslateAdHocToHR();
    changeIdentifier(id -> adhocToHR.translate(id), result, "hr");
    
    log("Applying some corrections to the core data.");
    CorrectErrors errors = new CorrectErrors();
    changeIdentifier(id -> errors.fix(id), result, "hr");
    return result;
  }
 
  /** 
   Read in a catalog that includes a number of synonymous identifiers for its stars.
   I use it to translate from HR into all the other target identifiers.
   
   Reference: https://github.com/johanley/star-catalog/tree/master/catalogs/output/open-source-bsc
   Note that in the above data-set, the HIP, HD, and HR identifiers are unique, in the sense of not being 
   repeated for different records. That is, identifier mapping will always be one-to-one with this data set. 
  */
  void readCatalogWithSynonyms() {
    log("Read catalog that contains synonymous identifiers.");
    ParseOSBrightStarCatalog parseCatalog = new ParseOSBrightStarCatalog();
    synonymousIdentifiers = parseCatalog.allRecords();
  }

  /** 
   Define the constellation lines using the star identifiers used by the Yale Bright Star catalog (BSC).
 
   The abbreviation HR comes from Harvard Revised Photometry, an old
   photometric system upon which the BSC is based.  
  */
  void outputWithHR() throws IOException {
    outputForId("hr", hr -> hr); //no change to the id at all
  }
  
  /** Define the constellation lines using the star identifiers from the Hipparcos catalog. */
  void outputWithHIP() throws IOException {
    outputForId("hip", hr -> hipId(hr));
  }
  
  /** Define the constellation lines using the star identifiers from the Henry Draper catalog. */
  void outputWithHD() throws IOException {
    outputForId("hd", hr -> hdId(hr));
  }
  
  //PRIVATE
  
  /** Translates HR identifiers into other identifiers. */
  private Map<Integer/*HR*/, OSBrightStarCatalogRecord> synonymousIdentifiers;

  private void outputToFile(String fileName, Map<String , List<List<Integer>>> data) throws IOException {
    String outputFile = DataFileReader.outputFile(fileName);
    List<String> outputLines = new ArrayList<>();
    for(String constellation : data.keySet()) {
      List<List<Integer>> polyline = data.get(constellation);
      outputLines.add(constellation + " = " + polyline + ";");
    }
    writeOutputFile(outputFile, outputLines);
  }
  
  private void writeOutputFile(String fileName, List<String> lines) throws IOException {
    Path path = Paths.get(fileName);
    try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
      for(String line : lines){
        writer.write(line);
        writer.newLine();
      }
    }
    log("File written (UTF-8): " + fileName);
  }
  
  private void checkForBlanks(Map<String , List<List<Integer>>> data) {
    log("Checking for blank identifiers.");
    int count = 0;
    for (String constellation : data.keySet()) {
      List<List<Integer>> polyline = data.get(constellation);
      for(List<Integer> line : polyline) {
        for(Integer id : line) {
          if (id == null || Util.isBlank(id.toString())) {
            ++count;
          }
        }
      }
    }
    log("Number missing the target id: " + count);
  }
  
  /** Alters the input data structure, to reference different identifiers. */
  private void changeIdentifier(UnaryOperator<Integer> translator, Map<String , List<List<Integer>>> data, String identName) {
    log("Use identifier: " + identName.toUpperCase());
    for(String constellation : data.keySet()) {
      List<List<Integer>> polyline = data.get(constellation);
      for (List<Integer> line : polyline) {
        line.replaceAll(translator);
      }
    }
  }
  
  private void outputForId(String identName, UnaryOperator<Integer> translator) throws IOException {
    log("---   Output file with " + identName.toUpperCase() + " identifiers.   ---");
    //I read this file repeatedly to eliminate cross-talk trouble that I don't understand
    Map<String, List<List<Integer>>> data = readInputDataForConstellationLinesAndNormalizeToHR();
    changeIdentifier(translator, data, identName);
    checkForBlanks(data);
    outputToFile("constellation-lines-" + identName + ".utf8", data);
    log("Number of constellations with lines: " + data.keySet().size());
  }

  /** Translate HR to HIP. */
  private Integer hipId(Integer hr) {
    Integer result = null;
    OSBrightStarCatalogRecord record = synonymousIdentifiers.get(hr);
    if (record == null) {
      log("  HR " + hr + " not found in OS Bright Star Catalog.");
    }
    else {
      result = record.HIP;
    }
    return result;
  }
  
  /** Translate HR to HD. */
  private Integer hdId(Integer hr) {
    Integer result = null;
    OSBrightStarCatalogRecord record = synonymousIdentifiers.get(hr);
    if (record == null) {
      //this just repeats the logging done with HIP; there's no real benefit
      log("  HR " + hr + " not found in OS Bright Star Catalog.");
    }
    else {
      result = record.HD;
    }
    return result;
  }
  
  /** Log the magnitude of the faintest star used in the constellation lines. */
  private void checkMagnitudeLimit() {
    Double magLimit = 0.0;
    Map<String, List<List<Integer>>> data = readInputDataForConstellationLinesAndNormalizeToHR();
    for(String constellation : data.keySet()) {
      List<List<Integer>> polyline = data.get(constellation);
      for (List<Integer> line : polyline) {
        for(Integer hr : line) {
          OSBrightStarCatalogRecord record = synonymousIdentifiers.get(hr);
          if (record != null) {
            Double mag = synonymousIdentifiers.get(hr).MAG;
            if (mag > magLimit) {
              magLimit = mag;
            }
          }
        }
      }
    }
    log("Magnitude limit (Hipparcos Vmag) of all stars in all polylines: " + magLimit);
  }
}
