package org.openapi2puml.openapi.plantuml;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlantUMLCodegenTest {

  private PlantUMLCodegen plantUMLCodegen = new PlantUMLCodegen(null, null,false, true);

  @Test
  @DisplayName("Check basic PlantUML object generation")
  @Tag("integration-test")
  void test_generatePetStorePlantUml() {
    String specFile = "src/test/resources/petstore/swagger.yaml";
    Swagger swagger = new SwaggerParser().read(new File(specFile).getAbsolutePath());
    Map<String, Object> stringObjectMap = plantUMLCodegen.convertSwaggerToPlantUmlObjectModelMap(swagger);

    assertFalse(stringObjectMap.isEmpty(), "PlantUML object map should not be empty");
    assertNotNull(stringObjectMap.get(plantUMLCodegen.TITLE), "Title should not be null");
    assertNotNull(stringObjectMap.get(plantUMLCodegen.VERSION), "Version should not be null");
    assertNotNull(stringObjectMap.get(plantUMLCodegen.CLASS_DIAGRAMS), "Class diagrams should not be null");
    assertNotNull(stringObjectMap.get(plantUMLCodegen.INTERFACE_DIAGRAMS), "Interface Diagrams should not be null");
    assertNotNull(stringObjectMap.get(plantUMLCodegen.ENTITY_RELATIONS), "Relations should not be null");
  }
}
