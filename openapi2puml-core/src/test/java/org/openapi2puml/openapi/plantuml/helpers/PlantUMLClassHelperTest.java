package org.openapi2puml.openapi.plantuml.helpers;

import org.junit.jupiter.api.Tag;
import org.openapi2puml.openapi.plantuml.vo.ClassDiagram;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlantUMLClassHelperTest {

  private PlantUMLClassHelper helper = new PlantUMLClassHelper(true);

  private static final String CLASS_CAT = "Cat";
  @Test
  @DisplayName("Check basic Class Diagram list creation")
  @Tag("integration-test")
  void processSwaggerModels() {
    String specFile = "src/test/resources/petstore/swagger.yaml";
    Swagger swagger = new SwaggerParser().read(new File(specFile).getAbsolutePath());

    List<ClassDiagram> classDiagrams = helper.processSwaggerModels(swagger);

    assertNotNull(classDiagrams, "Model should have at least one class");
  }

  @Test
  @DisplayName("Check basic Class Diagram list creation with mocked swagger")
  void processSwaggerModelsWithSwaggerMock() {
    Map<String, Model > modelsMap = new HashMap<>();
    modelsMap.put(CLASS_CAT, makeMockModel(CLASS_CAT));
    Swagger swagger = mock(Swagger.class);
    when(swagger.getDefinitions()).thenReturn(modelsMap);

    List<ClassDiagram> classDiagrams = helper.processSwaggerModels(swagger);

    assertNotNull(classDiagrams, "Model should have at least one class");
    assertTrue(classDiagrams.size() == 1);
    assertEquals(CLASS_CAT, classDiagrams.get(0).getClassName());
  }

  private ModelImpl makeMockModel(String modelName) {
    ModelImpl model = new ModelImpl();
    model.setName(modelName);
    return model;
  }

}