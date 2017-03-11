package org.payn.neoch.io.xmltools;

import java.io.File;

import org.payn.chsm.io.xml.ElementXMLInput;
import org.w3c.dom.Element;

/**
 * Element helper containing information about cell and boundary input
 * 
 * @author robpayn
 *
 */
public class ElementXMLInputMatrix extends ElementXMLInput {
   
   /**
    * Constructor based on the provided input XML element and a working directory
    * 
    * @param element
    * @param workingDir
    * @throws Exception
    */
   public ElementXMLInputMatrix(Element element, File workingDir) throws Exception 
   {
      super(element, workingDir);
   }

   /**
    * Get the cell file form the NEO configuration
    * 
    * @return
    *       cell file
    * @throws Exception
    */
   public File getCellFile() throws Exception 
   {
      return new File(
            inputDirectory.getAbsolutePath() + File.separator + getCellFilePath()  
            );
   }
   
   /**
    * Get the cell file path from the NEO configuration
    * 
    * @return
    *       cell file path
    */
   private String getCellFilePath() 
   {
      return this.getFirstChildElementHelper("files").getAttribute("cellFile");
   }

   /**
    * Get the boundary file from the NEO configuration
    * 
    * @return
    *       boundary file
    * @throws Exception
    */
   public File getBoundaryFile() throws Exception 
   {
      return new File(
            inputDirectory.getAbsolutePath() + File.separator + getBoundaryFilePath()  
            );
   }

   /**
    * Get the boundary file path from the NEO configuration
    * 
    * @return
    *       boundary file path
    */
   private String getBoundaryFilePath() 
   {
      return this.getFirstChildElementHelper("files").getAttribute("boundaryFile");
   }

}
