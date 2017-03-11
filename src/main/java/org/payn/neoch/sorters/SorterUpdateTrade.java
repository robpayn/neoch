package org.payn.neoch.sorters;

import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.chsm.processors.interfaces.UpdaterSimpleAuto;
import org.payn.chsm.sorters.SorterUpdate;
import org.payn.neoch.UpdaterTrade;

/**
 * Sorter for ordering calls to trade phase updating processors
 * 
 * @author robpayn
 *
 */
public class SorterUpdateTrade extends SorterUpdate {

   /**
    * Create a new instance based on a list of updaters
    * 
    * @param updaters
    *       list of updaters
    */
   public SorterUpdateTrade(ArrayList<UpdaterSimpleAuto> updaters) 
   {
      super(updaters);
   }

   /**
    * Add a dependency if the needed processor is a trade phase updater
    */
   @Override
   public void addUpdateDependency(Processor processor, Processor neededProcessor) 
   {
      if (UpdaterTrade.class.isInstance(neededProcessor))
      {
         addToGraph((UpdaterTrade)processor, (UpdaterTrade)neededProcessor);
      }
   }

}
