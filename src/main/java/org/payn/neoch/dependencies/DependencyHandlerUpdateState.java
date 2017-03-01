package org.payn.neoch.dependencies;

import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.chsm.dependencies.DependencyHandlerUpdate;
import org.payn.chsm.processors.interfaces.UpdaterSimpleAuto;
import org.payn.neoch.UpdaterState;

/**
 * Dependency handler for trade phase updating processors
 * 
 * @author robpayn
 *
 */
public class DependencyHandlerUpdateState extends DependencyHandlerUpdate {

   /**
    * Create a new instance based on a list of updaters
    * 
    * @param updaters
    *       list of updaters
    */
   public DependencyHandlerUpdateState(ArrayList<UpdaterSimpleAuto> updaters) 
   {
      super(updaters);
   }

   /**
    * Add a dependency if the needed processor is a trade phase updater
    */
   @Override
   public void addUpdateDependency(Processor processor, Processor neededProcessor) 
   {
      if (UpdaterState.class.isInstance(neededProcessor))
      {
         addToGraph((UpdaterState)processor, (UpdaterState)neededProcessor);
      }
   }

}
