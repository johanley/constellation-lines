package starlines.tweaks;

import java.util.LinkedHashMap;
import java.util.Map;

import starlines.input.OSBrightStarCatalogRecord;

/**
 Change several references found in Hipparcos data.
 
 In several cases involving close double stars, the Hipparcos data doesn't reference the desired HR identifier  
 used by the core data for constellation lines.
 
 <P>The fix applied here is to manually overwrite a few records in the Hipparcos-based data, to use references that are 
 compatible with the HR-based data.  
 
 <P>See tweaks.utf8.
*/
public final class PointHipparcosToAlternateHRandHD {

  public boolean hasCloseDoubleProblemWithReferences(Integer hip) {
    return corrections.keySet().contains(hip);
  }
  
  /** Changes the state of the given record object. */
  public void repointToCompanionStar(OSBrightStarCatalogRecord record) {
    if (hasCloseDoubleProblemWithReferences(record.HIP)) {
      AlternateIds correction = corrections.get(record.HIP);
      record.HR = correction.HR;
      record.HD = correction.HD;
    }
  }
  
  private static final class AlternateIds {
    AlternateIds(Integer hr, Integer hd){
      this.HR = hr;
      this.HD = hd;
    }
    Integer HR;
    Integer HD;
  }
  
  private static final Map<Integer /*HIP id*/, AlternateIds> corrections = new LinkedHashMap<>();
  static {
    corrections.put(18255, new AlternateIds(1212, 24555));
    corrections.put(76669, new AlternateIds(5834, 139892));
    corrections.put(72105, new AlternateIds(5506, 129989));
  }
}
