package com.kicksolutions.swagger.plantuml;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlantUMLGeneratorTest {

  private PlantUMLGenerator generator = new PlantUMLGenerator();
  private static final String DEFAULT_PLANT_UML_FILENAME = "swagger.puml";

  // TODO - setup - remove the generated files if they exist (*.puml, *.svg)

  @Test
  @DisplayName("Basic Petstore test")
  void test_generatePetStorePlantUml() {
    String specFile = "src/test/resources/petstore/swagger.yaml";
    String outputPath = "src/test/resources/petstore";
    boolean generateDefinitionModelOnly = false;
    boolean includeCardinality = true;
    boolean generateSvg = true;

    generator.transformSwagger2Puml(specFile, outputPath, generateDefinitionModelOnly, includeCardinality, generateSvg);

    assertTrue(new File(outputPath + "/" + DEFAULT_PLANT_UML_FILENAME).exists(), "Expect PlantUML file to be generated");
  }

  @Test
  @DisplayName("Petstore test with inheritance")
  void test_generatePetStorePlantUml_withInheritance() {
    String specFile = "src/test/resources/petstore_with_inheritance/swagger.yaml";
    String outputPath = "src/test/resources/petstore_with_inheritance";
    boolean generateDefinitionModelOnly = false;
    boolean includeCardinality = true;
    boolean generateSvg = true;

    generator.transformSwagger2Puml(specFile, outputPath, generateDefinitionModelOnly, includeCardinality, generateSvg);

    assertTrue(new File(outputPath + "/" + DEFAULT_PLANT_UML_FILENAME).exists(), "Expect PlantUML file to be generated");
  }

  @Test
  @DisplayName("Petstore test with Multiple Response Types")
  void test_generatePetStorePlantUml_withMultipleResponseTypes() throws Exception{
    String specFile = "src/test/resources/petstore_with_multipleErrorResponseMessages/swagger.yaml";
    String outputPath = "src/test/resources/petstore_with_multipleErrorResponseMessages/";
    boolean generateDefinitionModelOnly = false;
    boolean includeCardinality = true;
    boolean generateSvg = true;

    generator.transformSwagger2Puml(specFile, outputPath, generateDefinitionModelOnly, includeCardinality, generateSvg);

    File plantUmlFile = new File(outputPath + "/" + DEFAULT_PLANT_UML_FILENAME);
    String stringToFind = "CatApi -->    ErrorMessage,WarningMessage";
    assertTrue(plantUmlFile.exists(), "Expect PlantUML file to be generated");
    assertFalse(FileUtils.readFileToString(plantUmlFile).contains(stringToFind), "Expect that invalid response "
        + "realtion will NOT be generated");
  }
}