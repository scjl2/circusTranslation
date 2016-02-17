=============================
Note on the Directory Content
=============================

   The files get-m2.xml and library.properties are directly taken from the binary distribution of Ant 1.8.2; I only cut out some bits from library.properties that are not relevant.

   Initially I did not include them in the distribution but it turned out that on my Linux system (Ubuntu 10.04) they were not part of the installation. I am still not totally confident this solution is robust, a better approach might be to either require Maven Ant Tasks to be installed, or otherwise re-implement the functionality of get-m2.xml. I shall also check the Ant licence agreement and let the developers know if I include those files in the first 'official' distribution of the HiJaC tools.
