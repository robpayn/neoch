package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.chsm.processors.auto.ProcessorDoubleDelta;
import org.payn.chsm.processors.auto.ProcessorDoubleState;
import org.payn.chsm.processors.auto.UpdaterDelta;
import org.payn.neoch.HolonBoundary;

/**
 * A processor for a load
 * 
 * @author robpayn
 *
 */
public abstract class ProcessorDoubleLoad extends ProcessorDoubleDelta 
implements UpdaterDelta {
   
   @Override
   public void setUpdateDependencies() throws Exception
   {
      setUpdateDependenciesDelta();
      State storage = ((HolonBoundary)state.getParentHolon()).getCell().getRootState(
            state.getBehavior().getResource()
            );
      rootStateProcessor = (ProcessorDoubleState)storage.getProcessor();
   }
   
}
