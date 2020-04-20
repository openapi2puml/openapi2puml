package org.openapi2puml.openapi.plantuml.helpers;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openapi2puml.openapi.plantuml.vo.InterfaceDiagram;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlantUMLInterfaceDiagramHelperTest {

  private PlantUMLInterfaceDiagramHelper helper = new PlantUMLInterfaceDiagramHelper();

  private static final String PATH_PET = "Pet";

  @Test
  @DisplayName("Check basic Interface Diagram list creation")
  @Tag("integration-test")
  void test_processSwaggerPaths() {
    // TODO - replace with a mock to allow specific testing
    String specFile = "src/test/resources/petstore/swagger.yaml";
    Swagger swagger = new SwaggerParser().read(new File(specFile).getAbsolutePath());

    List<InterfaceDiagram> interfaceDiagrams = helper.processSwaggerPaths(swagger);

    assertNotNull(interfaceDiagrams, "Model should have at least one interface");
  }

  @Test
  @DisplayName("Check basic Interface Diagram list creation with mocks")
  void test_processSwaggerPathsWithMocks(){
    Map<String, Path> PathsMap = new HashMap<>();
    PathsMap.put(PATH_PET, makeMockPath(PATH_PET));
    Swagger swagger = mock(Swagger.class);
    when(swagger.getPaths()).thenReturn(PathsMap);

    List<InterfaceDiagram> interfaceDiagrams = helper.processSwaggerPaths(swagger);
    assertNotNull(interfaceDiagrams);
    assertEquals(PATH_PET+"Api", interfaceDiagrams.get(0).getInterfaceName());
  }

  private Path makeMockPath(String pathName){
    Path mockPath = new Path();

    // create an operation
    Operation getOperation = new Operation();
    getOperation.setOperationId("get"+pathName);
    getOperation.setDescription(pathName);
    getOperation.setTags(Arrays.asList(pathName));

    Response http200Response = new Response();
    http200Response.description("Mock Response");
    getOperation.response(200, http200Response);

    mockPath.get(getOperation);

    return mockPath;
  }
}