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

Here is an example showing how the stick figures are represented for the constellation of Cancer:

Cnc = [74739, 74198, 74442, 76756];[69267, 74442]

For Cancer the stick figure is implemented using two series of line segments, each represented as a list (or array) of integers:

* [74739, 74198, 74442, 76756] - joins four stars (using three line segments)
* [69267, 74442] - joins two stars

The integers are identifiers in an underlying catalog (in this case, the Henry Draper catalog).
Of course, in order to actually plot the stick figures, you will need to calculate the positions of the corresponding stars, according to your context. 

The magnitude limit of all stars used in all stick figures is stated at the end of the 
<a href='https://github.com/johanley/constellation-lines/blob/master/output/logging-output.utf8'>logging output</a>. 

This project takes as its core input a data file that has been used in other projects.
That data file has a defect: it uses ad hoc identifiers for the stars, which are not related directly to any catalog.
This project amends that defect, and publishes the result in a form that's useful to others.