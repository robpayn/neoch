package org.payn.neoch;

import java.io.File;
import java.util.Iterator;

import org.payn.chsm.Behavior;
import org.payn.chsm.Holon;
import org.payn.chsm.Resource;
import org.payn.chsm.io.xml.ElementBehavior;
import org.payn.chsm.io.xml.ElementHolon;
import org.payn.chsm.io.xml.ElementInitValue;
import org.payn.neoch.io.xmltools.DocumentBoundary;
import org.payn.neoch.io.xmltools.DocumentCell;
import org.payn.neoch.io.xmltools.ElementBehaviorMatrix;
import org.payn.neoch.io.xmltools.ElementBoundary;
import org.payn.neoch.io.xmltools.ElementBuilder;
import org.payn.neoch.io.xmltools.ElementHolonMatrix;
import org.payn.neoch.io.xmltools.ElementXMLInput;
import org.payn.neoch.io.xmltools.XMLDocumentMatrixConfig;

/**
 * Builds a NEO lite matrix based on XML input. 
 * 
 * @author robpayn
 *
 */
public class MatrixBuilderXML extends MatrixBuilder {

   /**
    * XML element with builder information
    */
   private ElementBuilder element;

   /**
    * XML document with cell data
    */
   private DocumentCell cellDoc;

   /**
    * XML document with boundary data
    */
   private DocumentBoundary boundDoc;

   @Override
   public void initializeBuildSources() throws Exception 
   {
      ElementXMLInput inputElem = element.getXMLInputElement(workingDir);
      cellDoc = new DocumentCell(inputElem.getCellFile());
      boundDoc = new DocumentBoundary(inputElem.getBoundaryFile());
   }

   @Override
   protected void installCells() throws Exception 
   {
      Iterator<ElementHolonMatrix> cellIter = cellDoc.iterator();
      while (cellIter.hasNext())
      {
         ElementHolonMatrix cellElem = cellIter.next();
         createCell(cellElem.getName());
      }
   }

   @Override
   protected void installBoundaries() throws Exception 
   {
      Iterator<ElementBoundary> boundIter = boundDoc.iterator();
      while (boundIter.hasNext())
      {
         ElementBoundary boundElem = boundIter.next();
         HolonBoundary boundary = createBoundary(boundElem.getName(), boundElem.getCellName());
         
         // Install the adjacent boundary if specified.
         ElementBoundary adjBoundElem = boundElem.getAdjacentBoundElement();
         if (adjBoundElem.getElement() != null)
         {
            createBoundary(adjBoundElem.getName(), adjBoundElem.getCellName(), boundary);
         }
      }
   }

   @Override
   protected void installCellBehaviors() throws Exception 
   {
      Iterator<ElementHolonMatrix> cellIter = cellDoc.iterator();
      while (cellIter.hasNext())
      {
         ElementHolonMatrix cellElem = cellIter.next();
         HolonCell newCell = holon.getCell(cellElem.getName());
         Iterator<ElementBehaviorMatrix> behaviorIter = cellElem.iterator();
         while (behaviorIter.hasNext())
         {
            loadBehavior(newCell, behaviorIter.next());
         }
      }
   }

   @Override
   protected void installBoundaryBehaviors() throws Exception 
   {
      Iterator<ElementBoundary> boundIter = boundDoc.iterator();
      while (boundIter.hasNext())
      {
         ElementBoundary boundElem = boundIter.next();
         HolonBoundary boundary = holon.getBoundary(boundElem.getName());
         
         // Install the adjacent boundary if specified.
         ElementBoundary adjBoundElem = boundElem.getAdjacentBoundElement();
         HolonBoundary adjBoundary = null;
         if (adjBoundElem.getElement() != null)
         {
            adjBoundary = holon.getBoundary(adjBoundElem.getName());
         }
         
         // Install behaviors and assign specified initial values for state variables.
         Iterator<ElementBehaviorMatrix> behaviorIter = boundElem.iterator();
         while(behaviorIter.hasNext())
         {
            ElementBehaviorMatrix behaviorElem = behaviorIter.next();
            loadBehavior(boundary, behaviorElem);
         }
         
         // Install asymmetric behaviors in adjacent boundary
         if (adjBoundary != null)
         {
            behaviorIter = adjBoundElem.iterator();
            while(behaviorIter.hasNext())
            {
               ElementBehaviorMatrix behaviorElem = behaviorIter.next();
               loadBehavior(adjBoundary, behaviorElem);
            }
         }
      }
   }

   /**
    * Load a matrix type behavior in the provided holon
    * 
    * @param targetHolon
    *       holon in which to load the behavior
    * @param element
    *       XML element containing information about the behavior
    * @throws Exception
    *       if error in loading behavior
    */
   private Behavior loadBehavior(Holon targetHolon, ElementBehaviorMatrix element) 
         throws Exception 
   {
      Behavior behavior = getBehaviorFromResource(element.getResourceName(), element.getName());
      loadInitValues(targetHolon, behavior, element);
      if (element.isInstalled())
      {
         addBehavior(targetHolon, behavior);
         targetHolon.addInstalledBehavior(behavior);
      }
      else
      {
         Resource resource = resourceMap.get(element.getResourceName());
         behavior = resource.getBehavior(element.getName());
      }
//      loadInitValues(targetHolon, behavior, element);
      return behavior;
   }
   
   /**
    * Load the initial values in the provided behavior element
    * 
    * @param targetHolon
    *       holon in which initial values are loaded
    * @param behavior
    *       behavior for the states in which initial values are loaded
    * @param element
    *       behavior element with initial value information
    * @throws Exception
    *       if error in loading initial values
    */
   private void loadInitValues(Holon targetHolon, Behavior behavior,
         ElementBehavior element) throws Exception 
   {
      Iterator<ElementInitValue> initValIter = element.iterator();
      while (initValIter.hasNext())
      {
         ElementInitValue initValElem = initValIter.next();
         initializeValue(
               targetHolon,
               behavior,
               initValElem.getStateVariableName(),
               initValElem.getValue(),
               initValElem.getTypeAlias()
               );
      }
   }

   @Override
   protected HolonMatrix buildMatrix() throws Exception 
   {
      // Check for valid configuration file
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
      XMLDocumentMatrixConfig document = new XMLDocumentMatrixConfig(configFile);
      element = document.getBuilderElement();

      // Create the matrix and install global behaviors (time behavior required)
      loggerManager.statusUpdate("Creating the matrix holon and installing global behaviors...");
      ElementHolon holonElement = document.getHolonElement();
      
      setHolon(new HolonMatrix(holonElement.getName(), this));
      Iterator<?> behaviorIter = document.getHolonElement().iterator();
      while (behaviorIter.hasNext())
      {
         ElementBehavior behaviorElem = (ElementBehavior)behaviorIter.next();
         Behavior behavior = resourceMap.get(behaviorElem.getResourceName())
               .getBehavior(behaviorElem.getName());
         if (behaviorElem.isInstalled())
         {
            addBehavior(holon, behavior);
            holon.addInstalledBehavior(behavior);
         }
         loadInitValues(holon, behavior, behaviorElem);
         loggerManager.statusUpdate(String.format(
               "   Installed behavior %s", 
               behavior.getName()
               ));
      }

      // Build the cells and boundaries in the matrix
      loggerManager.statusUpdate("Building the matrix...");
      holon.setProcessor(controller);
      
      return holon;
   }

}
