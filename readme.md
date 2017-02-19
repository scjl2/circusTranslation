Can be imported into Eclipse, linked to the hijactools/trunk folder. Add framework and freemarker jars in hijactools/trunk/lib and tools.jar in [path]/jvm/[jvm name]/lib to the build path.

Updated hijactools/trunk/build.xml to download Freemarker v2.3.23

#Input Pattern#
* The Launcher object has 'Launch' in its name
* Separate Files for each object
* Chains of method calls are rewritten into separate method calls
  - var = a().b().c(); -> tmp1 = c(); tmp2 = tmp2.b(); var = tmp2.a();
* While loops must have the entire boolean statement inside the condition, not use a variable.
  - boolean var = a(); while (var) { ... } -> while (a()) { ... }
