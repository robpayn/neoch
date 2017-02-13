package org.payn.neoch.io.xmltools;

import java.io.File;

import org.payn.chsm.io.xml.ElementHelper;
import org.w3c.dom.Element;

/**
 * Element helper containint information about cell and boundary input
 * 
 * @author robpayn
 *
 */
public class ElementXMLInput extends ElementHelper {
   
   /**
    * Tag name for the element
    */
   public static final String TAG_NAME = "xmlinput";

   /**
    * Input directory
    */
   private File inputDirectory;

   /**
    * Constructor based on the provided input XML element and a working directory
    * 
    * @param element
    * @param workingDir
    * @throws Exception
    */
   public ElementXMLInput(Element element, File workingDir) throws Exception 
   {
      super(element);
      getInputDirectory(workingDir);
   }

   /**
    * Check if the "from working directory" flag is set
    * 
    * @return
    *       true if set, false otherewise
    */
   public boolean isFromWorkingDir() 
   {
      
      return Boolean.valueOf(getAttribute("fromWorkingDir"));
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
    * Get the input directory based on working directory flag
    * 
    * @param workingDir
    *       working directory
    * @throws Exception
    */
   private void getInputDirectory(File workingDir) throws Exception
   {
      if (isFromWorkingDir())
      {
         inputDirectory = workingDir;
      }
      else
      {
         inputDirectory = new File(getAttribute("path")); 
      }
      if (!inputDirectory.isDirectory())
      {
         throw new Exception("RawXML source must be a directory.");
      }
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
