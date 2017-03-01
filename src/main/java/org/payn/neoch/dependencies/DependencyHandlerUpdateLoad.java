package org.payn.neoch.dependencies;

import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.chsm.dependencies.DependencyHandlerUpdate;
import org.payn.chsm.processors.interfaces.UpdaterSimpleAuto;
import org.payn.neoch.UpdaterLoad;

/**
 * Dependency handler for storage phase updaters
 * 
 * @author robpayn
 *
 */
public class DependencyHandlerUpdateLoad extends DependencyHandlerUpdate {

   /**
    * Create a new dependency handler based on a list of updaters
    * 
    * @param updaters
    *       list of updaters
    */
   public DependencyHandlerUpdateLoad(ArrayList<UpdaterSimpleAuto> updaters) 
   {
      super(updaters);
   }

   /**
    * Add a dependency if the needed processor is a storage phase updaters
    */
   @Override
   public void addUpdateDependency(Processor processor, Processor neededProcessor) 
   {
      if (UpdaterLoad.class.isInstance(neededProcessor))
      {
         addToGraph((UpdaterLoad)processor, (UpdaterLoad)neededProcessor);
      }
   }

}
