package com.kicksolutions.swagger.plantuml.helpers;

import com.kicksolutions.swagger.plantuml.vo.InterfaceDiagram;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlantUMLInterfaceDiagramHelperTest {

  private PlantUMLInterfaceDiagramHelper helper = new PlantUMLInterfaceDiagramHelper();

  @Test
  @DisplayName("Check basic Interface Diagram list creation")
  void processSwaggerPaths() {
    // TODO - replace with a mock to allow specific testing
    String specFile = "src/test/resources/petstore/swagger.yaml";
    Swagger swagger = new SwaggerParser().read(new File(specFile).getAbsolutePath());

    List<InterfaceDiagram> interfaceDiagrams = helper.processSwaggerPaths(swagger);

    assertNotNull(interfaceDiagrams, "Model should have at least one interface");
  }

  // TODO - Testing with Mock for interfaces
//  @Test
//  @DisplayName("Check basic Class Diagram list creation with mocked swagger")
//  void processSwaggerModelsWithSwaggerMock() {
//    Map<String, Model > modelsMap = new HashMap<>();
//    modelsMap.put("Cat", makeMockModel());
//    Swagger swagger = mock(Swagger.class);
//    when(swagger.getDefinitions()).thenReturn(modelsMap);
//
//    List<ClassDiagram> classDiagrams = helper.processSwaggerModels(swagger);
//
//    assertNotNull(classDiagrams, "Model should have at least one class");
//    assertTrue(classDiagrams.size() == 1);
//    assertEquals(classDiagrams.get(0).getClassName(), "Cat"); // TODO - hardcodes
//  }
//
//  private ModelImpl makeMockModel() {
//    ModelImpl model = new ModelImpl();
//    model.setName("Cat");
//    return model;
//  }

}