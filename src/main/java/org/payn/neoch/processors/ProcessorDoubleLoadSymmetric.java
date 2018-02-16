package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.chsm.finitediff.processors.ProcessorDoubleCore;
import org.payn.neoch.HolonBoundary;

/**
 * Processor for a load with a symmetric relationship with the adjacent boundary
 * 
 * @author v78h241
 *
 */
public abstract class ProcessorDoubleLoadSymmetric extends ProcessorDoubleLoad {

   /**
    * The adjacent core processor to decrement with this load
    */
   private ProcessorDoubleCore coreProcessorAdjacent;

   @Override
   public void setUpdateDependencies() throws Exception
   {
      super.setUpdateDependencies();
      HolonBoundary adjacentBoundary = 
            ((HolonBoundary)state.getParentHolon()).getAdjacentBoundary();
      State coreAdjacent = adjacentBoundary.getCell().getCore(
            state.getBehavior().getResource()
            );
      coreProcessorAdjacent = (ProcessorDoubleCore)coreAdjacent.getProcessor();
   }
   
   @Override
   public void updateCoreProcessor() throws Exception
   {
      super.updateCoreProcessor();
      coreProcessorAdjacent.decrementNetDelta(value.n);
   }

}
