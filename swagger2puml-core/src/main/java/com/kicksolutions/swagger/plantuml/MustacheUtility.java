package com.kicksolutions.swagger.plantuml;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MustacheUtility {
  private static final Logger LOGGER = Logger.getLogger(MustacheUtility.class.getName());

  public String createPlantUmlFile(File targetLocation, Map<String, Object> additionalProperties)
      throws IOException, IllegalAccessException{
    String plantUmlFilePath = targetLocation.getAbsolutePath() + File.separator + "swagger.puml";

    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache mustache = mf.compile("puml.mustache");
    Writer writer = null;
    // TODO - HCode file name
    try {
      writer = new FileWriter(plantUmlFilePath);
      mustache.execute(writer, additionalProperties);

      LOGGER.log(Level.FINEST, "Successfully Written Plant UML File: " + plantUmlFilePath);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      throw new IllegalAccessException(e.getMessage());
    } finally {
      if (writer != null) {
        writer.flush();
      }
    }

    return plantUmlFilePath;
  }
}
