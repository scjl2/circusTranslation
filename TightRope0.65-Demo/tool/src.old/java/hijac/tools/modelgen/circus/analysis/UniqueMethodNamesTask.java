package hijac.tools.modelgen.circus.analysis;

import hijac.tools.analysis.AbstractAnalysisTask;
import hijac.tools.analysis.SCJDataKey;

import hijac.tools.config.Config;
import hijac.tools.config.Hijac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 * Analysis task that generates globally unique names for methods.
 *
 * <p>Method names are disambiguated by suffixing them with a number. If
 * the name of a method is globally unique, no prefix is applied to it.</p>
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class UniqueMethodNamesTask extends AbstractAnalysisTask {
   public static final SCJDataKey KEY = Hijac.key("UniqueMethodNames");
   public static final SCJDataKey[] REQUIRES = { };
   public static final SCJDataKey[] GENERATES = { KEY };

   public UniqueMethodNamesTask() {
      super("UniqueMethodNames", REQUIRES, GENERATES);
   } 

   public Map<String, Integer> countNames() {
      Map<String, Integer> name_count =
         new HashMap<String, Integer>();
      for (TypeElement type_element : ANALYSIS.getTypeElements()) {
         if (type_element.getKind() == ElementKind.CLASS &&
            !Config.isSCJClass(type_element.getQualifiedName().toString())) {
            /* What about constructors and initialisers? */
            List<ExecutableElement> methods =
               ElementFilter.methodsIn(type_element.getEnclosedElements());
            for (ExecutableElement method : methods) {
               String name = getMethodName(method);
               if (name_count.containsKey(name)) {
                  name_count.put(name, name_count.get(name) + 1);
               }
               else {
                  name_count.put(name, 1);
               }
            }
         }
      }
      return name_count;
   }

   /* TODO: For methods that have a target we do not need to be bothered with
    * unique names. */

   public Map<ExecutableElement, String> createUniqueNames(
         Map<String, Integer> name_count) {     
      HashMap<ExecutableElement, String> unique_names =
         new HashMap<ExecutableElement, String>();
      for (TypeElement type_element : ANALYSIS.getTypeElements()) {
         if (type_element.getKind() == ElementKind.CLASS &&
            !Config.isSCJClass(type_element.getQualifiedName().toString())) {
            /* What about constructors and initialisers? */
            List<ExecutableElement> methods =
               ElementFilter.methodsIn(type_element.getEnclosedElements());
            for (ExecutableElement method : methods) {
               String name = getMethodName(method);
               assert name_count.containsKey(name);
               String unique_name = createUniqueName(
                  name, unique_names, name_count.get(name) == 1);
               unique_names.put(method, unique_name);
            }
         }
      }
      return unique_names;
   }

   public void doTask() {
      /* Stage 1: Count the number of occurrences of each method name. */
      Map<String, Integer> name_count = countNames();
      /* Stage 2: Generate unique method names as Strings. */
      Map<ExecutableElement, String>
         unique_names = createUniqueNames(name_count);
      /* Stage 3: Generate a Name map from the String map. */
      Map<ExecutableElement, Name> result =
         new HashMap<ExecutableElement, Name>();
      for (ExecutableElement key : unique_names.keySet()) {
         result.put(key, ANALYSIS.ELEMENTS.getName(unique_names.get(key)));
      }
      ANALYSIS.put(KEY, result);
   }

   public static String createUniqueName(
         String name, HashMap<?, String> map, boolean single_occurrence) {
      if (map.containsValue(name) || map.containsValue(name + "1")) {
         int suffix = 1;
         while (map.containsValue(name + suffix)) {
            suffix++;
         }
         return name + suffix;
      }
      else {
         return single_occurrence ? name : (name + "1");
      }
   }

   public static String getMethodName(ExecutableElement method) {
      String name = method.getSimpleName().toString();
      /*name = type_element.getSimpleName().toString() + "_" + name;*/
      return name;
   }
}
