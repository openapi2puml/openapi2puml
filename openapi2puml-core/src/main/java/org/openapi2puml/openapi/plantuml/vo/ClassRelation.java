package org.openapi2puml.openapi.plantuml.vo;

import java.util.Objects;

public class ClassRelation {

  private String targetClass;
  // TODO - replace by enum
  private boolean isExtension;
  private boolean isComposition;
  private String cardinality;
  private String sourceClass;

  public ClassRelation() {
  }

  public ClassRelation(String targetClass, boolean isExtension, boolean isComposition, String cardinality,
                       String sourceClass) {
    this.targetClass = targetClass;
    this.isExtension = isExtension;
    this.isComposition = isComposition;
    this.cardinality = cardinality;
    this.sourceClass = sourceClass;
  }

  public String getTargetClass() {
    return targetClass;
  }

  public void setTargetClass(String targetClass) {
    this.targetClass = targetClass;
  }

  public boolean isExtension() {
    return isExtension;
  }

  public void setExtension(boolean isExtension) {
    this.isExtension = isExtension;
  }

  public boolean isComposition() {
    return isComposition;
  }

  public void setComposition(boolean isComposition) {
    this.isComposition = isComposition;
  }

  public String getCardinality() {
    return cardinality;
  }

  public void setCardinality(String cardinality) {
    this.cardinality = cardinality;
  }

  public String getSourceClass() {
    return sourceClass;
  }

  public void setSourceClass(String sourceClass) {
    this.sourceClass = sourceClass;
  }

  @Override
  public String toString() {
    return "ClassRelation [targetClass=" + targetClass + ", isExtension=" + isExtension + ", isComposition="
        + isComposition + ", cardinality=" + cardinality + ", sourceClass=" + sourceClass + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ClassRelation)) return false;
    ClassRelation that = (ClassRelation) o;
    return isExtension() == that.isExtension() &&
        isComposition() == that.isComposition() &&
        getTargetClass().equals(that.getTargetClass()) &&
        Objects.equals(getCardinality(), that.getCardinality()) &&
        Objects.equals(getSourceClass(), that.getSourceClass());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTargetClass(), isExtension(), isComposition(), getCardinality(), getSourceClass());
  }
}
