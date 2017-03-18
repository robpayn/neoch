package org.payn.neoch;

import java.io.File;
import java.util.HashMap;
import org.payn.chsm.Behavior;
import org.payn.chsm.Holon;
import org.payn.chsm.Resource;
import org.payn.chsm.io.initialize.InitialConditionTable;
import org.payn.chsm.io.xmltools.ElementBehavior;
import org.payn.chsm.io.xmltools.ElementBuilder;
import org.payn.chsm.io.xmltools.ElementHolon;
import org.payn.chsm.io.xmltools.ElementInitValue;
import org.payn.chsm.io.xmltools.XMLDocumentModelConfig;
import org.payn.neoch.io.xmltools.DocumentBoundary;
import org.payn.neoch.io.xmltools.DocumentCell;
import org.payn.neoch.io.xmltools.ElementBoundary;
import org.payn.neoch.io.xmltools.ElementXMLInputMatrix;

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
      ElementXMLInputMatrix inputElem = new ElementXMLInputMatrix(
            element.getXMLInputElement(), 
            workingDir
            );
      cellDoc = new DocumentCell(inputElem.getCellFile());
      boundDoc = new DocumentBoundary(inputElem.getBoundaryFile());
   }

   @Override
   protected void installCells() throws Exception 
   {
      for (ElementHolon cellElement: cellDoc.getCellList())
      {
         createCell(cellElement.getName());
      }
   }

   @Override
   protected void installBoundaries() throws Exception 
   {
      for (ElementBoundary elementBoundary: boundDoc.getBoundaryList())
      {
         HolonBoundary boundary = createBoundary(
               elementBoundary.getName(), 
               elementBoundary.getCellName()
               );
         
         // Install the adjacent boundary if specified.
         ElementBoundary adjBoundElem = 
               elementBoundary.getAdjacentBoundElement();
         if (adjBoundElem.getElement() != null)
         {
            createBoundary(
                  adjBoundElem.getName(), 
                  adjBoundElem.getCellName(), boundary
                  );
         }
      }
   }

   @Override
   protected void installCellBehaviors() throws Exception 
   {
      HashMap<String, ElementBehavior> defaultBehaviorMap = cellDoc.getDefaultBehaviorMap();
      HashMap<String, InitialConditionTable> behaviorTableMap = 
            new HashMap<String, InitialConditionTable>();
      for (ElementBehavior elementBehavior: defaultBehaviorMap.values())
      {
         if (elementBehavior.hasInitialConditionConfig())
         {
            InitialConditionTable behaviorTable = InitialConditionTable.getInstance(
                  new File(
                        workingDir 
                        + File.separator 
                        + elementBehavior.getAttributeInitConditionPath()
                        ), 
                  elementBehavior.getAttributeInitConditionDelimiter()
                  );
            behaviorTableMap.put(elementBehavior.getFullBehaviorName(), behaviorTable);
         }
      }
      for (ElementHolon cellElement: cellDoc.getCellList())
      {
         HolonCell newCell = holon.getCell(cellElement.getName());
         for (ElementBehavior elementBehavior: cellElement.getBehaviorList())
         {
            loadBehavior(
                  newCell, 
                  elementBehavior, 
                  defaultBehaviorMap.get(elementBehavior.getFullBehaviorName()),
                  behaviorTableMap.get(elementBehavior.getFullBehaviorName())
                  );
         }
      }
   }

   @Override
   protected void installBoundaryBehaviors() throws Exception 
   {
      HashMap<String, ElementBehavior> defaultBehaviorMap = boundDoc.getDefaultBehaviorMap();
      HashMap<String, InitialConditionTable> behaviorTableMap = 
            new HashMap<String, InitialConditionTable>();
      for (ElementBehavior elementBehavior: defaultBehaviorMap.values())
      {
         if (elementBehavior.hasInitialConditionConfig())
         {
            InitialConditionTable behaviorTable = InitialConditionTable.getInstance(
                  new File(
                        workingDir 
                        + File.separator 
                        + elementBehavior.getAttributeInitConditionPath()
                        ), 
                  elementBehavior.getAttributeInitConditionDelimiter()
                  );
            behaviorTableMap.put(elementBehavior.getFullBehaviorName(), behaviorTable);
         }
      }
      for (ElementBoundary elementBoundary: boundDoc.getBoundaryList())
      {
         HolonBoundary boundary = 
               holon.getBoundary(elementBoundary.getName());
         
         // Install the adjacent boundary if specified.
         ElementBoundary adjBoundElem = 
               elementBoundary.getAdjacentBoundElement();
         HolonBoundary adjBoundary = null;
         if (adjBoundElem.getElement() != null)
         {
            adjBoundary = holon.getBoundary(adjBoundElem.getName());
         }
         
         // Install behaviors and assign specified initial values for state variables.
         for(ElementBehavior elementBehavior: elementBoundary.getBehaviorList())
         {
            loadBehavior(
                  boundary, 
                  elementBehavior, 
                  defaultBehaviorMap.get(elementBehavior.getFullBehaviorName()),
                  behaviorTableMap.get(elementBehavior.getFullBehaviorName())
                  );
         }
         
         // Install asymmetric behaviors in adjacent boundary
         if (adjBoundary != null)
         {
            for (ElementBehavior elementBehavior: adjBoundElem.getBehaviorList())
            {
               loadBehavior(
                     adjBoundary, 
                     elementBehavior, 
                     defaultBehaviorMap.get(elementBehavior.getFullBehaviorName()),
                     behaviorTableMap.get(elementBehavior.getFullBehaviorName())
                     );
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
   private Behavior loadBehavior(
         Holon targetHolon, 
         ElementBehavior element, 
         ElementBehavior defaultElement,
         InitialConditionTable initialConditionTable
         ) throws Exception 
   {
      Behavior behavior = getBehaviorFromResource(element.getResourceName(), element.getName());
      loadInitValues(targetHolon, behavior, element, defaultElement, initialConditionTable);
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
   private void loadInitValues(
         Holon targetHolon, 
         Behavior behavior,
         ElementBehavior element, 
         ElementBehavior defaultElement, 
         InitialConditionTable initialConditionTable
         ) throws Exception 
   {
      if (initialConditionTable != null)
      {
         for (ElementInitValue elementInitValue: defaultElement.getInitValueList())
         {
            if (elementInitValue.getValue().equals(""))
            {
               initializeValue(
                     targetHolon,
                     behavior,
                     elementInitValue.getStateVariableName(),
                     initialConditionTable.find(
                           behavior.getName() + "." + elementInitValue.getStateVariableName(),
                           targetHolon.toString()
                           ),
                     elementInitValue.getTypeAlias()
                     );
            }
         }
      }
      if (defaultElement != null)
      {
         for (ElementInitValue elementInitValue: defaultElement.getInitValueList())
         {
            if (!elementInitValue.getValue().equals(""))
            {
               initializeValue(
                     targetHolon,
                     behavior,
                     elementInitValue.getStateVariableName(),
                     elementInitValue.getValue(),
                     elementInitValue.getTypeAlias()
                     );
            }
         }
      }
      for (ElementInitValue elementInitValue: element.getInitValueList())
      {
         initializeValue(
               targetHolon,
               behavior,
               elementInitValue.getStateVariableName(),
               elementInitValue.getValue(),
               elementInitValue.getTypeAlias()
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
      XMLDocumentModelConfig document = new XMLDocumentModelConfig(configFile);
      element = document.getBuilderElement();

      // Create the matrix and install global behaviors (time behavior required)
      loggerManager.statusUpdate(
            "Building the matrix and installing global behaviors..."
            );
      ElementHolon holonElement = document.getHolonElement();
      holon = new HolonMatrix(holonElement.getName(), this, controller);
      for (ElementBehavior elementBehavior: 
         document.getHolonElement().getBehaviorList())
      {
         Behavior behavior = resourceMap.get(elementBehavior.getResourceName())
               .getBehavior(elementBehavior.getName());
         if (elementBehavior.isInstalled())
         {
            addBehavior(holon, behavior);
            holon.addInstalledBehavior(behavior);
         }
         loadInitValues(holon, behavior, elementBehavior, null, null);
         loggerManager.statusUpdate(String.format(
               "   Installed behavior %s", 
               behavior.getName()
               ));
      }

      // Install the cells and boundaries in the matrix
      loggerManager.statusUpdate("Installing states in the matrix...");
      installStates();
      
      return holon;
   }

}
