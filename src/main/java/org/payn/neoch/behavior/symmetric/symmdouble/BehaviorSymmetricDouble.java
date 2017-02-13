package org.payn.neoch.behavior.symmetric.symmdouble;

import org.payn.chsm.Processor;
import org.payn.neoch.behavior.symmetric.BehaviorSymmetric;

/**
 * A symmetric behavior that tracks a load with a double precision floating point value
 * 
 * @author robpayn
 *
 */
public abstract class BehaviorSymmetricDouble extends BehaviorSymmetric {

   @Override
   protected Processor createNegativeProcessor(String stateName)
   {
      NegativeDouble negProcessor = new NegativeDouble();
      negProcessor.setAdjacentName(stateName);
      return negProcessor;
   }

}
