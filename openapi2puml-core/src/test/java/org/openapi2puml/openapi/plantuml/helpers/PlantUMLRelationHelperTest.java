package org.openapi2puml.openapi.plantuml.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapi2puml.openapi.plantuml.vo.ClassDiagram;
import org.openapi2puml.openapi.plantuml.vo.ClassRelation;
import org.openapi2puml.openapi.plantuml.vo.InterfaceDiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlantUMLRelationHelperTest {
  private PlantUMLRelationHelper relationHelper = new PlantUMLRelationHelper();

  private static final String TARGET_CLASSNAME = "MyTargetClass";
  private static final String SOURCE_CLASSNAME = "MySourceClass";
  private static final String SOURCE_INTERFACENAME = "MySourceInterface";


  @Test
  @DisplayName("Check basic relation list creation with empty relations")
  void test_getRelations_whenEmpty() {
    List<ClassRelation> relations = relationHelper.getRelations(new ArrayList<>(), new ArrayList<>());
    Assertions.assertTrue(relations.isEmpty());
  }

  @Test
  @DisplayName("Check basic relation list creation")
  void test_getRelations() {
    List<ClassRelation> relations = relationHelper.getRelations(getMockClassDiagrams(), getMockInterfaceDiagrams());
    assertFalse(relations.isEmpty());

    assertEquals(2, relations.size());

    // Class relation
    assertEquals(TARGET_CLASSNAME, relations.get(0).getTargetClass());
    assertEquals(SOURCE_CLASSNAME, relations.get(0).getSourceClass());

    // Interface relation
    assertEquals(TARGET_CLASSNAME, relations.get(1).getTargetClass());
    assertEquals(SOURCE_INTERFACENAME, relations.get(1).getSourceClass());
  }


  private List<ClassDiagram> getMockClassDiagrams() {
    List<ClassDiagram> classDiagrams = new ArrayList<>();

    ClassDiagram classDiagram = new ClassDiagram(SOURCE_CLASSNAME, null, null, null, true, null);
    ClassRelation classRelation = new ClassRelation(TARGET_CLASSNAME, false, false, "1..*", null);
    classDiagram.setChildClasses(Collections.singletonList(classRelation));

    classDiagrams.add(classDiagram);

    return classDiagrams;
  }

  private List<InterfaceDiagram> getMockInterfaceDiagrams() {
    List<InterfaceDiagram> interfaceDiagrams = new ArrayList<>();

    InterfaceDiagram interfaceDiagram = new InterfaceDiagram();
    interfaceDiagram.setInterfaceName(SOURCE_INTERFACENAME);
    ClassRelation classRelation = new ClassRelation(TARGET_CLASSNAME, false, false, "1..*", null);

    interfaceDiagram.setChildClasses(Collections.singletonList(classRelation));
    interfaceDiagrams.add(interfaceDiagram);

    return interfaceDiagrams;
  }
}
