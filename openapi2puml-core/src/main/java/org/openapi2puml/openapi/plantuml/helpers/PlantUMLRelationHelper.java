package org.openapi2puml.openapi.plantuml.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openapi2puml.openapi.plantuml.vo.ClassDiagram;
import org.openapi2puml.openapi.plantuml.vo.ClassRelation;
import org.openapi2puml.openapi.plantuml.vo.InterfaceDiagram;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PlantUMLRelationHelper {
  private static final Logger logger = LogManager.getLogger(PlantUMLRelationHelper.class);

  public List<ClassRelation> getRelations(List<ClassDiagram> classDiagrams, List<InterfaceDiagram> interfaceDiagrams) {
    List<ClassRelation> relations = new ArrayList<>();
    relations.addAll(getAllClassRelations(classDiagrams));
    relations.addAll(getAllInterfacesRelations(interfaceDiagrams));

    return filterUniqueRelations(relations, false);
  }

  private List<ClassRelation> getAllClassRelations(List<ClassDiagram> classDiagrams) {
    List<ClassRelation> modelRelations = new ArrayList<>();

    for (ClassDiagram classDiagram : classDiagrams) {
      logger.debug("Creating Relations for Class: " + classDiagram.getClassName());
      List<ClassRelation> classRelations = classDiagram.getChildClasses();

      for (ClassRelation classRelation : classRelations) {
        logger.debug("- Relation to target class: " + classRelation.getTargetClass());
        // TODO - check why we don't set this (and for interfaces) when creating the relations
        classRelation.setSourceClass(classDiagram.getClassName());
        modelRelations.add(classRelation);
      }
    }

    return modelRelations;
  }

  private List<ClassRelation> getAllInterfacesRelations(List<InterfaceDiagram> interfaceDiagrams) {
    List<ClassRelation> modelRelations = new ArrayList<>();

    for (InterfaceDiagram interfaceDiagram : interfaceDiagrams) {
      logger.debug("Creating Relations for Interface: " + interfaceDiagram.getInterfaceName());
      List<ClassRelation> classRelations = interfaceDiagram.getChildClasses();

      for (ClassRelation classRelation : classRelations) {
        logger.debug("- Relation to target class: " + classRelation.getTargetClass());
        // TODO - check why we don't set this (and for interfaces) when creating the relations
        classRelation.setSourceClass(interfaceDiagram.getInterfaceName());
        modelRelations.add(classRelation);
      }
    }

    return modelRelations;
  }

  /**
   * From the list of ClassRelations from the Classes and Interfaces, find the list of
   * unique Class Relations
   *
   * TODO - why not just use a set before this -> need to check the comparison.
   *
   * @param allRelations
   * @param compareTargetOnly
   * @return
   */
  private List<ClassRelation> filterUniqueRelations(List<ClassRelation> allRelations, boolean compareTargetOnly) {
    List<ClassRelation> uniqueRelations = new ArrayList<>();

    for (ClassRelation relation : allRelations) {
      if (!isRelationAlreadyInUniqueRelations(relation, uniqueRelations, compareTargetOnly)) {
        uniqueRelations.add(relation);
      }
    }

    return uniqueRelations;
  }

  private boolean isRelationAlreadyInUniqueRelations(ClassRelation sourceRelation, List<ClassRelation> relatedResponses,
                                                     boolean considerTargetClassOnly) {
    for (ClassRelation relation : relatedResponses) {

      if (considerTargetClassOnly) {
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
