package org.openapi2puml.openapi.plantuml;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;

public class MustacheUtility {
  private static final Logger LOGGER = LogManager.getLogger(MustacheUtility.class);
  private static final String MUSTACHE_TEMPLATE_FILE = "puml.mustache";

  public String createPlantUmlFile(File targetLocation, Map<String, Object> additionalProperties)
      throws IOException, IllegalAccessException {
    String plantUmlFilePath = targetLocation.getAbsolutePath() +
        ( targetLocation.isFile() ? "" : File.separator + "swagger.puml");

    Writer writer = null;
    try {
      writer = new FileWriter(plantUmlFilePath);
      renderMustacheContent(writer, additionalProperties);
      LOGGER.info("Plant UML File created successfully: " + plantUmlFilePath);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalAccessException(e.getMessage());
    } finally {
      if (writer != null) {
        writer.flush();
      }
    }

    return plantUmlFilePath;
  }

  public String createPlantUmlString(File targetLocation, Map<String, Object> additionalProperties)
      throws IOException, IllegalAccessException {

    Writer writer = new StringWriter();
    try {
      renderMustacheContent(writer, additionalProperties);
      LOGGER.info("Plant UML String created successfully");
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalAccessException(e.getMessage());
    }

    return writer.toString();
  }

  private Writer renderMustacheContent(Writer writer, Map<String, Object> context) throws IOException, IllegalAccessException {
    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache mustache = mf.compile(MUSTACHE_TEMPLATE_FILE);

    if(writer != null) {
      try {
        mustache.execute(writer, context);
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
        if (writer != null) {
          writer.flush();
        }
        throw new IllegalAccessException(e.getMessage());
      }
    }

    return writer;
  }
}
