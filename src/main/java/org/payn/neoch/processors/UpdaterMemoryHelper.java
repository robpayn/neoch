package org.payn.neoch.processors;

import org.payn.chsm.processors.ProcessorDouble;
import org.payn.chsm.values.ValueDouble;
import org.payn.neoch.processors.interfaces.UpdaterMemory;

/** 
 * Helper class to implement the memory updater for double precision
 * states that need memory.
 * 
 * @author v78h241
 *
 */
public class UpdaterMemoryHelper implements UpdaterMemory {
   
   /**
    * Processor decorated by this updater
    */
   protected ProcessorDouble processor;
   
   /**
    * Saved value of the processor's value
    */
   protected double savedValue;
   
   /**
    * Value changed by the processor
    */
   protected ValueDouble value;

   /**
    * Construct new instance decorating the provided processor
    * 
    * @param proc
    *       processor to be decorated
    */
   public UpdaterMemoryHelper(ProcessorDouble proc)
   {
      this.processor = proc;
      this.value = proc.getValue();
   }

   @Override
   public void saveNumber() 
   {
      savedValue = value.n;
   }

   @Override
   public void restoreSavedNumber() 
   {
      value.n = savedValue;
   }

}
