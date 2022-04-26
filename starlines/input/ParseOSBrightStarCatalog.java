package starlines.input;

import static starlines.util.LogUtil.log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import starlines.tweaks.PointHipparcosToAlternateHRandHD;
import starlines.tweaks.SupplyMiraManually;
import starlines.util.DataFileReader;
import starlines.util.Util;

/** 
 Parse the OS Bright Star Catalog, and use it to map the HR identifier to other identifiers.
 This catalog is the main output of <a href='https://github.com/johanley/star-catalog'>this project</a>. 
*/
public final class ParseOSBrightStarCatalog {
  
  /**  The key of the returned map is the HR identifier, used to look up all other items embedded in the record.  */
  public Map<Integer/*HR*/, OSBrightStarCatalogRecord> allRecords(){
    if (catalogData.isEmpty()) {
      readCatalogData();
    }
    return catalogData;
  }

  // PRIVATE
  
  private static Map<Integer/*HR*/, OSBrightStarCatalogRecord> catalogData = new LinkedHashMap<>();
  
  private void readCatalogData() {
    String fileName = DataFileReader.inputFile("os-bright-star-catalog-hip.utf8");
    DataFileReader reader = new DataFileReader();
    List<String> lines = reader.readFile(fileName);
    List<OSBrightStarCatalogRecord> records = new ArrayList<>();
    PointHipparcosToAlternateHRandHD repointer = new PointHipparcosToAlternateHRandHD();
    for(String line : lines) {
      OSBrightStarCatalogRecord record = parse(line);
      if (repointer.hasCloseDoubleProblemWithReferences(record.HIP)) {
        repointer.repointToCompanionStar(record);
      }
      records.add(record);
    }
    SupplyMiraManually mira = new SupplyMiraManually();
    mira.addMiraIfAbsentFrom(records);
    
    for(OSBrightStarCatalogRecord record : records) {
      catalogData.put(record.HR, record);
    }
  }
  
  private OSBrightStarCatalogRecord parse(String line) {
    OSBrightStarCatalogRecord result = new OSBrightStarCatalogRecord();
    result.HIP = sliceInt(line, 1, 6);
    result.MAG = sliceDouble(line, 148, 5);
    result.HD = sliceInt(line, 189, 6);
    result.HR = sliceInt(line, 196, 4);
    return result;
  }
  
  private Integer sliceInt(String line, int start /*1-based*/, int numchars) {
    Integer result = null;
    String slice = slice(line, start, numchars);
    if (Util.isPresent(slice)) {
      result = Integer.valueOf(slice);
    }
    else {
      log("  ID missing at [" + start + "]: " + line);
    }
    return result;
  }
  
  private Double sliceDouble(String line, int start /*1-based*/, int numchars) {
    Double result = null;
    String slice = slice(line, start, numchars);
    if (Util.isPresent(slice)) {
      result = Double.valueOf(slice);
    }
    else {
      log("Item missing at position [" + start + "]: " + line);
    }
    return result;
  }
  
  /** The start index is 1-based.  */
  private String slice(String line, int start /*1-based*/, int numchars){
    String result = "";
    try {
      result = line.substring(start-1, start-1+numchars).trim();
    }
    catch(StringIndexOutOfBoundsException ex) {
      //some tables will not pad on the right - just fail silently
    }
    return result;
  }
}