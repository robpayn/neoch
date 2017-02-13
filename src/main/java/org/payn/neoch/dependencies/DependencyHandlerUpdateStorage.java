package org.payn.neoch.dependencies;

import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.chsm.dependencies.DependencyHandlerUpdate;
import org.payn.chsm.processors.interfaces.UpdaterAutoSimple;
import org.payn.neoch.UpdaterStorage;

/**
 * Dependency handler for storage phase updaters
 * 
 * @author robpayn
 *
 */
public class DependencyHandlerUpdateStorage extends DependencyHandlerUpdate {

   /**
    * Create a new dependency handler based on a list of updaters
    * 
    * @param updaters
    *       list of updaters
    */
   public DependencyHandlerUpdateStorage(ArrayList<UpdaterAutoSimple> updaters) 
   {
      super(updaters);
   }

   /**
    * Add a dependency if the needed processor is a storage phase updaters
    */
   @Override
   public void addUpdateDependency(Processor processor, Processor neededProcessor) 
   {
      if (UpdaterStorage.class.isInstance(neededProcessor))
      {
         addToGraph((UpdaterStorage)processor, (UpdaterStorage)neededProcessor);
      }
   }

}
