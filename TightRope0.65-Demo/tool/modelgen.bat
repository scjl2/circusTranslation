@echo off
setlocal

set HIJAC_HOME=%CD%

set HIJAC_CLASSES=%HIJAC_HOME%\target\classes

set LIB_JARS=%HIJAC_HOME%\local\lib\*:%HIJAC_HOME%\lib\*:%JAVA_HOME%\lib\tools.jar

set CLASSPATH="%HIJAC_CLASSES%;%LIB_JARS%"

set JAVA_OPTS=-enableassertions

java -version

java %JAVA_OPT% -classpath %CLASSPATH% hijac.tools.application.ModelGen
