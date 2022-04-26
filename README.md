# Constellation Lines

Data for creating lines joining bright stars, used to create stick figures in the sky representing the constellations.

There are no standard constellation stick figures. It's largely a matter of taste.

The <a href='https://github.com/johanley/constellation-lines/tree/master/output'>outputs</a> of this project are several text files with structured data.
Each file expresses the same stick figures, but uses different identifiers for the stars.
The identifiers are taken from these widely used catalogs:
 * HR: Yale Bright Star Catalog
 * HD: Henry Draper catalog 
 * HIP: Hipparcos catalog

The version that uses the HIP identifier is also compatible with <a href='https://github.com/johanley/star-catalog'>my own catalog of bright stars</a> based on the Hipparcos data.

Here's an example (using the constellation of Cancer) showing how the stick figures are represented:

Cnc = [[74739, 74198, 74442, 76756], [69267, 74442]];

For Cancer, the stick figure is implemented as two lines, each represented as a list (or array, if you wish) of integers:

* [74739, 74198, 74442, 76756]
* [69267, 74442]

The integers represent identifiers in an underlying catalog (in this case, the Henry Draper catalog).

This project takes as its core input a data file that has been used in other projects.
That data file has a defect: it uses ad hoc identifiers for the stars, which are not related directly to any catalog.
This project amends that defect.