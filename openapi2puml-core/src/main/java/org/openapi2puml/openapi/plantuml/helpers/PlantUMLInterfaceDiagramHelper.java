package org.openapi2puml.openapi.plantuml.helpers;

import org.apache.logging.log4j.LogManager;
import org.openapi2puml.openapi.plantuml.vo.ClassRelation;
import org.openapi2puml.openapi.plantuml.vo.InterfaceDiagram;
import org.openapi2puml.openapi.plantuml.vo.MethodDefinitions;
import io.swagger.models.*;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.*;
import org.apache.commons.lang3.StringUtils;
import org.openapi2puml.openapi.plantuml.FormatUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PlantUMLInterfaceDiagramHelper {

  private static final Logger LOGGER = Logger.getLogger(PlantUMLInterfaceDiagramHelper.class.getName());
  private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PlantUMLInterfaceDiagramHelper.class);


  public List<InterfaceDiagram> processSwaggerPaths(Swagger swagger) {
    List<InterfaceDiagram> interfaceDiagrams = new ArrayList<>();
    Map<String, Path> paths = swagger.getPaths();

    logger.debug("Paths to Process: " + paths.keySet().toString());

    for (Map.Entry<String, Path> entry : paths.entrySet()) {
      Path pathObject = entry.getValue();

      logger.debug("Processing Path: " + entry.getKey());

      List<Operation> operations = pathObject.getOperations();
      String uri = entry.getKey();

      for (Operation operation : operations) {
        interfaceDiagrams.add(getInterfaceDiagram(operation, uri));
      }
    }

    return interfaceDiagrams;
  }

  private InterfaceDiagram getInterfaceDiagram(Operation operation, String uri) {
    LOGGER.entering(LOGGER.getName(), "getInterfaceDiagram");

    InterfaceDiagram interfaceDiagram = new InterfaceDiagram();
    String interfaceName = getInterfaceName(operation.getTags(), operation, uri);
    List<String> errorClassNames = getErrorClassNames(operation);
    interfaceDiagram.setInterfaceName(interfaceName);
    interfaceDiagram.setErrorClasses(errorClassNames);
    interfaceDiagram.setMethods(getInterfaceMethods(operation));
    interfaceDiagram.setChildClass(getInterfaceRelations(operation, errorClassNames));

    LOGGER.exiting(LOGGER.getName(), "getInterfaceDiagram");
    return interfaceDiagram;
  }

  private String getInterfaceName(List<String> tags, Operation operation, String uri) {
    String interfaceName;

    if (!tags.isEmpty()) {
      interfaceName = FormatUtility.toTitleCase(tags.get(0).replaceAll(" ", ""));
    } else if (StringUtils.isNotEmpty(operation.getOperationId())) {
      interfaceName = FormatUtility.toTitleCase(operation.getOperationId());
    } else {
      interfaceName = FormatUtility.toTitleCase(uri.replaceAll("{", "").replaceAll("}", "").replaceAll("\\", ""));
    }

    return interfaceName + "Api";
  }

  private List<String> getErrorClassNames(Operation operation) {
    List<String> errorClasses = new ArrayList<>();
    Map<String, Response> responses = operation.getResponses();

    for (Map.Entry<String, Response> responsesEntry : responses.entrySet()) {
      String responseCode = responsesEntry.getKey();

      if (responseCode.equalsIgnoreCase("default") || Integer.parseInt(responseCode) >= 300) {
        Property responseProperty = responsesEntry.getValue().getSchema();

        if (responseProperty instanceof RefProperty) {
          String errorClassName = ((RefProperty) responseProperty).getSimpleRef();
          if (!errorClasses.contains(errorClassName)) {
            errorClasses.add(errorClassName);
          }
        }
      }
    }

    return errorClasses;
  }

  private List<ClassRelation> getInterfaceRelatedInputs(Operation operation) {
    List<ClassRelation> relatedResponses = new ArrayList<>();
    List<Parameter> parameters = operation.getParameters();

    for (Parameter parameter : parameters) {
      if (parameter instanceof BodyParameter) {
        Model bodyParameter = ((BodyParameter) parameter).getSchema();

        if (bodyParameter instanceof RefModel) {

          ClassRelation classRelation = new ClassRelation();
          classRelation.setTargetClass(((RefModel) bodyParameter).getSimpleRef());
          classRelation.setComposition(false);
          classRelation.setExtension(true);

          relatedResponses.add(classRelation);
        } else if (bodyParameter instanceof ArrayModel) {
          Property propertyObject = ((ArrayModel) bodyParameter).getItems();

          if (propertyObject instanceof RefProperty) {
            ClassRelation classRelation = new ClassRelation();
            classRelation.setTargetClass(((RefProperty) propertyObject).getSimpleRef());
            classRelation.setComposition(false);
            classRelation.setExtension(true);

            relatedResponses.add(classRelation);
          }
        }
      }
    }

    return relatedResponses;
  }

  private List<ClassRelation> getInterfaceRelatedResponses(Operation operation) {
    List<ClassRelation> relatedResponses = new ArrayList<>();
    Map<String, Response> responses = operation.getResponses();

    for (Map.Entry<String, Response> responsesEntry : responses.entrySet()) {
      String responseCode = responsesEntry.getKey();

      if (!(responseCode.equalsIgnoreCase("default") || Integer.parseInt(responseCode) >= 300)) {
        Property responseProperty = responsesEntry.getValue().getSchema();

        if (responseProperty instanceof RefProperty) {
          ClassRelation relation = new ClassRelation();
          relation.setTargetClass(((RefProperty) responseProperty).getSimpleRef());
          relation.setComposition(false);
          relation.setExtension(true);

          relatedResponses.add(relation);
        } else if (responseProperty instanceof ArrayProperty) {
          ArrayProperty arrayObject = (ArrayProperty) responseProperty;
          Property arrayResponseProperty = arrayObject.getItems();

          if (arrayResponseProperty instanceof RefProperty) {
            ClassRelation relation = new ClassRelation();
            relation.setTargetClass(((RefProperty) arrayResponseProperty).getSimpleRef());
            relation.setComposition(false);
            relation.setExtension(true);

            relatedResponses.add(relation);
          }
        }
      }

    }

    return relatedResponses;
  }

  private List<MethodDefinitions> getInterfaceMethods(Operation operation) {
    List<MethodDefinitions> interfaceMethods = new ArrayList<>();
    MethodDefinitions methodDefinitions = new MethodDefinitions();
    methodDefinitions.setMethodDefinition(operation.getOperationId() + "(" +
        getMethodParameters(operation) + ")");
    methodDefinitions.setReturnType(getInterfaceReturnType(operation));

    interfaceMethods.add(methodDefinitions);

    return interfaceMethods;
  }

  private String getMethodParameters(Operation operation) {
    String methodParameter = "";
    List<Parameter> parameters = operation.getParameters();

    for (Parameter parameter : parameters) {
      if (StringUtils.isNotEmpty(methodParameter)) {
        methodParameter += ",";
      }

      if (parameter instanceof PathParameter) {
        methodParameter += FormatUtility.toTitleCase(
            ((PathParameter) parameter).getType()) + " " + ((PathParameter) parameter).getName();
      } else if (parameter instanceof QueryParameter) {
        Property queryParameterProperty = ((QueryParameter) parameter).getItems();

        if (queryParameterProperty instanceof RefProperty) {
          methodParameter += FormatUtility.toTitleCase(
              ((RefProperty) queryParameterProperty).getSimpleRef()) + "[] " + ((BodyParameter) parameter).getName();
        } else if (queryParameterProperty instanceof StringProperty) {
          methodParameter += FormatUtility.toTitleCase(
              queryParameterProperty.getType()) + "[] " + ((QueryParameter) parameter).getName();
        } else {
          methodParameter += FormatUtility.toTitleCase(
              ((QueryParameter) parameter).getType()) + " " + ((QueryParameter) parameter).getName();
        }
      } else if (parameter instanceof BodyParameter) {
        Model bodyParameter = ((BodyParameter) parameter).getSchema();

        if (bodyParameter instanceof RefModel) {
          methodParameter += FormatUtility.toTitleCase(
              ((RefModel) bodyParameter).getSimpleRef()) + " " + ((BodyParameter) parameter).getName();
        } else if (bodyParameter instanceof ArrayModel) {
          Property propertyObject = ((ArrayModel) bodyParameter).getItems();

          if (propertyObject instanceof RefProperty) {
            methodParameter += FormatUtility.toTitleCase(
                ((RefProperty) propertyObject).getSimpleRef()) + "[] " + ((BodyParameter) parameter).getName();
          }
        }
      } else if (parameter instanceof FormParameter) {
        methodParameter += FormatUtility.toTitleCase(
            ((FormParameter) parameter).getType()) + " " + ((FormParameter) parameter).getName();
      }
    }

    return methodParameter;
  }

  private String getInterfaceReturnType(Operation operation) {
    String returnType = "void";

    Map<String, Response> responses = operation.getResponses();
    for (Map.Entry<String, Response> responsesEntry : responses.entrySet()) {
      String responseCode = responsesEntry.getKey();

      if (!(responseCode.equalsIgnoreCase("default") || Integer.parseInt(responseCode) >= 300)) {
        Property responseProperty = responsesEntry.getValue().getSchema();

        if (responseProperty instanceof RefProperty) {
          returnType = ((RefProperty) responseProperty).getSimpleRef();
        } else if (responseProperty instanceof ArrayProperty) {
          Property arrayResponseProperty = ((ArrayProperty) responseProperty).getItems();
          if (arrayResponseProperty instanceof RefProperty) {
            returnType = ((RefProperty) arrayResponseProperty).getSimpleRef() + "[]";
          }
        } else if (responseProperty instanceof ObjectProperty) {
          returnType = FormatUtility.toTitleCase(operation.getOperationId()) + "Generated";
        }
      }
    }

    return returnType;
  }

  private List<ClassRelation> getInterfaceRelations(Operation operation, List<String> errorClassNames) {
    List<ClassRelation> relations = new ArrayList<>();
    relations.addAll(getInterfaceRelatedResponses(operation));
    relations.addAll(getInterfaceRelatedInputs(operation));
    for (String errorClassName : errorClassNames) {
      relations.add(getErrorClass(errorClassName));
    }

    return filterUnique(relations, true);
  }

  private ClassRelation getErrorClass(String errorClassName) {
    ClassRelation classRelation = new ClassRelation();
    classRelation.setTargetClass(errorClassName);
    classRelation.setComposition(false);
    classRelation.setExtension(true);

    return classRelation;
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
