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

  public String createPlantUmlFile(File targetLocation, Map<String, Object> additionalProperties)
      throws IOException, IllegalAccessException {
    String plantUmlFilePath = targetLocation.getAbsolutePath() +
        ( targetLocation.isFile() ? "" : File.separator + "swagger.puml");

    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache mustache = mf.compile("puml.mustache");
    Writer writer = null;
    try {
      writer = new FileWriter(plantUmlFilePath);
      mustache.execute(writer, additionalProperties);

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
}
