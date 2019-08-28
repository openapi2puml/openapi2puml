package org.openapi2puml.openapi.plantuml.helpers;

import org.openapi2puml.openapi.plantuml.vo.ClassDiagram;
import org.openapi2puml.openapi.plantuml.vo.ClassRelation;
import org.openapi2puml.openapi.plantuml.vo.InterfaceDiagram;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlantUMLRelationHelper {

  private static final Logger LOGGER = Logger.getLogger(PlantUMLRelationHelper.class.getName());

  public List<ClassRelation> getRelations(List<ClassDiagram> classDiagrams, List<InterfaceDiagram> interfaceDiagrams) {
    List<ClassRelation> relations = new ArrayList<>();
    relations.addAll(getAllModelRelations(classDiagrams));
    relations.addAll(getAllInterfacesRelations(interfaceDiagrams));

    return filterUnique(relations, false);
  }

  private List<ClassRelation> getAllModelRelations(List<ClassDiagram> classDiagrams) {
    List<ClassRelation> modelRelations = new ArrayList<>();

    for (ClassDiagram classDiagram : classDiagrams) {
      List<ClassRelation> classRelations = classDiagram.getChildClass();

      for (ClassRelation classRelation : classRelations) {
        classRelation.setSourceClass(classDiagram.getClassName());
        modelRelations.add(classRelation);
      }
    }

    return modelRelations;
  }

  private List<ClassRelation> getAllInterfacesRelations(List<InterfaceDiagram> interfaceDiagrams) {
    List<ClassRelation> modelRelations = new ArrayList<>();

    for (InterfaceDiagram classDiagram : interfaceDiagrams) {
      List<ClassRelation> classRelations = classDiagram.getChildClass();

      for (ClassRelation classRelation : classRelations) {
        classRelation.setSourceClass(classDiagram.getInterfaceName());
        modelRelations.add(classRelation);
      }
    }

    return modelRelations;
  }

  private List<ClassRelation> filterUnique(List<ClassRelation> relations, boolean compareTargetOnly) {
    List<ClassRelation> uniqueList = new ArrayList<>();

    for (ClassRelation relation : relations) {
      if (!isTargetClassInMap(relation, uniqueList, compareTargetOnly)) {
        uniqueList.add(relation);
      }
    }

    return uniqueList;
  }

  private boolean isTargetClassInMap(ClassRelation sourceRelation, List<ClassRelation> relatedResponses,
                                     boolean considerTargetOnly) {
    for (ClassRelation relation : relatedResponses) {

      if (considerTargetOnly) {
        if (StringUtils.isNotEmpty(relation.getTargetClass()) && StringUtils.isNotEmpty(sourceRelation.getTargetClass())
            && relation.getTargetClass().equalsIgnoreCase(sourceRelation.getTargetClass())) {
          return true;
        }
      } else {
        if (StringUtils.isNotEmpty(relation.getSourceClass())
            && StringUtils.isNotEmpty(sourceRelation.getSourceClass())
            && StringUtils.isNotEmpty(relation.getTargetClass())
            && StringUtils.isNotEmpty(sourceRelation.getTargetClass())
            && relation.getSourceClass().equalsIgnoreCase(sourceRelation.getSourceClass())
            && relation.getTargetClass().equalsIgnoreCase(sourceRelation.getTargetClass())) {

          return true;
        }
      }
    }

    return false;
  }

}
