package org.openapi2puml.openapi.plantuml;

import org.openapi2puml.openapi.plantuml.helpers.PlantUMLClassHelper;
import org.openapi2puml.openapi.plantuml.helpers.PlantUMLInterfaceDiagramHelper;
import org.openapi2puml.openapi.plantuml.helpers.PlantUMLRelationHelper;
import org.openapi2puml.openapi.plantuml.vo.ClassDiagram;
import org.openapi2puml.openapi.plantuml.vo.InterfaceDiagram;
import io.swagger.models.Swagger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PlantUMLCodegen {

  static final String TITLE = "title";
  static final String VERSION = "version";
  static final String CLASS_DIAGRAMS = "classDiagrams";
  static final String INTERFACE_DIAGRAMS = "interfaceDiagrams";
  static final String ENTITY_RELATIONS = "entityRelations";

  private boolean generateDefinitionModelOnly;
  private boolean includeCardinality;
  private Swagger swagger;
  private File targetLocation;

  public PlantUMLCodegen(Swagger swagger, File targetLocation, boolean generateDefinitionModelOnly,
                         boolean includeCardinality) {
    this.swagger = swagger;
    this.targetLocation = targetLocation;
    this.generateDefinitionModelOnly = generateDefinitionModelOnly;
    this.includeCardinality = includeCardinality;
  }

  /**
   * generate a PlantUML File based on this classes Swagger property
   *
   * @return filepath to the PlantUML file as a String
   * @throws IOException            - If there is an error writing the file
   * @throws IllegalAccessException - if there is an issue generating the file information
   */
  public String generatePlantUmlFile(Swagger swagger) throws IOException, IllegalAccessException {
    Map<String, Object> plantUmlObjectModelMap = convertSwaggerToPlantUmlObjectModelMap(swagger);

    MustacheUtility mustacheUtility = new MustacheUtility();
    String plantUmlFilePath = mustacheUtility.createPlantUmlFile(targetLocation, plantUmlObjectModelMap);

    return plantUmlFilePath;
  }

  public Map<String, Object> convertSwaggerToPlantUmlObjectModelMap(Swagger swagger) {
    Map<String, Object> additionalProperties = new TreeMap<>();

    additionalProperties.put(TITLE, swagger.getInfo().getTitle());
    additionalProperties.put(VERSION, swagger.getInfo().getVersion());

    // First refactoring point - use PlantUMLClassHelper
    PlantUMLClassHelper plantUMLClassHelper = new PlantUMLClassHelper(this.includeCardinality);
    List<ClassDiagram> classDiagrams = plantUMLClassHelper.processSwaggerModels(swagger);
    additionalProperties.put(CLASS_DIAGRAMS, classDiagrams);

    List<InterfaceDiagram> interfaceDiagrams = new ArrayList<>();

    if (!generateDefinitionModelOnly) {
      PlantUMLInterfaceDiagramHelper plantUMLInterfaceDiagramHelper = new PlantUMLInterfaceDiagramHelper();
      interfaceDiagrams.addAll(plantUMLInterfaceDiagramHelper.processSwaggerPaths(swagger));
      additionalProperties.put(INTERFACE_DIAGRAMS, interfaceDiagrams);
    }

    PlantUMLRelationHelper plantUMLRelationHelper = new PlantUMLRelationHelper();
    // TODO - Test class for this part
    additionalProperties.put(ENTITY_RELATIONS, plantUMLRelationHelper.getRelations(classDiagrams, interfaceDiagrams));

    return additionalProperties;
  }

}