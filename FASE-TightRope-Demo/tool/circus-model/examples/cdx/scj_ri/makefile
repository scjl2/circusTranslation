# Makefile to compile the RTSJ-based reference implementation of SCJ.
SRC = src
CLS = classes

JAVA_FILES =\
  src/javax/safetycritical/*.java\
  src/javax/safetycritical/annotate/*.java

JAVAC = jamaicac

all :
	$(JAVAC) -sourcepath $(SRC) -d $(CLS) $(JAVA_FILES)

clean :
	rm -rf $(CLS)/*
