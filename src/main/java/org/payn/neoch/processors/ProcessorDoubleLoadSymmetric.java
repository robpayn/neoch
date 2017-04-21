package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.chsm.processors.auto.ProcessorDoubleState;
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
   private ProcessorDoubleState storageProcessorAdjacent;

   @Override
   public void setUpdateDependencies() throws Exception
   {
      super.setUpdateDependencies();
      State storageAdjacent = ((HolonBoundary)state.getParentHolon()).getAdjacentBoundary().getCell().getRootState(
            state.getBehavior().getResource());
      storageProcessorAdjacent = (ProcessorDoubleState)storageAdjacent.getProcessor();
   }
   
   @Override
   public void updateRootStateProcessor() throws Exception
   {
      super.updateRootStateProcessor();
      storageProcessorAdjacent.decrementNetChange(value.n);
   }

}
