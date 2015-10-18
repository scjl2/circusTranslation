# Makefile to obtain and compile the icecap SCJ library implementation.

JAVA_FILES =\
  icecapSDK/src/javax/realtime/*.java\
  icecapSDK/src/javax/safetycritical/*.java\
  icecapSDK/src/javax/safetycritical/annotate/*.java

all : checkout compile success

checkout :
	@echo "Makefile: Checking out icecapSDK from icelab.dk..."
#	cvs -d:pserver:anonymous@icelab.dk:/data/cvs co icecaptools
#	cvs -d:pserver:anonymous@icelab.dk:/data/cvs co icecapv
	cvs -d:pserver:anonymous@icelab.dk:/data/cvs co icecapSDK
#	cvs -d:pserver:anonymous@icelab.dk:/data/cvs co

compile :
	@echo "Makefile: Compiling icecapSDK..."
	mkdir -p icecapSDK/classes
	javac -cp icecapSDK/src -d icecapSDK/classes/ $(JAVA_FILES)

success :
	@echo "Makefile: Success!"
	@echo "Compiled classes are in icecapSDK/classes."

clean :
	@echo "Deleting local repositories..."
#	rm -rf icecaptools
#	rm -rf icecapv
	rm -rf icecapSDK
#	rm -rf SCJJMLTest

.PHONY : checkout compile success clean
