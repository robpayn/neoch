package org.payn.neoch.sorters;

import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.chsm.processors.interfaces.UpdaterSimpleAuto;
import org.payn.chsm.sorters.SorterUpdate;
import org.payn.neoch.UpdaterStorage;

/**
 * Sorter for ordering calls to storage phase updaters
 * 
 * @author robpayn
 *
 */
public class SorterUpdateStorage extends SorterUpdate {

   /**
    * Create a new sorter based on a list of updaters
    * 
    * @param updaters
    *       list of updaters
    */
   public SorterUpdateStorage(ArrayList<UpdaterSimpleAuto> updaters) 
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
