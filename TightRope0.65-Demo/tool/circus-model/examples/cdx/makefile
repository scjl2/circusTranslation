##############################################################
# Makefile to compile, run and clean the application sources #
##############################################################

# Change the following path to point to the RTSJ library.
#RTSJ_RI_LIB=/usr/local/pkg/rtsj-ri-1.0.2.6-noarch-1/lib/foundation.jar

SRC1_DIR = java/runnable
SRC2_DIR = java/clean

SRC1_FILES =\
 $(SRC1_DIR)/cdx/*.java\
 $(SRC1_DIR)/javacp/utils/*.java

SRC2_FILES =\
 $(SRC2_DIR)/cdx/*.java\
 $(SRC2_DIR)/javacp/utils/*.java\
 $(SRC2_DIR)/javax/safetycritical/*.java\
 $(SRC2_DIR)/javax/safetycritical/annotate/*.java

CLS1_DIR = classes/runnable
CLS2_DIR = classes/clean

#PATH := scj_ri/bin:$(PATH)

#CLASSPATH := $(RTSJ_RI_LIB):$(CLASSPATH)

export CLASSPATH PATH

all : scj compile

scj :
	make -C scj_ri

compile :
	scjavac -d $(CLS1_DIR) $(SRC1_FILES)
#	  javac -d $(CLS2_DIR) $(SRC2_FILES)

CLASSPATH := $(CLS1_DIR):$(CLASSPATH)

export CLASSPATH

run :
	scjvm cdx.Main

clean :
	make -C scj_ri clean
	rm -rf $(CLS1_DIR)/* $(CLS2_DIR)/*

.PHONY : all scj compile run clean
