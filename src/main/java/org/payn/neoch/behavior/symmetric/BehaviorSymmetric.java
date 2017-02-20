package org.payn.neoch.behavior.symmetric;

import org.payn.chsm.Controller;
import org.payn.chsm.Holon;
import org.payn.chsm.Processor;
import org.payn.chsm.StateVariable;
import org.payn.neoch.HolonBoundary;
import org.payn.neoch.UpdaterLoad;
import org.payn.neoch.behavior.BehaviorMatrix;

/**
 * A symmetric behavior.  Specifies that the adjacent boundary has negative the load of this boundary.
 * 
 * @author robpayn
 *
 */
public abstract class BehaviorSymmetric extends BehaviorMatrix {
   
   /**
    * Create the appropriate name for a negative load associated with
    * the provided load state name
    * 
    * @param stateName
    *       state on which the name should be based
    * @return
    *       name for negative load in symmetric behavior
    */
   public static String createNegName(String stateName) 
   {
      return stateName + "Neg";
   }
   
   @Override
   public void installProcessor(Holon holon, Controller controller,
         String stateName, Processor processor) throws Exception
   {
      super.installProcessor(holon, controller, stateName, processor);
      if (UpdaterLoad.class.isInstance(processor))
      {
         Processor negProcessor = createNegativeProcessor(stateName);
         negProcessor.setController(controller);
         new StateVariable(
               BehaviorSymmetric.createNegName(stateName), 
               this, 
               negProcessor, 
               ((HolonBoundary)holon).getAdjacentBoundary()
               );
         controller.addProcessor(negProcessor);
      }
   }

   /**
    * Instantiating classes must implement a method that will provide an appropriate
    * load processor type for the adjacent side of the symmetric behavior
    * 
    * @param stateName
    * @return
    */
   protected abstract Processor createNegativeProcessor(String stateName);

}
