#############################################
# Generic makefile to compile LaTeX sources #
#############################################

# Possible commands are : "make", "make show" and "make clean".

# The following subdirectories are considered for inclusion by pdflatex.

CLS = cls
STY = sty
TEX = tex
FIG = fig
PDF = pdf
EPS = eps
PNG = png
GEN = gen

# The following subdirectory are considered for inclusion by bibtex.

BIB = bib

TEXINPUTS := .:$(CLS):$(STY):$(TEX):$(FIG):$(PDF):$(EPS):$(PNG):$(GEN):

BIBINPUTS = .:$(BIB)

export TEXINPUTS BIBINPUTS

PDFOUTPUTS = $(patsubst %.tex,%.pdf,$(wildcard *.tex))

DEPENDS_ON =\
$(wildcard $(CLS)/*.cls)\
$(wildcard $(STY)/*.sty)\
$(wildcard $(TEX)/*.tex)\
$(wildcard $(FIG)/*.eps)\
$(wildcard $(PDF)/*.eps)\
$(wildcard $(EPS)/*.eps)\
$(wildcard $(PNG)/*.eps)\
$(wildcard $(BIB)/*.bib)\
$(wildcard $(GEN)/*)\

TMP_DIR = tmp

all : $(PDFOUTPUTS)

%.pdf : %.tex $(DEPENDS_ON)
	mkdir -p $(TMP_DIR)
	pdflatex -interaction=nonstopmode -output-directory $(TMP_DIR) $<
	-bibtex $(TMP_DIR)/$*
	pdflatex -interaction=nonstopmode -output-directory $(TMP_DIR) $<
	pdflatex -interaction=nonstopmode -output-directory $(TMP_DIR) $<
	mv $(TMP_DIR)/$@ .

show : $(PDFOUTPUTS)
	evince $(PDFOUTPUTS) &

clean :
	rm -rf $(PDFOUTPUTS) $(TMP_DIR) $(GEN)/*

.PHONY : all show clean
