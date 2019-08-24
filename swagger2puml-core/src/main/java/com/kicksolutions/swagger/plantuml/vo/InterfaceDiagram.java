package com.kicksolutions.swagger.plantuml.vo;

import java.util.List;

/**
 * 
 * @author MSANTOSH
 *
 */
public class InterfaceDiagram {
	
	private String interfaceName;
	private List<MethodDefinitions> methods;
	private List<ClassRelation> childClass;
	private List<String> errorClasses;

	public InterfaceDiagram() {
		super();
	}

	public InterfaceDiagram(String interfaceName, List<MethodDefinitions> methods,
			List<ClassRelation> childClass,List<String> errorClasses) {
		super();
		this.interfaceName = interfaceName;
		this.methods = methods;
		this.childClass = childClass;
		this.errorClasses = errorClasses;
	}

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

	public List<ClassRelation> getChildClass() {
		return childClass;
	}

	public void setChildClass(List<ClassRelation> childClass) {
		this.childClass = childClass;
	}

	public List<String> getErrorClasses() {
		return errorClasses;
	}

	public void setErrorClasses(List<String> errorClasses) {
		this.errorClasses = errorClasses;
	}

	@Override
	public String toString() {
		return "InterfaceDiagram [interfaceName=" + interfaceName + ", methods=" + methods + ", childClass="
				+ childClass + ", errorClasses=" + errorClasses + "]";
	}
}
