package starlines.tweaks;

import java.util.LinkedHashMap;
import java.util.Map;
import static starlines.util.LogUtil.*;

/** 
 Correct a few errors in the core constellation-lines.utf8 data.
 See tweaks.utf8 in the input directory for more information. 
*/
public final class CorrectErrors {
  
  /**  Replace an erroneous HR with its corrected value (if any); otherwise just return the given value.  */
  public Integer fix(Integer hr) {
    Integer result = hr;
    Integer correction = CORRECTIONS.get(hr);
    if (correction != null) {
      log("  Correcting error in core data. Changing HR=" + hr + " to HR="+ correction);
      result = correction;
    }
    return result;
  }

  /** See tweaks.utf8.  */
  private static final Map<Integer, Integer> CORRECTIONS = new LinkedHashMap<>();
  static {
    CORRECTIONS.put(2472, 2473);
    CORRECTIONS.put(301, 1302);
    CORRECTIONS.put(545, 546);
    CORRECTIONS.put(2890, 2891);
  }
}
