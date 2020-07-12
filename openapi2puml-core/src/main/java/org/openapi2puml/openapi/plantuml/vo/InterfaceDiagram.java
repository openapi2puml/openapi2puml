package org.openapi2puml.openapi.plantuml.vo;

import java.util.List;

public class InterfaceDiagram {

  private String interfaceName;
  private List<MethodDefinitions> methods;
  private List<ClassRelation> childClasses;
  private List<String> errorClasses;

  public InterfaceDiagram() {
    super();
  }

  // TODO REMOVE
//  public InterfaceDiagram(String interfaceName, List<MethodDefinitions> methods,
//                          List<ClassRelation> childClasses, List<String> errorClasses) {
//    super();
//    this.interfaceName = interfaceName;
//    this.methods = methods;
//    this.childClasses = childClasses;
//    this.errorClasses = errorClasses;
//  }

  public String getInterfaceName() {
    return interfaceName;
  }

  public void setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
  }

  public List<MethodDefinitions> getMethods() {
    return methods;
  }

  public void setMethods(List<MethodDefinitions> methods) {
    this.methods = methods;
  }

  public List<ClassRelation> getChildClasses() {
    return childClasses;
  }

  public void setChildClasses(List<ClassRelation> childClasses) {
    this.childClasses = childClasses;
  }

  public List<String> getErrorClasses() {
    return errorClasses;
  }

  public void setErrorClasses(List<String> errorClasses) {
    this.errorClasses = errorClasses;
  }

  @Override
  public String toString() {
    return "InterfaceDiagram [interfaceName=" + interfaceName + ", methods=" + methods + ", childClasses="
        + childClasses + ", errorClasses=" + errorClasses + "]";
  }

}
