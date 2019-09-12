package org.openapi2puml.openapi.plantuml.helpers;

import org.openapi2puml.openapi.plantuml.vo.ClassDiagram;
import org.openapi2puml.openapi.plantuml.vo.ClassMembers;
import org.openapi2puml.openapi.plantuml.vo.ClassRelation;
import io.swagger.models.*;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang3.StringUtils;
import org.openapi2puml.openapi.plantuml.FormatUtility;

import java.util.*;
import java.util.logging.Logger;

public class PlantUMLClassHelper {

  private static final Logger LOGGER = Logger.getLogger(PlantUMLClassHelper.class.getName());
  private boolean includeCardinality;

  private static final String CARDINALITY_ONE_TO_MANY = "1..*";
  private static final String CARDINALITY_NONE_TO_MANY = "0..*";

  public PlantUMLClassHelper(boolean includeCardinality) {
    this.includeCardinality = includeCardinality;
  }

  public List<ClassDiagram> processSwaggerModels(Swagger swagger) {
    LOGGER.entering(LOGGER.getName(), "processSwaggerModels");

    List<ClassDiagram> classDiagrams = new ArrayList<>();
    Map<String, Model> modelsMap = swagger.getDefinitions();

    for (Map.Entry<String, Model> models : modelsMap.entrySet()) {
      String className = models.getKey();
      Model modelObject = models.getValue();

      LOGGER.info("Processing Model " + className);

      String superClass = getSuperClass(modelObject);
      List<ClassMembers> classMembers = getClassMembers(modelObject, modelsMap);

      classDiagrams.add(new ClassDiagram(className, modelObject.getDescription(), classMembers,
          getChildClasses(classMembers, superClass), isModelClass(modelObject), superClass));
    }

    LOGGER.exiting(LOGGER.getName(), "processSwaggerModels");

    return classDiagrams;
  }

  private boolean isModelClass(Model model) {
    LOGGER.entering(LOGGER.getName(), "isModelClass");

    boolean isModelClass = true;

    if (model instanceof ModelImpl) {
      List<String> enumValues = ((ModelImpl) model).getEnum();

      if (enumValues != null && !enumValues.isEmpty()) {
        isModelClass = false;
      }
    }

    LOGGER.exiting(LOGGER.getName(), "isModelClass");

    return isModelClass;
  }

  private String getSuperClass(Model model) {
    LOGGER.entering(LOGGER.getName(), "getSuperClass");

    String superClass = null;

    if (model instanceof ArrayModel) {
      ArrayModel arrayModel = (ArrayModel) model;
      Property propertyObject = arrayModel.getItems();

      if (propertyObject instanceof RefProperty) {
        superClass = "ArrayList["+ ((RefProperty) propertyObject).getSimpleRef() + "]";
      }
    } else if (model instanceof ModelImpl) {
      Property addProperty = ((ModelImpl) model).getAdditionalProperties();

      if (addProperty instanceof RefProperty) {
        superClass = "Map[" + ((RefProperty) addProperty).getSimpleRef() + "]";
      }
    }

    LOGGER.exiting(LOGGER.getName(), "getSuperClass");

    return superClass;
  }

  private List<ClassRelation> getChildClasses(List<ClassMembers> classMembers, String superClass) {
    LOGGER.entering(LOGGER.getName(), "getChildClasses");

    List<ClassRelation> childClasses = new ArrayList<>();

    for (ClassMembers member : classMembers) {

      boolean alreadyExists = false;

      for (ClassRelation classRelation : childClasses) {

        if (classRelation.getTargetClass().equalsIgnoreCase(member.getClassName())) {
          alreadyExists = true;
        }
      }

      if (!alreadyExists && member.getClassName() != null && member.getClassName().trim().length() > 0) {
        if (StringUtils.isNotEmpty(superClass)) {
          childClasses.add(new ClassRelation(member.getClassName(), true, false, member.getCardinality(),null));
        } else {
          childClasses.add(new ClassRelation(member.getClassName(), false, true, member.getCardinality(),null));
        }
      }
    }

    LOGGER.exiting(LOGGER.getName(), "getChildClasses");

    return childClasses;
  }

  private List<ClassMembers> getClassMembers(Model modelObject, Map<String, Model> modelsMap) {
    LOGGER.entering(LOGGER.getName(), "getClassMembers");

    List<ClassMembers> classMembers = new ArrayList<>();

    if (modelObject instanceof ModelImpl) {
      classMembers = getClassMembers((ModelImpl) modelObject, modelsMap);
    } else if (modelObject instanceof ComposedModel) {
      classMembers = getClassMembers((ComposedModel) modelObject, modelsMap);
    } else if (modelObject instanceof ArrayModel) {
      classMembers = getClassMembers((ArrayModel) modelObject, modelsMap);
    }

    LOGGER.exiting(LOGGER.getName(), "getClassMembers");
    return classMembers;
  }

