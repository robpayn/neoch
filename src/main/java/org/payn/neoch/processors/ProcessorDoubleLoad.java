package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.chsm.processors.finitedifference.ProcessorDoubleStore;
import org.payn.chsm.processors.finitedifference.ProcessorDoubleDelta;
import org.payn.chsm.processors.finitedifference.interfaces.UpdaterDelta;
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
      State storage = ((HolonBoundary)state.getParentHolon()).getCell().getStore(
            state.getBehavior().getResource()
            );
      storeProcessor = (ProcessorDoubleStore)storage.getProcessor();
   }
   
}
