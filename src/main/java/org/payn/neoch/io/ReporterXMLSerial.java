package org.payn.neoch.io;

import java.io.File;

import org.payn.chsm.Holon;
import org.payn.chsm.ModelLoader;
import org.payn.chsm.io.file.ReporterSingleThread;
import org.payn.neoch.HolonMatrix;
import org.payn.neoch.MatrixBuilderXML;
import org.payn.neoch.MatrixLoaderXML;
import org.payn.neoch.io.xmltools.DocumentBoundary;
import org.payn.neoch.io.xmltools.DocumentCell;
import org.payn.neoch.io.xmltools.XMLDocumentMatrixConfig;

/**
 * Output handler that creates serializable XML output for the XML builder
 * 
 * This output handler is only compatible with derivatives of the MatrixBuilderXML matrix builder
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
   private XMLDocumentMatrixConfig configDoc;

   /**
    * Buffered iteration count
    */
   private long iteration;

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
      ModelLoader<?> matrixLoader = matrix.getBuilder().getLoader();
      if (!(MatrixLoaderXML.class.isInstance(matrixLoader) && 
            MatrixBuilderXML.class.isInstance(matrix.getBuilder())))
      {
         throw new Exception("XML serial outputter only works when the matrix builder and loader are both XML based");
      }
      configDoc = ((MatrixLoaderXML)matrixLoader).getDocument();
      configDoc.changeRootBehaviors(matrix);
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