  private List<ClassMembers> getClassMembers(ArrayModel arrayModel, Map<String, Model> modelsMap) {
    LOGGER.entering(LOGGER.getName(), "getClassMembers-ArrayModel");

    List<ClassMembers> classMembers = new ArrayList<>();

    Property propertyObject = arrayModel.getItems();

    if (propertyObject instanceof RefProperty) {
      classMembers.add(getRefClassMembers((RefProperty) propertyObject));
    }

    LOGGER.exiting(LOGGER.getName(), "getClassMembers-ArrayModel");
    return classMembers;
  }

  private List<ClassMembers> getClassMembers(ComposedModel composedModel, Map<String, Model> modelsMap) {
    return getClassMembers(composedModel, modelsMap, new HashSet<>());
  }

  /**
   * New Overloaded getClassMembers Implementation to handle deeply nested class hierarchies
   * @param composedModel
   * @param modelsMap
   * @param visited
   * @return
   */
  private List<ClassMembers> getClassMembers(ComposedModel composedModel, Map<String, Model> modelsMap, Set<Model> visited) {
    LOGGER.entering(LOGGER.getName(), "getClassMembers-ComposedModel-DeepNest");

    List<ClassMembers> classMembers = new ArrayList<>();
    Map<String, Property> childProperties = new HashMap<>();

    if (null != composedModel.getChild()) {
      childProperties = composedModel.getChild().getProperties();
    }

    List<ClassMembers> ancestorMembers;

    List<Model> allOf = composedModel.getAllOf();
    for (Model currentModel : allOf) {

      if (currentModel instanceof RefModel) {
        RefModel refModel = (RefModel) currentModel;
        // This line throws an NPE when encountering deeply nested class hierarchies because it assumes any child
        // classes are RefModel and not ComposedModel
//				childProperties.putAll(modelsMap.get(refModel.getSimpleRef()).getProperties());

        Model parentRefModel = modelsMap.get(refModel.getSimpleRef());

        if (parentRefModel.getProperties() != null) {
          childProperties.putAll(parentRefModel.getProperties());
        }

        classMembers = convertModelPropertiesToClassMembers(childProperties,
            modelsMap.get(refModel.getSimpleRef()), modelsMap);

        // If the parent model also has AllOf references -- meaning it's a child of some other superclass
        // then we need to recurse to get the grandparent's properties and add them to our current classes
        // derived property list
        if (parentRefModel instanceof ComposedModel) {
          ComposedModel parentRefComposedModel = (ComposedModel) parentRefModel;
          // Use visited to mark which classes we've processed -- this is just to avoid
          // an infinite loop in case there's a circular reference in the class hierarchy.
          if (!visited.contains(parentRefComposedModel)) {
            ancestorMembers = getClassMembers(parentRefComposedModel, modelsMap, visited);
            classMembers.addAll(ancestorMembers);
          }
        }
      }
    }

    visited.add(composedModel);
    LOGGER.exiting(LOGGER.getName(), "getClassMembers-ComposedModel-DeepNest");
    return classMembers;
  }

  private List<ClassMembers> getClassMembers(ModelImpl model, Map<String, Model> modelsMap) {
    LOGGER.entering(LOGGER.getName(), "getClassMembers-ModelImpl");

    List<ClassMembers> classMembers = new ArrayList<>();

    Map<String, Property> modelMembers = model.getProperties();
    if (modelMembers != null && !modelMembers.isEmpty()) {
      classMembers.addAll(convertModelPropertiesToClassMembers(modelMembers, model, modelsMap));
    } else {
      Property modelAdditionalProps = model.getAdditionalProperties();

      if (modelAdditionalProps instanceof RefProperty) {
        classMembers.add(getRefClassMembers((RefProperty) modelAdditionalProps));
      }

      if (modelAdditionalProps == null) {
        List<String> enumValues = model.getEnum();

        if (enumValues != null && !enumValues.isEmpty()) {
          classMembers.addAll(getEnum(enumValues));
        }
      }
    }

    LOGGER.exiting(LOGGER.getName(), "getClassMembers-ModelImpl");

    return classMembers;
  }

  private ClassMembers getRefClassMembers(RefProperty refProperty) {
    LOGGER.entering(LOGGER.getName(), "getRefClassMembers");
    ClassMembers classMember = new ClassMembers();
    classMember.setClassName(refProperty.getSimpleRef());
    classMember.setName(" ");

    if (includeCardinality) {
      classMember.setCardinality(CARDINALITY_NONE_TO_MANY);
    }

    LOGGER.exiting(LOGGER.getName(), "getRefClassMembers");
    return classMember;
  }

