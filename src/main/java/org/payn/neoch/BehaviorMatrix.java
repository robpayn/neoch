package org.payn.neoch;


import org.payn.chsm.BehaviorAbstract;
import org.payn.chsm.Processor;
import org.payn.chsm.Value;

/**
 * A behavior for a resource within the matrix
 * 
 * @author v78h241
 *
 */
public abstract class BehaviorMatrix extends BehaviorAbstract {
   
   /**
    * Add a processor for a symmetric behavior
    * 
    * @param processorName
    * @param processorClass
    */
   protected void addSymmetricProcessor(String processorName, Class<? extends Processor> processorClass, Class<? extends Value>  valueClass)
   {
      super.addProcessor(processorName, processorClass, valueClass);
   }

}
