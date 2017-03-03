package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.neoch.HolonBoundary;

/**
 * Processor for a load with a symmetric relationship with the adjacent boundary
 * 
 * @author v78h241
 *
 */
public abstract class ProcessorDoubleLoadSymmetric extends ProcessorDoubleLoad {

   /**
    * The adjacent storage processor to decrement with this load
    */
   private ProcessorDoubleStorage storageProcessorAdjacent;

   @Override
   public void setUpdateDependencies() throws Exception
   {
      super.setUpdateDependencies();
      State storageAdjacent = ((HolonBoundary)state.getParentHolon()).getAdjacentBoundary().getCell().getStorage(
            state.getBehavior().getResource());
      storageProcessorAdjacent = (ProcessorDoubleStorage)storageAdjacent.getProcessor();
   }
   
   @Override
   public void updateStorageProcessor() throws Exception
   {
      super.updateStorageProcessor();
      storageProcessorAdjacent.decrementNetLoad(value.n);
   }

}
