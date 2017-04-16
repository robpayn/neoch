package org.payn.neoch;

import java.io.File;
import org.payn.chsm.Holon;
import org.payn.chsm.ModelBuilder;
import org.payn.chsm.ModelBuilderXML;
import org.payn.chsm.ModelLoader;
import org.payn.chsm.io.xmltools.ElementBehavior;
import org.payn.chsm.io.xmltools.ElementHolon;
import org.payn.neoch.io.xmltools.ElementBoundary;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Builds a matrix based on information in an XML file
 * 
 * @author robpayn
 *
 */
public class MatrixBuilderXML extends ModelBuilderXML {
   
   /**
    * Main entry point.  Construct a NEOCH model and execute.
    * Model will be configured based on the loader and configuration
    * specified in the command line.
    * 
    * @param args
    *       command line arguments
    */
   public static void main(String[] args)
   {
      try 
      {
         ModelBuilder builder = ModelLoader.loadBuilder(
               new File(System.getProperty("user.dir")),
               args
               );
         HolonMatrix matrix = (HolonMatrix)builder.buildModel();
         matrix.getController().initializeController();
         matrix.getController().executeController();
      } 
      catch (Exception e) 
      {
         e.printStackTrace();
      }
   }
   
   @Override
   protected Holon createHolon(String name, Holon parentHolon) throws Exception 
   {
      return new HolonMatrix(name, this, controller);
   }

   @Override
   protected void installHolons(ElementHolon element, Holon holon) throws Exception
   {
      super.installHolons(element, holon);
      
      loggerManager.statusUpdate(String.format(
            "   Installing cells in %s...",
            holon
            ));
      NodeList nodes = 
            element.getFirstChildElement("cells").getElementsByTagName("cell");
      for(int i = 0; i < nodes.getLength(); i++)
      {
         ElementHolon childElement = new ElementHolon((Element)nodes.item(i));
         new HolonCell(childElement.getName(), (HolonMatrix)holon);
      }
      
      loggerManager.statusUpdate(String.format(
            "   Installing boundaries in %s...",
            holon
            ));
      nodes = 
            element.getFirstChildElement("boundaries").getElementsByTagName("boundary");
      for(int i = 0; i < nodes.getLength(); i++)
      {
         ElementBoundary childBoundaryElement = 
               new ElementBoundary((Element)nodes.item(i));
         HolonBoundary childBoundary = new HolonBoundary(
               childBoundaryElement.getName(), 
               childBoundaryElement.getCellName(),
               (HolonMatrix)holon
               );
 
         // Install the adjacent boundary if specified.
         ElementBoundary adjChildBoundaryElement = 
               childBoundaryElement.getAdjacentBoundElement();
         if (adjChildBoundaryElement.getElement() != null)
         {
            HolonBoundary adjBoundary = new HolonBoundary(
                  adjChildBoundaryElement.getName(), 
                  adjChildBoundaryElement.getCellName(),
                  (HolonMatrix)holon
                  );
            childBoundary.setAdjacentBoundary(adjBoundary);
            adjBoundary.setAdjacentBoundary(childBoundary);
         }
      }
   }
   
   @Override
   protected void installBehaviors(ElementHolon element, Holon holon) throws Exception
   {
      super.installBehaviors(element, holon);
      
      loggerManager.statusUpdate(
            "   Installing behaviors in cells and setting initial values..."
            );
      NodeList nodes = 
            element.getFirstChildElement("cells").getElementsByTagName("cell");
      for(int i = 0; i < nodes.getLength(); i++)
      {
         ElementHolon childElement = new ElementHolon((Element)nodes.item(i));
         HolonCell childHolon = ((HolonMatrix)holon).getCell(childElement.getName());
         for (ElementBehavior elementBehavior: childElement.getBehaviorList())
         {
            loadBehavior(
                  childHolon, 
                  elementBehavior, 
                  defaultBehaviorMap.get(elementBehavior.getFullBehaviorName()),
                  behaviorTableMap.get(elementBehavior.getFullBehaviorName())
                  );
         }
      }
      
      loggerManager.statusUpdate(
            "   Installing behaviors in boundaries and setting initial values..."
            );
      nodes = 
            element.getFirstChildElement("boundaries").getElementsByTagName("boundary");
      for(int i = 0; i < nodes.getLength(); i++)
      {
         ElementBoundary childBoundaryElement = 
               new ElementBoundary((Element)nodes.item(i));
         HolonBoundary childBoundary = 
               ((HolonMatrix)holon).getBoundary(childBoundaryElement.getName());
         
         // Install behaviors and assign specified initial values for state variables.
         for(ElementBehavior elementBehavior: childBoundaryElement.getBehaviorList())
         {
            loadBehavior(
                  childBoundary, 
                  elementBehavior, 
                  defaultBehaviorMap.get(elementBehavior.getFullBehaviorName()),
                  behaviorTableMap.get(elementBehavior.getFullBehaviorName())
                  );
         }
         
         // Install the adjacent boundary if specified.
         ElementBoundary adjBoundElem = 
               childBoundaryElement.getAdjacentBoundElement();
         if (adjBoundElem.getElement() != null)
         {
            HolonBoundary adjBoundary = childBoundary.getAdjacentBoundary();
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

}
