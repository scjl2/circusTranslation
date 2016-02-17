package hijac.tools.analysis;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;

import hijac.tools.collections.Collections;
import hijac.tools.compiler.SCJCompilationException;
import hijac.tools.compiler.SCJCompilationTask;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Class that encapsulates the analysis of and SCJ application.
 *
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class SCJAnalysis {
   /**
    * Utility object for {@link com.sun.source.util.Trees} associated with
    * this analysis.
    */
   public final Trees TREES;

   /**
    *  Utility object for {@link javax.lang.model.util.Elements} associated
    * with this analysis.
    */
   public final Elements ELEMENTS;

   /**
    * Utility object for {@link javax.lang.model.util.Types} associated with
    * this analysis.
    */
   public final Types TYPES;

   /* Members that record compilation units and type elements. */
   protected final Set<CompilationUnitTree> units;
   protected final Set<TypeElement> type_elements;

   /* Dictionary that records the analysis data. */
   protected final HashMap<SCJDataKey, Object> data;

   /* Every SCJAnalysis has to be linked to an SCJCompilationTask. */

   protected SCJAnalysis(SCJCompilationTask task,
         Iterable<CompilationUnitTree> units,
         Iterable<TypeElement> type_elements) {
      assert task.success();
      TREES = task.getTrees();
      ELEMENTS = task.getElements();
      TYPES = task.getTypes();
      this.units = new HashSet<CompilationUnitTree>();
      this.type_elements = new HashSet<TypeElement>();
      Collections.addAll(this.units, units);
      Collections.addAll(this.type_elements, type_elements);
      data = new HashMap<SCJDataKey, Object>();
   }

   /**
    * Creates a new analysis for an SCJ compilation task.
    *
    * <p>This is currently the only way to construct an {@code SCJAnalysis}
    * object. Note that a consequence of calling this method is the
    * compilation task being actually executed. This involves parsing and
    * analysing the SCJ sources by the Java parser.</p>
    *
    * @param task compilation task for which the analysis is created.
    *
    * @return SCJAnalysis object for the analysis.
    */
   @SuppressWarnings("unchecked")
   public static SCJAnalysis create(SCJCompilationTask task)
         throws IOException, SCJCompilationException {
      Iterable<CompilationUnitTree> units =
         (Iterable<CompilationUnitTree>) task.parse();
      if (!task.success()) { task.raiseError(); }
      Iterable<TypeElement> type_elements =
         (Iterable<TypeElement>) task.analyze();
      if (!task.success()) { task.raiseError(); }
      return new SCJAnalysis(task, units, type_elements);
   }

   /**
    * Returns all compilation units of the compiled SCJ sources.
    *
    * @return Set of compilation units of the Java Compiler Tree API.
    */
   public Set<CompilationUnitTree> getCompilationUnits() {
      return units;
   }

   /**
    * Returns all type elements of the compiled SCJ sources.
    *
    * @return Set of type elements of the Java model API.
    */
   public Set<TypeElement> getTypeElements() {
      return type_elements;
   }

   /**
    * Returns a specific type element of the compiled SCJ sources given its
    * canonical name.
    *
    * @param fullname Full canonical name of the type element.
    *
    * @return Type element of the Java model API.
    */
   public TypeElement getTypeElement(String fullname) {
      return ELEMENTS.getTypeElement(fullname);
   }

   /**
    * Returns all super classes of a given  type element i.e. not just the
    * direct ones.
    *
    * @param type_element Type element to examine.
    *
    * @return Set of type elements yielding the super classes.
    */
   public Set<TypeElement> getAllSuperclasses(TypeElement type_element) {
      Set<TypeElement> result = new HashSet<TypeElement>();
      TypeMirror super_type = type_element.getSuperclass();
      if (super_type.getKind() != TypeKind.NONE) {
         TypeElement element = (TypeElement) TYPES.asElement(super_type);
         result.add(element);
         result.addAll(getAllSuperclasses(element));
      }
      return result;
   }

   /**
    * Returns all implemented interfaces of a given  type element i.e. not
    * just the direct ones.
    *
    * @param type_element Type element to examine.
    *
    * @return Set of type elements yielding the interfaces.
    */
   public Set<TypeElement> getAllInterfaces(TypeElement type_element) {
      Set<TypeElement> result = new HashSet<TypeElement>();
      for(TypeMirror type : type_element.getInterfaces()) {
         TypeElement element = (TypeElement) TYPES.asElement(type);
         result.add(element);
         result.addAll(getAllInterfaces(element));
      }
      TypeMirror super_type = type_element.getSuperclass();
      if (super_type.getKind() != TypeKind.NONE) {
         TypeElement element = (TypeElement) TYPES.asElement(super_type);
         result.addAll(getAllInterfaces(element));
      }
      return result;
   }

   /* Data Storage */

   /* Should we implement the full Map<SCJDataKey, Object> interface? */

   /**
    * Clears all stored data items.
    */
   public void clear() {
      data.clear();
   }

   /**
    * Determines if a data item for a specific key exists.
    *
    * @param key Data key to check for.
    *
    * @return True if an item has been stored under this key.
    */
   public boolean containsKey(SCJDataKey key) {
      return data.containsKey(key);
   }

   /**
    * Determines if data items for all keys in an array exist.
    *
    * @param keys Array of data keys to check for.
    *
    * @return True if items have been stored for all keys.
    */
   public boolean containsKeys(SCJDataKey[] keys) {
      for (SCJDataKey key : keys) {
         if (!data.containsKey(key)) {
            return false;
         }
      }
      return true;
   }

   /**
    * Retrieves a data item stored under a given key.
    *
    * @param key Data key to look up.
    *
    * @return Value stored under the key.
    */
   @SuppressWarnings("unchecked")
   public <T> T get(SCJDataKey key) {
      return (T) data.get(key);
   }

   /**
    * Determines if the item storage is empty.
    *
    * @return True if no data items have been stored.
    */
   public boolean isEmpty() {
      return data.isEmpty();
   }

   /**
    * Stores a data item under a given key.
    *
    * @param key Data key under which to store the item.
    * @param value Data item to be stored.
    */
   public <T> void put(SCJDataKey key, T value) {
      assert !data.containsKey(key);
      data.put(key, value);
   }

   /**
    * Removes a data item stored under a given key.
    *
    * @param key Data key for which to remove the stored item.
    */
   public void remove(SCJDataKey key) {
      data.remove(key);
   }

   /**
    * Removes all data items stored under the keys in an array.
    *
    * @param keys Data keys for which to remove the stored items.
    */
   public void removeAll(SCJDataKey[] keys) {
      for (SCJDataKey key : keys) {
         data.remove(key);
      }
   }

   /**
    * Returns all values stored by this analysis.
    *
    * @return Values stored in the analysis map.
    */
   public Collection<Object> values() {
      return data.values();
   }
}
