TightRope v0.65 Demo
=======

Tool Demo
-----------


The TightRope tool demo is configured to translate the FlatBuffer example application, which is an SCJ solution to the Readers-Writers problem using a one-place buffer and suspension.

To run the TightRope tool demo:
  1. enter the 'TightRope-Demo/tool' directory, open the 'hijac.properties' file and uncomment the relevent line under 'Configuration of the SCJ Application Sources' to select the program you want to translate;
  2. Go back to the 'TightRope-Demo/tool' directory and open a terminal;
  2. Use `ant` to compile the tool (this step may not be needed);
  3. Use `./run TightRope` to run the TightRope demo.

The the resulting model will be written to 'TightRope-Demo/output'. The individual files can be examined, and TightRope produces a PDF report of the model.

### Folders

* The 'output' folder contains the output *Circus* model;
* The 'packages' folder contains the LaTeX style files needed for the report;
* The 'templates' folder contains the Freemarker templates used to output the model;
* The 'tool' folder contains the TightRope tool and the application it is built upon.

### Caveats and Addenda

  1. This tool has only been tested on Linux;
  2. TightRope assumes that it can access pdfltex at: '/usr/bin/pdflatex';
  3. This edition of TightRope v0.65 is *only* configured to translate the FlatBuffer application and the AirCraft application;
  4. TightRope is a prototype tool and work is ongoing to extend its coverage;
  5. This tool is adapted from a preexisting tool for translating SCJ Level 1 programs. We claim no ownership of their work and thank them for kindly providing us with the source code.
