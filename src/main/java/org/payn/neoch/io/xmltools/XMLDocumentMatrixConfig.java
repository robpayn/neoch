package org.payn.neoch.io.xmltools;

import java.io.File;
import org.payn.chsm.io.xml.ElementHolon;
import org.payn.chsm.io.xml.XMLDocumentModelConfig;
import org.payn.neoch.HolonMatrix;

/**
 * XML document for configuration information for a NEOCH simulation
 * 
 * @author robpayn
 *
 */
public class XMLDocumentMatrixConfig extends XMLDocumentModelConfig {
   
   /**
    * Element containing the base holon information
    */
   private ElementHolon holonElem;

   /**
    * Get a holon helper element for the root element
    * 
    * @return
    *       holon element
    */
   public ElementHolon getHolonElement() 
   {
      return holonElem;
   }

   /**
    * Element containing information about the builder
    */
   private ElementBuilder builderElem;

   /**
    * Get the builder element
    * 
    * @return
    *       Element for the builder
    */
   public ElementBuilder getBuilderElement() 
   {
      return builderElem;
   }

   /**
    * Create the config document based on the provided xml file
    * 
    * @param configFile
    *       xml file
    * @throws Exception
    *       if error in reading xml file
    */
   public XMLDocumentMatrixConfig(File configFile) throws Exception 
   {
      super(configFile);
      builderElem = new ElementBuilder(rootElementHelper.getFirstChildElement(ElementBuilder.TAG_NAME));
      holonElem = new ElementHolon(rootElementHelper.getFirstChildElement(ElementHolonMatrix.TAG_NAME));
   }

   /**
    * Get the name attribute of the document element
    * 
    * @return
    *       name of the document element
    */
   public String getHolonName() 
   {
      return holonElem.getName();
   }

   /**
    * Add the behaviors in the matrix holon
    * 
    * @param matrix
    */
   public void changeRootBehaviors(HolonMatrix matrix) 
   {
      holonElem.clearBehaviorElements();
      holonElem.createBehaviorElements(matrix);
   }

}
