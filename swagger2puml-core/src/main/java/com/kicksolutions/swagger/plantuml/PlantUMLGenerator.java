package com.kicksolutions.swagger.plantuml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.SourceFileReader2;

/**
 * MSANTOSH
 *
 */
public class PlantUMLGenerator
{
	private static final Logger LOGGER = Logger.getLogger(PlantUMLGenerator.class.getName());

	public PlantUMLGenerator() {
		super();
	}

	/**
	 *
	 * @param specFile
	 * @param output
	 */
	public void transformSwagger2Puml(String specFile,String output,boolean generateDefinitionModelOnly,boolean includeCardinality,boolean generateSvg){
		LOGGER.entering(LOGGER.getName(), "transformSwagger2Puml");

		File swaggerSpecFile = new File(specFile);
		File targetLocation = new File(output);

		if(swaggerSpecFile.exists() && !swaggerSpecFile.isDirectory()
				&& targetLocation.exists() && targetLocation.isDirectory()) {

			Swagger swaggerObject = new SwaggerParser().read(swaggerSpecFile.getAbsolutePath());
			PlantUMLCodegen codegen = new PlantUMLCodegen(swaggerObject, targetLocation, generateDefinitionModelOnly, includeCardinality);
			String pumlPath = null;

			try{
				LOGGER.info("Processing File --> "+ specFile);
				pumlPath = codegen.generatePuml();
				LOGGER.info("Sucessfully Create PUML !!!");

				if(generateSvg)
				{
					generateUmlDiagramNonThread(pumlPath, targetLocation);
				}
			}
			catch(Exception e){
				LOGGER.log(Level.SEVERE, e.getMessage(),e);
				throw new RuntimeException(e);
			}
		}else{
			throw new RuntimeException("Spec File or Ouput Locations are not valid");
		}

		LOGGER.exiting(LOGGER.getName(), "transformSwagger2Puml");
	}

	private void generateUmlDiagramNonThread(String plantUmlFilePath, File targetOutputFile) throws Exception {
		SourceFileReader sourceFileReader = new SourceFileReader(new File(plantUmlFilePath));
		sourceFileReader.setFileFormatOption(new FileFormatOption(FileFormat.SVG));
		List<GeneratedImage> list = sourceFileReader.getGeneratedImages();
	}
}