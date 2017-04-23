package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.chsm.finitedifference.processors.ProcessorDoubleBaseState;
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
   private ProcessorDoubleBaseState storageProcessorAdjacent;

   @Override
   public void setUpdateDependencies() throws Exception
   {
      super.setUpdateDependencies();
      HolonBoundary adjacentBoundary = 
            ((HolonBoundary)state.getParentHolon()).getAdjacentBoundary();
      State storageAdjacent = adjacentBoundary.getCell().getBaseState(
            state.getBehavior().getResource()
            );
      storageProcessorAdjacent = (ProcessorDoubleBaseState)storageAdjacent.getProcessor();
   }
   
   @Override
   public void updateRootStateProcessor() throws Exception
   {
      super.updateRootStateProcessor();
      storageProcessorAdjacent.decrementNetChange(value.n);
   }

}
