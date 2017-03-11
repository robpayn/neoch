package org.payn.neoch.sorters;

import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.chsm.processors.interfaces.UpdaterSimpleAuto;
import org.payn.chsm.sorters.SorterUpdate;
import org.payn.neoch.UpdaterState;

/**
 * Sorter for ordering calls to trade phase updating processors
 * 
 * @author robpayn
 *
 */
public class SorterUpdateState extends SorterUpdate {

   /**
    * Create a new instance based on a list of updaters
    * 
    * @param updaters
    *       list of updaters
    */
   public SorterUpdateState(ArrayList<UpdaterSimpleAuto> updaters) 
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
