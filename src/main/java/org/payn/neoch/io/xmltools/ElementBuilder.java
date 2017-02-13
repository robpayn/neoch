package org.payn.neoch.io.xmltools;

import java.io.File;

import org.payn.chsm.io.xml.ElementHelperLoader;
import org.w3c.dom.Element;

/**
 * XML element specifying characteristics of the matrix builder
 * 
 * @author robpayn
 *
 */
public class ElementBuilder extends ElementHelperLoader {

   /**
    * Tag name for the builder element
    */
   public static final String TAG_NAME = "builder";
   
   /**
    * Create a new instance based on the provided XML element
    * 
    * @param element
    */
   public ElementBuilder(Element element) 
   {
      super(element);
   }

   /**
    * Get the XML input element
    * 
    * @param workingDir 
    *       working directory for the input
    * 
    * @return
    *       XML input element
    * @throws Exception  
    */
   public ElementXMLInput getXMLInputElement(File workingDir) throws Exception 
   {
      return new ElementXMLInput(this.getFirstChildElement(ElementXMLInput.TAG_NAME), workingDir);
   }

}
