package starlines.tweaks;

import java.util.List;

import starlines.input.OSBrightStarCatalogRecord;
import static starlines.util.LogUtil.*;

/** 
 Add the missing variable star omicron Ceti (Mira). 
 
 In Hipparcos, the Vmag of Mira 6.47, so it doesn't pass the criterion of having 6.0 as the Vmag limit.
 Here, Mira is simply added in a brute force, hard-coded manner. 
 A better solution is better source data, which includes Mira (and similar variable stars) in the first place.
 
 <P>This implementation is such that if Mira is indeed present in the source data (at some future date), then 
 no action is taken.
*/
public final class SupplyMiraManually {
  
  /** Add Mira to the given list of records, but only if it's HR absent. */
  public void addMiraIfAbsentFrom(List<OSBrightStarCatalogRecord> records) {
    boolean hasMira = false;
    for(OSBrightStarCatalogRecord record : records) {
      if (MIRA.equals(record.HR)) {
        hasMira = true;
      }
    }
    if(!hasMira) {
      log("Adding Mira manually, for HR " + MIRA);
      OSBrightStarCatalogRecord mira = new OSBrightStarCatalogRecord();
      mira.HR = MIRA;
      mira.HD = 14386;
      mira.HIP = 10826;
      mira.MAG = 3.4; // from RASC Observer's Handbook 2019; the maximum varies; Yale BSC has it as 3.04
      records.add(mira);
    }
  }
  
  /** The HR identifier of Mira - {@value}. */
  static final Integer MIRA = 681;
}
