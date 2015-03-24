##############################################################
# Common makefile to generate PDF reports from LaTeX sources #
##############################################################

# Possible commands are : "make", "make show" and "make clean".

# Local subdirectories considered for inclusion by pdflatex and bibtex.

CLS = cls
STY = sty
TEX = tex
FIG = fig
PDF = pdf
EPS = eps
PNG = png
BIB = bib

TEXINPUTS := .:$(CLS):$(STY):$(TEX):$(FIG):$(PDF):$(EPS):$(PNG):

BIBINPUTS := .:$(BIB)

export TEXINPUTS BIBINPUTS

PDFOUTPUTS =$(patsubst %.tex,%.pdf,$(wildcard *.tex))

DEPENDS_ON =\
$(wildcard $(CLS)/*)\
$(wildcard $(STY)/*)\
$(wildcard $(TEX)/*)\
$(wildcard $(FIG)/*)\
$(wildcard $(PDF)/*)\
$(wildcard $(EPS)/*)\
$(wildcard $(PNG)/*)\
$(wildcard $(BIB)/*)

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
