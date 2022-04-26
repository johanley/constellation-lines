package starlines.input;

/** Simple struct to carry selected data from the OS Bright Star Catalog. */
public final class OSBrightStarCatalogRecord {

  /** Identifier from the Hipparcos catalog. */
  public Integer HIP;
  
  /** Identifier from the Bright Star Catalog (Harvard Revised). */
  public Integer HR;
  
  /** Identifier from the Henry Draper catalog. */
  public Integer HD;
  
  /** Visual magnitude, in Johnson V-band. */
  public Double MAG;
}
