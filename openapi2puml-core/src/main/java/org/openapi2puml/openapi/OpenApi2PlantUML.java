package org.openapi2puml.openapi;

import org.apache.commons.lang3.StringUtils;
import org.openapi2puml.CliArgs;
import org.openapi2puml.openapi.plantuml.PlantUMLGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OpenApi2PlantUML {
  private static final Logger LOGGER = LogManager.getLogger(OpenApi2PlantUML.class);
  // TODO - rewrite usage
  private static final String USAGE = new StringBuilder()
      .append(" Usage: ")
      .append(OpenApi2PlantUML.class.getName()).append(" <options> \n")
      .append(" -i <spec file> ")
      .append(" -o <output directory> ")
      .append(" -generateDefinitionModelOnly true/false; Default=false ")
      .append(" -includeCardinality true/false; Default=true ")
          .append(" -generateSvg true/false; Default=true ")
          .append(" -generatePng true/false; Default=true ")
          .toString();

  public OpenApi2PlantUML() {

  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    OpenApi2PlantUML openApi2PlantUML = new OpenApi2PlantUML();
    openApi2PlantUML.init(args);
  }

  /**
   * @param args
   */
  private void init(String args[]) {

    CliArgs cliArgs = new CliArgs(args);
    String specFile = cliArgs.getArgumentValue("-i", "");
    String output = cliArgs.getArgumentValue("-o", "");
    boolean generateDefinitionModelOnly = Boolean.parseBoolean(
        cliArgs.getArgumentValue("-generateDefinitionModelOnly", "false"));
    boolean includeCardinality = Boolean.parseBoolean(cliArgs.getArgumentValue("-includeCardinality", "true"));
    boolean generateSvg = Boolean.parseBoolean(cliArgs.getArgumentValue("-generateSvg", "true"));
    boolean generatePng  = Boolean.parseBoolean(cliArgs.getArgumentValue("-generatePng", "true"));
    if (StringUtils.isNotEmpty(specFile) && StringUtils.isNotEmpty(output)) {
      process(specFile, output, generateDefinitionModelOnly, includeCardinality, generateSvg, generatePng);
    } else {
      LOGGER.error(USAGE);
    }
  }

  /**
   * @param specFile
   * @param output
   */
  private void process(String specFile, String output, boolean generateDefinitionModelOnly, boolean includeCardinality,
                       boolean generateSvg, boolean generatePng) {
    PlantUMLGenerator generator = new PlantUMLGenerator();
    generator.transformOpenApi2Puml(specFile, output, generateDefinitionModelOnly, includeCardinality, generateSvg, generatePng);
  }
}
