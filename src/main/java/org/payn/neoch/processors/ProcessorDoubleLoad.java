package org.payn.neoch.processors;

import org.payn.chsm.State;
import org.payn.chsm.finitediff.processors.ProcessorDoubleCore;
import org.payn.chsm.finitediff.processors.ProcessorDoubleDelta;
import org.payn.chsm.finitediff.processors.interfaces.UpdaterDelta;
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
      State core = ((HolonBoundary)state.getParentHolon()).getCell().getCore(
            state.getBehavior().getResource()
            );
      coreProcessor = (ProcessorDoubleCore)core.getProcessor();
   }
   
}
