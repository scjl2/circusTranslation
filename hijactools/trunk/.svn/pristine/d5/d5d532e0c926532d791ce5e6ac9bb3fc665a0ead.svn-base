##############################################################
# Makefile to compile, run and clean the application sources #
##############################################################

SRC_DIR = java
CLS_DIR = classes

CLASSPATH := classes:$(CLASSPATH)

export CLASSPATH

all : compile

compile :
	scjavac -sourcepath $(SRC_DIR) -d $(CLS_DIR) $(SRC_DIR)/*.java

run :
	scjvm Main

clean :
	rm -rf $(CLS_DIR)/*.class

.PHONE : all compile run clean