  private List<ClassMembers> getEnum(List<String> enumValues) {
    LOGGER.entering(LOGGER.getName(), "getEnum");

    List<ClassMembers> classMembers = new ArrayList<>();

    if (enumValues != null && !enumValues.isEmpty()) {
      for (String enumValue : enumValues) {
        ClassMembers classMember = new ClassMembers();
        classMember.setName(enumValue);
        classMembers.add(classMember);
      }
    }

    LOGGER.exiting(LOGGER.getName(), "getEnum");
    return classMembers;
  }

  private List<ClassMembers> convertModelPropertiesToClassMembers(Map<String, Property> modelMembers,
                                                                  Model modelObject, Map<String, Model> models) {
    LOGGER.entering(LOGGER.getName(), "convertModelPropertiesToClassMembers");

    List<ClassMembers> classMembers = new ArrayList<>();

    for (Map.Entry<String, Property> modelMapObject : modelMembers.entrySet()) {
      String variablName = modelMapObject.getKey();

      ClassMembers classMemberObject = new ClassMembers();
      Property property = modelMembers.get(variablName);

      if (property instanceof ArrayProperty) {
        classMemberObject = getClassMember((ArrayProperty) property, modelObject, models, variablName);
      } else if (property instanceof RefProperty) {
        classMemberObject = getClassMember((RefProperty) property, models, modelObject, variablName);
      } else {
        classMemberObject.setDataType(
            getDataType(property.getFormat() != null ? property.getFormat() : property.getType(), false));
        classMemberObject.setName(variablName);
      }

      classMembers.add(classMemberObject);
    }

    LOGGER.exiting(LOGGER.getName(), "convertModelPropertiesToClassMembers");
    return classMembers;
  }

  private ClassMembers getClassMember(ArrayProperty property, Model modelObject, Map<String, Model> models,
                                      String variablName) {
    LOGGER.entering(LOGGER.getName(), "getClassMember-ArrayProperty");

    ClassMembers classMemberObject = new ClassMembers();
    Property propObject = property.getItems();

    if (propObject instanceof RefProperty) {
      classMemberObject = getClassMember((RefProperty) propObject, models, modelObject, variablName);
    } else if (propObject instanceof StringProperty) {
      classMemberObject = getClassMember((StringProperty) propObject, variablName);
    }

    LOGGER.exiting(LOGGER.getName(), "getClassMember-ArrayProperty");
    return classMemberObject;
  }

  private ClassMembers getClassMember(StringProperty stringProperty, String variablName) {
    LOGGER.entering(LOGGER.getName(), "getClassMember-StringProperty");

    ClassMembers classMemberObject = new ClassMembers();
    classMemberObject.setDataType(getDataType(stringProperty.getType(), true));
    classMemberObject.setName(variablName);

    LOGGER.exiting(LOGGER.getName(), "getClassMember-StringProperty");
    return classMemberObject;
  }

  private ClassMembers getClassMember(RefProperty refProperty, Map<String, Model> models, Model modelObject,
                                      String variableName) {
    LOGGER.entering(LOGGER.getName(), "getClassMember-RefProperty");

    ClassMembers classMemberObject = new ClassMembers();
    classMemberObject.setDataType(getDataType(refProperty.getSimpleRef(), true));
    classMemberObject.setName(variableName);

    if (models.containsKey(refProperty.getSimpleRef())) {
      classMemberObject.setClassName(refProperty.getSimpleRef());
    }

    if (includeCardinality && StringUtils.isNotEmpty(variableName) && modelObject != null) {
      if (isRequiredProperty(modelObject, variableName)) {
        classMemberObject.setCardinality(CARDINALITY_ONE_TO_MANY);
      } else {
        classMemberObject.setCardinality(CARDINALITY_NONE_TO_MANY);
      }
    }

    LOGGER.exiting(LOGGER.getName(), "getClassMember-RefProperty");
    return classMemberObject;
  }

  private boolean isRequiredProperty(Model modelObject, String propertyName) {
    boolean isRequiredProperty = false;
    LOGGER.entering(LOGGER.getName(), "isRequiredProperty");

    if (modelObject != null) {
      if (modelObject instanceof ModelImpl) {
        List<String> requiredProperties = ((ModelImpl) modelObject).getRequired();
        if (requiredProperties != null && !requiredProperties.isEmpty()) {
          isRequiredProperty = requiredProperties.contains(propertyName);
        }
      }
    }

    LOGGER.exiting(LOGGER.getName(), "isRequiredProperty");
    return isRequiredProperty;
  }

  private String getDataType(String className, boolean isArray) {
    if (isArray) {
      return FormatUtility.toTitleCase(className) + "[]";
    }

    return FormatUtility.toTitleCase(className);
  }

}
