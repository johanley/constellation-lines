package starlines.tweaks;

import java.util.Arrays;

import java.util.List;

import static starlines.util.LogUtil.*;

/**
 Translate the ad hoc identifiers used in constellation-lines.utf8 into the 
 HR identifiers used by the Yale Bright Star Catalog (BSC). 
*/
public final class TranslateAdHocToHR {
  
  /**
   Translate an ad hoc id into an HR id in the Yale BSC.
    
   The source data in constellation-lines.utf8 was originally derived from the BSC. 
   Differences exist because the Yale BSC contains non-stellar items which were removed, and this affected id values.
   Those removals are here reversed, in order to recover the original HR id found in the BSC. 
  */
  public Integer translate(Integer adhocId) {
    for (Integer deletedId : DELETED_ITEMS) {
      if (adhocId >= deletedId) {
        ++adhocId;
      }
    }
    return adhocId + 1; //because the ad hoc id's are from 0-based List objects (so aren't 1-based like the catalogs).
  }
  
  /** 
   The 14 HRs that were simply discarded from the underlying BSC, since they are non-stellar objects, in increasing order.
   
   <P>In order to recover the original HR, these HRs need to be inserted back in, so to speak.
   So, adding '92' back in means that all ad hoc id's greater than or equal to 92 need to be increased by 1, for example.
  */
  private static final List<Integer> DELETED_ITEMS = Arrays.asList(92,95,182,1057,1841,2472,2496,3515,3671,6309,6515,7189,7539,8296);
  
  /** Informal test harness. */
  private static void main(String[] args) {
    test(90, 91, 92, 93, 94, 95);
  }
  
  private static void test(Integer... ids) {
    TranslateAdHocToHR tr = new TranslateAdHocToHR();
    for (Integer id : ids) {
      Integer result = tr.translate(id);
      log("In:" + id + " out:"+ result);
    }
  }
}
