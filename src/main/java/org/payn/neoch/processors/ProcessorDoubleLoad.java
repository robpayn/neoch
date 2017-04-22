package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.chsm.finitedifference.processors.ProcessorDoubleDelta;
import org.payn.chsm.finitedifference.processors.ProcessorDoubleBaseState;
import org.payn.chsm.finitedifference.processors.interfaces.UpdaterDelta;
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
      State storage = ((HolonBoundary)state.getParentHolon()).getCell().getBaseState(
            state.getBehavior().getResource()
            );
      rootStateProcessor = (ProcessorDoubleBaseState)storage.getProcessor();
   }
   
}
