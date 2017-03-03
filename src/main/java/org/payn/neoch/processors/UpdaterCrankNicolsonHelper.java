package org.payn.neoch.processors;

import org.payn.neoch.processors.interfaces.UpdaterCrankNicolson;

/**
 * Helper for a processor for performing Crank Nicolson updates
 * 
 * @author v78h241
 *
 */
public class UpdaterCrankNicolsonHelper extends UpdaterMemoryHelper<ProcessorDoubleLoad> 
implements UpdaterCrankNicolson {

   /**
    * Construct a new instance decorating the provided processor
    * 
    * @param proc
    *       processor to be decorated
    */
   public UpdaterCrankNicolsonHelper(ProcessorDoubleLoad proc) 
   {
      super(proc);
   }

   @Override
   public void updateCrankNicolson() throws Exception 
   {
      processor.updateLoad();
      value.n = 0.5 * (value.n + savedValue);
      processor.updateStorageProcessor();
   }

}
