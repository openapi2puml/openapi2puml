package org.openapi2puml.openapi.plantuml;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceFileReader;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlantUMLGenerator {
  private static final Logger LOGGER = Logger.getLogger(PlantUMLGenerator.class.getName());

  public PlantUMLGenerator() {
    super();
  }

  public void transformOpenApi2Puml(String specFile, String output, boolean generateDefinitionModelOnly,
                                    boolean includeCardinality, boolean generateSvg) {
    LOGGER.entering(LOGGER.getName(), "transformOpenApi2Puml");

    File swaggerSpecFile = new File(specFile);
    File targetLocation = new File(output);

    if (swaggerSpecFile.exists() && !swaggerSpecFile.isDirectory()
        && targetLocation.exists() && targetLocation.isDirectory()) {

      LOGGER.info("Processing File --> " + specFile);
      Swagger swaggerObject = new SwaggerParser().read(swaggerSpecFile.getAbsolutePath());

      try {
        PlantUMLCodegen codegen = new PlantUMLCodegen(swaggerObject, targetLocation, generateDefinitionModelOnly,
            includeCardinality);
        String pumlPath = codegen.generatePlantUmlFile(swaggerObject);
        LOGGER.info("Successfully Created Plant UML output file!");

        if (generateSvg) {
          generateUmlDiagramFile(pumlPath, targetLocation);
        }
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
        throw new RuntimeException(e);
      }

    } else {
      throw new RuntimeException("Spec File or Output Location are not valid");
    }

    LOGGER.exiting(LOGGER.getName(), "transformOpenApi2Puml");
  }

  private void generateUmlDiagramFile(String plantUmlFilePath, File targetOutputFile) throws Exception {
    SourceFileReader sourceFileReader = new SourceFileReader(new File(plantUmlFilePath), targetOutputFile);
    sourceFileReader.setFileFormatOption(new FileFormatOption(FileFormat.SVG));
    sourceFileReader.getGeneratedImages();
  }
}