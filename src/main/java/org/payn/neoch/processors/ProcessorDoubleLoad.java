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
      storageProcessor.incrementNetLoad(value.n);
   }

   /**
    * Set the update dependencies for the load
    */
   protected abstract void setUpdateDependenciesLoad() throws Exception;

   /**
    * Update the load value
    * 
    * @throws Exception
    */
   protected abstract void updateLoad() throws Exception;

}
