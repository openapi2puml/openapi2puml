package org.openapi2puml.openapi.plantuml.vo;

public class MethodDefinitions {

  private String returnType;
  private String methodDefinition;

  public MethodDefinitions(String returnType, String methodDefinition) {
    super();
    this.returnType = returnType;
    this.methodDefinition = methodDefinition;
  }

  public MethodDefinitions() {
    super();
  }

  public String getReturnType() {
    return returnType;
  }

  public void setReturnType(String returnType) {
    this.returnType = returnType;
  }

  public String getMethodDefinition() {
    return methodDefinition;
  }

  public void setMethodDefinition(String methodDefinition) {
    this.methodDefinition = methodDefinition;
  }

  @Override
  public String toString() {
    return "MethodDefinitions [returnType=" + returnType + ", methodDefinition=" + methodDefinition + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MethodDefinitions))
      return false;
    MethodDefinitions o = (MethodDefinitions)obj;
    return this.returnType.equals(o.returnType) && this.methodDefinition.equals(o.methodDefinition);
  }
}