package org.payn.neoch;

import java.io.File;

import org.payn.chsm.ModelLoader;
import org.payn.chsm.ModelLoaderXML;
import org.payn.neoch.io.xmltools.ElementBuilder;
import org.payn.neoch.io.xmltools.XMLDocumentMatrixConfig;

/**
 * A matrix loader based on information in an XML file.
 * 
 * @author robpayn
 *
 */
public class MatrixLoaderXML extends ModelLoaderXML<MatrixBuilder> {

   @Override
   protected MatrixBuilder createBuilder() throws Exception 
   {
      ElementBuilder builderElem = ((XMLDocumentMatrixConfig)documentConfig).getBuilderElement();
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
   public XMLDocumentMatrixConfig getModelConfigXML(File configFile) throws Exception 
   {
      return new XMLDocumentMatrixConfig(configFile);
   }

   /**
    * Get the XML document
    * 
    * @return
    *       XML document
    */
   public XMLDocumentMatrixConfig getDocument() 
   {
      return (XMLDocumentMatrixConfig)documentConfig;
   }

}
