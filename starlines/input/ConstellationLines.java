package starlines.input;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import starlines.util.Consts;
import starlines.util.DataFileReader;

/** 
 Read in the core input data for constellation lines.
 This input file uses an ad hoc identifier, which is overwritten by the caller into a well-known identifier.
 The source of this data is <a href='https://github.com/johanley/astro/blob/master/js/ephem.js'>a previous project</a>. 
*/
public final class ConstellationLines {

  /** Read in the data file, and parse it into a data structure. */
  public void readData() {
    parseInputFile();
  }

  /**
   All polylines for all constellations. 
   
   The key is the abbreviation for the constellation name, for example Peg (for Pegasus).
   The value is a 'polyline', a List of a List of Integers. 
   The integers identify the stars that are to be connected together by lines.
  */
  public Map<String/*Ari*/ , List<List<Integer>> /*1..N polylines*/> all(){
    return lines;
  }
  
  // PRIVATE 
  
  private Map<String/*Ari*/ , List<List<Integer>> /*1..N polylines*/> lines = new LinkedHashMap<>();
  
  private void parseInputFile() {
    DataFileReader reader = new DataFileReader();
    String linesFile = DataFileReader.inputFile("constellation-lines.utf8");
    List<String> lines = reader.readFile(linesFile);
    for (String line : lines) {
      if (line.trim().startsWith(Consts.COMMENT)) {
        continue;
      }
      else {
        processLine(line.trim());
      }
    }
  }
  
  /**
   Source data example (came from another project, in javascript-world):
     Ari = [[820,797,613,549,541],[968,883,613],[947,883],[834,797]];
   Each line is a single constellation, represented by a polyline, with 
   the exception of a couple that are really faint, and have no stars to join.
  */
  private void processLine(String line) {
    int equals = line.indexOf("=");
    String constellationAbbr = line.substring(0, equals).trim();
    List<List<Integer>> polylinesIds = new ArrayList<>();
    
    String polylines = line.substring(equals+1).trim(); // [[820,797,613,549,541],[968,883,613],[947,883],[834,797]];
    //chop off the extra square brackets that were needed in javascript-land
    polylines = polylines.substring(1, polylines.length() - 2); // [820,797,613,549,541],[968,883,613],[947,883],[834,797]
    //use regexes to grab each single-line
    String A = Pattern.quote("[");
    String B = Pattern.quote("]");
    String COMMA = Pattern.quote(",");
    Pattern singleLine = Pattern.compile(A + "(.*?)" + B); //1 matching group: 820,797,613,549,541. Reluctant qualifier!
    
    //split the matching group around the comma
    Matcher matcher = singleLine.matcher(polylines);
    while (matcher.find()) {
      //String wholeMatch = matcher.group(0);
      String oneLine = matcher.group(1);
      String[] parts = oneLine.split(COMMA);
      List<Integer> polylineIds = new ArrayList<>();
      for(String part : parts) {
        polylineIds.add(Integer.valueOf(part));
      }
      polylinesIds.add(polylineIds);
    }
    lines.put(constellationAbbr, polylinesIds);
  }
}