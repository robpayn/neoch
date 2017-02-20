package org.payn.neoch.processors;

import java.util.ArrayList;

import org.payn.chsm.dependencies.DependencyHandlerInitialize;
import org.payn.chsm.processors.interfaces.InitializerSimple;
import org.payn.chsm.processors.interfaces.UpdaterAutoSimple;
import org.payn.chsm.processors.interfaces.UpdaterSimple;
import org.payn.neoch.ControllerNEO;
import org.payn.neoch.dependencies.DependencyHandlerUpdateLoad;
import org.payn.neoch.dependencies.DependencyHandlerUpdateState;
import org.payn.neoch.dependencies.DependencyHandlerUpdateStorage;
import org.payn.neoch.dependencies.DependencyHandlerUpdateTrade;

/**
 * A controller for a basic Eulerian single step calculation
 * 
 * @author v78h241
 *
 */
public class ControllerNEOEuler extends ControllerNEO {

   /**
    * Set up dependencies for simple initializers
    */
   @Override
   public void handleInitializationDependencies() throws Exception 
   {
      DependencyHandlerInitialize dependencyHandler = new DependencyHandlerInitialize(initializers);
      this.dependencyHandler = dependencyHandler;
      initializers = dependencyHandler.getSortedProcessors();
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
      DependencyHandlerUpdateTrade dependencyHandlerTrade = new DependencyHandlerUpdateTrade(tradeUpdaters);
      DependencyHandlerUpdateLoad dependencyHandlerLoad = new DependencyHandlerUpdateLoad(loadUpdaters);
      DependencyHandlerUpdateStorage dependencyHandlerStorage = new DependencyHandlerUpdateStorage(storageUpdaters);
      DependencyHandlerUpdateState dependencyHandlerUpdate = new DependencyHandlerUpdateState(stateUpdaters);
      DependencyHandlerUpdateState dependencyHandlerInfo = new DependencyHandlerUpdateState(infoUpdaters);
      
      this.dependencyHandler = dependencyHandlerTrade;
      tradeUpdaters = dependencyHandlerTrade.getSortedProcessors();
      
      this.dependencyHandler = dependencyHandlerLoad;
      loadUpdaters = dependencyHandlerLoad.getSortedProcessors();
      
      this.dependencyHandler = dependencyHandlerStorage;
      storageUpdaters = dependencyHandlerStorage.getSortedProcessors();
      
      this.dependencyHandler = dependencyHandlerUpdate;
      stateUpdaters = dependencyHandlerUpdate.getSortedProcessors();

      this.dependencyHandler = dependencyHandlerInfo;
      infoUpdaters = dependencyHandlerInfo.getSortedProcessors();
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
   protected void update(ArrayList<UpdaterAutoSimple> updaters) throws Exception 
   {
      for (UpdaterSimple updater: updaters)
      {
         updater.update();
      }
   }

}
