package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.chsm.processors.finitedifference.ProcessorDoubleStore;
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
   private ProcessorDoubleStore storageProcessorAdjacent;

   @Override
   public void setUpdateDependencies() throws Exception
   {
      super.setUpdateDependencies();
      HolonBoundary adjacentBoundary = 
            ((HolonBoundary)state.getParentHolon()).getAdjacentBoundary();
      State storageAdjacent = adjacentBoundary.getCell().getStore(
            state.getBehavior().getResource()
            );
      storageProcessorAdjacent = (ProcessorDoubleStore)storageAdjacent.getProcessor();
   }
   
   @Override
   public void updateStoreProcessor() throws Exception
   {
      super.updateStoreProcessor();
      storageProcessorAdjacent.decrementNetChange(value.n);
   }

}
