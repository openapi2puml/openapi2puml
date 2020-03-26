package org.openapi2puml.openapi.plantuml;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;

public class PlantUMLGenerator {
  private static final Logger LOGGER = LogManager.getLogger(PlantUMLGenerator.class);

  public PlantUMLGenerator() {
    super();
  }

  public void transformOpenApi2Puml(String specFile, String output, boolean generateDefinitionModelOnly,
                                    boolean includeCardinality, boolean generateSvg) {
    File swaggerSpecFile = new File(specFile);
    File targetLocation = new File(output);

    // TODO - change so we can handle a named output file too
    if (swaggerSpecFile.exists() && !swaggerSpecFile.isDirectory()
        && targetLocation.exists() && targetLocation.isDirectory()) {

      LOGGER.info("Processing Swagger Spec File: " + specFile);
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
        // TODO - Replace with better error message
        LOGGER.error(e.getMessage(), e);
        throw new RuntimeException(e);
      }

    } else {
      // TODO - better handling than a runtime exception
      throw new RuntimeException("Spec File or Output Location are not valid");
    }
  }

  private void generateUmlDiagramFile(String plantUmlFilePath, File targetOutputFile) throws Exception {
    SourceFileReader sourceFileReader = new SourceFileReader(new File(plantUmlFilePath), targetOutputFile);
    sourceFileReader.setFileFormatOption(new FileFormatOption(FileFormat.SVG));
    sourceFileReader.getGeneratedImages();
  }
}