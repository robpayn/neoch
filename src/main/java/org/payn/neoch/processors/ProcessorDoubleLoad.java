package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.chsm.processors.ProcessorDouble;
import org.payn.neoch.HolonBoundary;
import org.payn.neoch.UpdaterLoad;

/**
 * A processor for a load
 * 
 * @author robpayn
 *
 */
public abstract class ProcessorDoubleLoad extends ProcessorDouble 
implements UpdaterLoad {
   
   /**
    * The storage processor to increment with this load
    */
   private ProcessorDoubleStorage storageProcessor;

   @Override
   public void setUpdateDependencies() throws Exception
   {
      setUpdateDependenciesLoad();
      State storage = ((HolonBoundary)state.getParentHolon()).getCell().getStorage(
            state.getBehavior().getResource());
      storageProcessor = (ProcessorDoubleStorage)storage.getProcessor();
   }
   
   @Override
   public void update() throws Exception
   {
      updateLoad();
      updateStorageProcessor();
   }

   /**
    * Update the storage processor net load
    * 
    * @throws Exception
    */
   @Override
   public void updateStorageProcessor() throws Exception 
   {
      storageProcessor.incrementNetLoad(value.n);
   }

}
