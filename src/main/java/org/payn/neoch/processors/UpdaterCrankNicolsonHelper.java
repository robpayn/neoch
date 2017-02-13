package org.payn.neoch.processors;

import org.payn.chsm.processors.ProcessorDouble;
import org.payn.neoch.processors.interfaces.UpdaterCrankNicolson;

/**
 * Helper for a processor for performing Crank Nicolson updates
 * 
 * @author v78h241
 *
 */
public class UpdaterCrankNicolsonHelper extends UpdaterMemoryHelper implements UpdaterCrankNicolson {

   /**
    * Construct a new instance decorating the provided processor
    * 
    * @param proc
    *       processor to be decorated
    */
   public UpdaterCrankNicolsonHelper(ProcessorDouble proc) 
   {
      super(proc);
   }

   @Override
   public void updateCrankNicolson() throws Exception 
   {
      value.n = 0.5 * (value.n + savedValue);
   }

}
