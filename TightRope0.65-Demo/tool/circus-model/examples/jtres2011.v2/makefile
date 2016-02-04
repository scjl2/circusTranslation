##############################################################
# Makefile to compile, run and clean the application sources #
##############################################################

SRC_DIR = java
CLS_DIR = classes

SRC_FILES =\
$(SRC_DIR)/javax/realtime/*.java\
$(SRC_DIR)/javax/safetycritical/*.java\
$(SRC_DIR)/javax/safetycritical/annotate/*.java\
$(SRC_DIR)/jtres/*.java

CLASSPATH := classes:$(CLASSPATH)

export CLASSPATH

all : compile

compile :
	scjavac -sourcepath $(SRC_DIR) -d $(CLS_DIR) $(SRC_FILES)

run :
	scjvm Main

clean :
	rm -rf $(CLS_DIR)/*

.PHONE : all compile run clean
