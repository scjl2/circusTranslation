##############################################################
# Common makefile to generate PDF reports from LaTeX sources #
##############################################################

# Possible commands are : "make", "make show" and "make clean".

# Local subdirectories considered for inclusion by pdflatex and bibtex.

CLS = cls
STY = sty
SEC = sections
MDL = models
CLS = classes
WRK = work
EXP = export
LAW = laws
PAT = patterns
FIG = fig
TAB = tables
PDF = pdf
EPS = eps
PNG = png
BIB = bib

# Common subdirectories considered for inclusion by pdflatex and bibtex.

STY_COMMON = $(MODEL_HOME)/common/sty
BIB_COMMON = $(MODEL_HOME)/common/bib

# Additional inclusion locations for Java files.

JAVA_FILES = ../java

# Additional inclusion locations for Circus files (local).

CIRCUS_LOCAL = circus

CIRCUS_FILES = ../circus

# Additional inclusion locations for Circus files (framework).

CIRCUS_FRAMEWORK = $(MODEL_HOME)/framework/circus
CIRCUS_LIBRARY   = $(MODEL_HOME)/library/circus
CIRCUS_TOOLKITS  = $(MODEL_HOME)/toolkits

TEXINPUTS := .:$(CLS):$(STY):$(STY_COMMON):$(SEC):$(MDL):$(CLS):$(WRK):$(EXP):$(LAW):$(PAT):$(FIG):$(TAB):$(PDF):$(EPS):$(PNG):$(JAVA_FILES):$(CIRCUS_LOCAL):$(CIRCUS_FILES):$(CIRCUS_FRAMEWORK):$(CIRCUS_TOOLKITS):$(CIRCUS_LIBRARY):

BIBINPUTS := .:$(BIB):$(BIB_COMMON)

export TEXINPUTS BIBINPUTS

PDFOUTPUTS =$(patsubst %.tex,%.pdf,$(wildcard *.tex))

DEPENDS_ON =\
$(wildcard $(CLS)/*)\
$(wildcard $(STY)/*)\
$(wildcard $(SEC)/*)\
$(wildcard $(MDL)/*)\
$(wildcard $(CLS)/*)\
$(wildcard $(WRK)/*)\
$(wildcard $(EXP)/*)\
$(wildcard $(LAW)/*)\
$(wildcard $(PAT)/*)\
$(wildcard $(FIG)/*)\
$(wildcard $(TAB)/*)\
$(wildcard $(PDF)/*)\
$(wildcard $(EPS)/*)\
$(wildcard $(PNG)/*)\
$(wildcard $(BIB)/*)\
$(wildcard $(STY_COMMON)/*)\
$(wildcard $(BIB_COMMON)/*)\
$(wildcard $(JAVA_FILES)/*)\
$(wildcard $(CIRCUS_LOCAL)/*)\
$(wildcard $(CIRCUS_FILES)/*)\
$(wildcard $(CIRCUS_FRAMEWORK)/*)\
$(wildcard $(CIRCUS_TOOLKITS)/*)\
$(wildcard $(CIRCUS_LIBRARY)/*)

TMP_DIR = tmp

all : $(PDFOUTPUTS)

%.pdf : %.tex $(DEPENDS_ON)
	mkdir -p $(TMP_DIR)
	pdflatex -interaction=nonstopmode -output-directory $(TMP_DIR) $<
	-bibtex $(TMP_DIR)/$*
	pdflatex -interaction=nonstopmode -output-directory $(TMP_DIR) $<
	reset
	pdflatex -interaction=nonstopmode -output-directory $(TMP_DIR) $<
	mv $(TMP_DIR)/$@ .

show : $(PDFOUTPUTS)
	evince $(PDFOUTPUTS) &

clean :
	rm -rf $(PDFOUTPUTS) $(TMP_DIR)

.PHONY : all show clean
