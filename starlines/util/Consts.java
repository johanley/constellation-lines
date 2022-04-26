package starlines.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/** Simple constants class. */
public final class Consts {

  /** The (UTF-8) encoding used to read and write text files. */
  public static final Charset ENCODING = StandardCharsets.UTF_8;

  /** The character that begins a comment, in a data file, {@value}. */
  public static final String COMMENT = "#";

}
