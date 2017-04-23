package org.payn.neoch.io.reporters;

import java.io.File;
import java.util.HashMap;

import org.payn.chsm.Holon;
import org.payn.chsm.io.ModelLoaderXML;
import org.payn.chsm.io.reporters.ReporterSingleThread;
import org.payn.chsm.io.xmltools.XMLDocumentModelConfig;
import org.payn.neoch.HolonMatrix;
import org.payn.neoch.io.MatrixBuilderXML;
import org.payn.neoch.io.xmltools.DocumentBoundary;
import org.payn.neoch.io.xmltools.DocumentCell;

/**
 * Reporter that creates serializable XML output for the XML builder
 * 
 * This reporter is only compatible with derivatives of the MatrixBuilderXML matrix builder
 * 
 * @author robpayn
 *
 */
public class ReporterXMLSerial extends ReporterSingleThread {
   
   /**
    * Matrix containing the output data
    */
   private HolonMatrix matrix;
   
   /**
    * Cell xml document
    */
   private DocumentCell cellDoc;

   /**
    * Boundary xml document
    */
   private DocumentBoundary boundDoc;

   /**
    * Configuration xml document
    */
   private XMLDocumentModelConfig configDoc;

   /**
    * Buffered iteration count
    */
   private long iteration;

   /**
    * Construct a new instance with the provided working directory and
    * argument map
    * 
    * @param workingDir
    * @param argMap
    */
   public ReporterXMLSerial(File workingDir, HashMap<String, String> argMap) 
   {
      super(workingDir, argMap);
   }

   @Override
   public void initialize(Holon holon) throws Exception
   {
      super.initialize(holon);
      matrix = (HolonMatrix)holon;
   }
   
   /**
    * Open the output location (nothing needed here)
    */
   @Override
   public void openLocation() throws Exception 
   {}

   /**
    * Buffer the output to memory
    */
   @Override
   public void bufferOutput() throws Exception 
   {
      ModelLoaderXML matrixLoader = null;
      try
      {
         matrixLoader = (ModelLoaderXML)matrix.getBuilder().getLoader();
         if (!MatrixBuilderXML.class.isInstance(matrix.getBuilder()))
         {
            throw new Exception();
         }
      }
      catch(Exception e)
      {
         throw new Exception(
               "XML serial outputter requires XML based builder and loader"
               + "");
      }
      configDoc = matrixLoader.getDocument();
      configDoc.getHolonElement().clearBehaviorElements();
      configDoc.getHolonElement().createBehaviorElements(matrix);
      cellDoc = new DocumentCell();
      cellDoc.createCellElements(matrix.getCellMap());
      boundDoc = new DocumentBoundary();
      boundDoc.createBoundaryElements(matrix.getBoundaryMap());
      iteration = iterationValue.n;
   }

   /**
    * Write the output in the separate thread
    */
   @Override
   public void backgroundWrite() throws Exception 
   {
      File iterationDir = new File(String.format(
            "%s%siteration_%s",
            outputDir.getAbsolutePath(), 
            File.separator,
            Long.toString(iteration)
            ));
      if(!iterationDir.exists())
      {
         iterationDir.mkdir();
      }
      configDoc.write(iterationDir);
      cellDoc.write(iterationDir);
      boundDoc.write(iterationDir);
   }

   /**
    * Close when the thread is finished (nothing needed here)
    */
   @Override
   public void closeWhenFinished() throws Exception 
   {
   }

}
