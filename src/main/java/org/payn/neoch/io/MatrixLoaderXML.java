package org.payn.neoch.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.payn.chsm.Behavior;
import org.payn.chsm.Holon;
import org.payn.chsm.ModelLoader;
import org.payn.chsm.Resource;
import org.payn.chsm.io.OutputHandlerFactory;
import org.payn.chsm.io.OutputHandlerFactoryXML;
import org.payn.chsm.io.xml.ElementBehavior;
import org.payn.chsm.io.xml.ElementOutput;
import org.payn.chsm.io.xml.ElementProcessor;
import org.payn.chsm.io.xml.ElementResource;
import org.payn.chsm.processors.ControllerHolon;
import org.payn.neoch.MatrixBuilder;
import org.payn.neoch.MatrixLoader;
import org.payn.neoch.io.xmltools.ElementBuilder;
import org.payn.neoch.io.xmltools.ElementLogger;
import org.payn.neoch.io.xmltools.XMLDocumentConfig;

/**
 * A matrix loader based on information in an XML file.
 * 
 * @author robpayn
 *
 */
public class MatrixLoaderXML extends MatrixLoader {

   /**
    * XML document containing the configuration
    */
   private XMLDocumentConfig documentConfig;
   
   /**
    * Path to the root
    */
   private String pathRoot;

   /**
    * Setter for the configuration document
    * 
    * @param documentConfig
    *       configuration document
    */
   private void setDocument(XMLDocumentConfig documentConfig) 
   {
      this.documentConfig = documentConfig;
   }

   /**
    * Getter for the configuration document
    * 
    * @return
    *       configuration document
    */
   public XMLDocumentConfig getDocument()
   {
      return documentConfig;
   }
   
   @Override
   protected void initializeInputs() throws Exception
   {
      // Check for configuration file in file system
      if (!argMap.containsKey("config"))
      {
         throw new Exception(
               "Must provide an argument for configuration file relative to working directory " +
                     "(e.g. 'config=./config/config.xml')"
               );
      }
      File configFile = new File(
            workingDir.getAbsolutePath() + argMap.get("config")
            );
      if (!configFile.exists() || configFile.isDirectory()) 
      {
         throw new Exception(String.format(
               "%s is an invalid configuration file.", 
               configFile.getAbsolutePath()
               ));
      }
      
      // Parse the XML configuration file
      XMLDocumentConfig documentConfig = new XMLDocumentConfig(configFile);
      setDocument(documentConfig);
      pathRoot = documentConfig.getPathRoot();
   }

   @Override
   protected void initializeLoggers() throws Exception {
      Iterator<ElementLogger> loggerIter = documentConfig.getLoggerIterator();
      if (loggerIter != null)
      {
         loggerList.clear();
         while (loggerIter.hasNext())
         {
            ElementLogger loggerElem = loggerIter.next();
            File loggerFile = loggerElem.getFile(pathRoot);
            String classPath = loggerElem.getClassPath();
            loggerList.add(MatrixLoader.loadClass(
                  getClass().getClassLoader(), loggerFile, classPath
                  ));
         }
      }
   }

   @Override
   protected MatrixBuilder createBuilder() throws Exception 
   {
      ElementBuilder builderElem = documentConfig.getBuilderElement();
      String classPath = builderElem.getClassPath();
      if (!classPath.equals(""))
      {
         return (MatrixBuilder)ModelLoader.createObjectInstance(
               getClass().getClassLoader(), 
               builderElem.getFile(pathRoot), 
               classPath,
               String.format("Builder %s", classPath)
               );
      }
      else
      {
         return null;
      }
   }

   @Override
   protected ControllerHolon getController() throws Exception 
   {
      ElementProcessor procElem = documentConfig.getProcessorElement();
      if (procElem != null)
      {
         String classPath = procElem.getClassPath();
         if (!classPath.equals(""))
         {
            return (ControllerHolon)ModelLoader.createObjectInstance(
                  getClass().getClassLoader(), 
                  procElem.getFile(pathRoot), 
                  classPath,
                  String.format("Controller %s", classPath)
                  );
         }
      }
      return null;
   }

   @Override
   protected ArrayList<Resource> getResources() throws Exception 
   {
      Iterator<ElementResource> resourceIter = documentConfig.getResourceIterator();
      ArrayList<Resource> list = new ArrayList<Resource>();
      while (resourceIter.hasNext())
      {
         ElementResource resourceElem = resourceIter.next();
         if (resourceElem.isActive())
         {
            Resource resource = getResource(resourceElem);
            resource.initialize(resourceElem.getName());
            list.add(resource);
         }
      }
      return list;
   }

   /**
    * Get the resource object appropriate for the element
    * 
    * @param resourceElem
    * @return
    * @throws Exception
    */
   protected Resource getResource(ElementResource resourceElem) throws Exception 
   {
      String classPath = resourceElem.getClassPath();
      if (!classPath.equals(""))
      {
         return (Resource)MatrixLoader.createObjectInstance(
               getClass().getClassLoader(), 
               resourceElem.getFile(pathRoot), 
               classPath, 
               String.format("Resource %s", classPath)
               );
      }
      else
      {
         return null;
      }
   }

   @Override
   protected ArrayList<OutputHandlerFactory<?,?>> getOutputHandlerFactories() throws Exception 
   {
      // Create the output handlers and set their configurations
      Iterator<ElementOutput> outputIter = documentConfig.getOutputIterator();
      ArrayList<OutputHandlerFactory<?,?>> list = new ArrayList<OutputHandlerFactory<?,?>>();
      while (outputIter.hasNext())
      {
         ElementOutput outputElem = outputIter.next();
         if (outputElem.isActive())
         {
            OutputHandlerFactoryXML<?> outputHandlerFactory = getOutputHandlerFactory(outputElem);
            outputHandlerFactory.setConfig(outputElem);
            outputHandlerFactory.setLogger(loggerManager);
            list.add(outputHandlerFactory);
         }
      }
      return list;
   }

   /**
    * Get a configured output handler factory
    * 
    * @param outputElem
    * @return
    * @throws Exception
    */
   protected OutputHandlerFactoryXML<?> getOutputHandlerFactory(ElementOutput outputElem) throws Exception 
   {
      String classPath = outputElem.getClassPath();
      if (!classPath.equals(""))
      {
         return (OutputHandlerFactoryXML<?>)createObjectInstance(
            getClass().getClassLoader(), 
            outputElem.getFile(pathRoot), 
            classPath, 
            String.format("Output handler factory %s", classPath)
            );
      }
      else
      {
         return null;
      }
   }

   /**
    * Get the behavior object associated with the element
    * 
    * @param behaviorElem
    * @return
    * @throws Exception
    */
   protected Behavior getBehavior(ElementBehavior behaviorElem) throws Exception 
   {
      String classPath = behaviorElem.getClassPath();
      if (!classPath.equals(""))
      {
         return (Behavior)ModelLoader.createObjectInstance(
               Holon.class.getClassLoader(), 
               behaviorElem.getFile(pathRoot), 
               classPath, 
               String.format("Behavior %s", classPath)
               );
      }
      else
      {
         return null;
      }
   }

}
