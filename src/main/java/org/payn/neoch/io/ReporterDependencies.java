package org.payn.neoch.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import org.payn.chsm.Processor;
import org.payn.chsm.io.file.ReporterFileSystem;
import org.payn.neoch.ControllerNEO;

/**
 * Reporter for debugging dependencies
 * 
 * @author robpayn
 *
 */
public class ReporterDependencies extends ReporterFileSystem {
   
   /**
    * Construct a new instance using the provided working directory
    * and argument map
    * 
    * @param workingDir
    * @param argMap
    */
   public ReporterDependencies(File workingDir,
         HashMap<String, String> argMap) 
   {
      super(workingDir, argMap);
   }

   /**
    * No operations necessary to open location
    */
   @Override
   public void openLocation() throws Exception 
   {}

   /**
    * Write only on first time step
    */
   @Override
   public void conditionalWrite() throws Exception 
   {
      if (iterationValue.n <= 1)
      {
         write();
      }
   }

   /**
    * Write the sorted lists to the respective files
    */
   @Override
   public void write() throws Exception 
   {
      ControllerNEO controller = (ControllerNEO)source.getProcessor();
      if (iterationValue.n == 0)
      {
         File file = new File(
               outputDir + File.separator + "dependencies_init.txt"
               );
         BufferedWriter writer = new BufferedWriter(new FileWriter(file));
         for (Processor initializer: controller.getInitializers())
         {
            writer.write(initializer.toString());
            writer.newLine();
         }
         writer.close();
      }
      if (iterationValue.n == 1)
      {
         File file = new File(
               outputDir + File.separator + "dependencies_exec.txt"
               );
         BufferedWriter writer = new BufferedWriter(new FileWriter(file));
         writer.write("Trade phase:");
         writer.newLine();
         for (Processor updater: controller.getTradeUpdaters())
         {
            writer.write(updater.toString());
            writer.newLine();
         }
         writer.write("Load phase:");
         writer.newLine();
         for (Processor updater: controller.getLoadUpdaters())
         {
            writer.write(updater.toString());
            writer.newLine();
         }
         writer.write("Storage phase:");
         writer.newLine();
         for (Processor updater: controller.getStorageUpdaters())
         {
            writer.write(updater.toString());
            writer.newLine();
         }
         writer.write("State phase:");
         writer.newLine();
         for (Processor updater: controller.getStateUpdaters())
         {
            writer.write(updater.toString());
            writer.newLine();
         }
         writer.close();
      }
   }

   /**
    * No close operation required
    */
   @Override
   public void closeLocation() throws Exception 
   {}

}
