package org.payn.neoch.processors;

import java.util.ArrayList;

import org.payn.chsm.processors.interfaces.InitializerSimple;
import org.payn.chsm.processors.interfaces.UpdaterSimpleAuto;
import org.payn.chsm.sorters.SorterInitialize;
import org.payn.chsm.processors.interfaces.UpdaterSimple;
import org.payn.neoch.ControllerNEOCH;
import org.payn.neoch.sorters.SorterUpdateLoad;
import org.payn.neoch.sorters.SorterUpdateState;
import org.payn.neoch.sorters.SorterUpdateStorage;
import org.payn.neoch.sorters.SorterUpdateTrade;

/**
 * A controller for a basic Eulerian single step calculation
 * 
 * @author v78h241
 *
 */
public class ControllerNEOCHEuler extends ControllerNEOCH {

   /**
    * Set up dependencies for simple initializers
    */
   @Override
   public void handleInitializationDependencies() throws Exception 
   {
      SorterInitialize sorter = new SorterInitialize(initializers);
      this.sorter = sorter;
      initializers = sorter.getSortedProcessors();
   }

   /**
    * Initialize the controller
    */
   @Override
   public void initialize() throws Exception 
   {
      for (InitializerSimple initializer: initializers)
      {
         initializer.initialize();
      }
   }

   /**
    * Set up dependencies for simple updaters
    */
   @Override
   public void handleExecutionDependencies() throws Exception 
   {
      SorterUpdateTrade sorterTrade = new SorterUpdateTrade(tradeUpdaters);
      SorterUpdateLoad sorterLoad = new SorterUpdateLoad(loadUpdaters);
      SorterUpdateStorage sorterStorage = new SorterUpdateStorage(storageUpdaters);
      SorterUpdateState sorterUpdate = new SorterUpdateState(stateUpdaters);
      SorterUpdateState sorterInfo = new SorterUpdateState(infoUpdaters);
      
      this.sorter = sorterTrade;
      tradeUpdaters = sorterTrade.getSortedProcessors();
      
      this.sorter = sorterLoad;
      loadUpdaters = sorterLoad.getSortedProcessors();
      
      this.sorter = sorterStorage;
      storageUpdaters = sorterStorage.getSortedProcessors();
      
      this.sorter = sorterUpdate;
      stateUpdaters = sorterUpdate.getSortedProcessors();

      this.sorter = sorterInfo;
      infoUpdaters = sorterInfo.getSortedProcessors();
   }

   /**
    * Perform a single iteration
    * @throws Exception 
    */
   @Override
   public void iterate() throws Exception 
   {
      solve();
      update(infoUpdaters);
   }

   /**
    * Solve the equations for the current iteration
    * 
    * @throws Exception
    */
   protected void solve() throws Exception 
   {
      update(tradeUpdaters);
      update(loadUpdaters);
      update(storageUpdaters);
      update(stateUpdaters);
   }

   /**
    * Update the provided list of updaters
    * 
    * @param updaters
    *       list of updaters to update
    * @throws Exception
    *       if error in update
    */
   protected void update(ArrayList<UpdaterSimpleAuto> updaters) throws Exception 
   {
      for (UpdaterSimple updater: updaters)
      {
         updater.update();
      }
   }

}
