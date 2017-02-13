package org.payn.neoch.processors;

import java.util.ArrayList;

import org.payn.chsm.Holon;
import org.payn.chsm.Processor;
import org.payn.chsm.processors.ProcessorDouble;
import org.payn.chsm.resources.time.BehaviorTime;
import org.payn.chsm.values.ValueDouble;
import org.payn.neoch.UpdaterState;
import org.payn.neoch.UpdaterStorage;
import org.payn.neoch.processors.interfaces.UpdaterMemory;

/**
 * A controller for a basic Eulerian single step calculation
 * 
 * @author v78h241
 *
 */
public class ControllerNEORKTwo extends ControllerNEOEuler {

   /**
    * Value for the time interval
    */
   private ValueDouble timeIntervalValue;
   
   /**
    * List of memory updaters for the processors in the store phase
    */
   private ArrayList<UpdaterMemory> storeProcessorMemoryUpdaters;
   
   /**
    * Constructor
    */
   public ControllerNEORKTwo()
   {
      super();
      storeProcessorMemoryUpdaters = new ArrayList<UpdaterMemory>();
   }

   /**
    * Overrides initialization from ControllerNEOEuler to set the value for the time interval
    */
   @Override
   public void initialize() throws Exception 
   {
      timeIntervalValue = 
            (ValueDouble)((Holon)state).getState(BehaviorTime.ITERATION_INTERVAL).getValue();
      super.initialize();
   }

   /**
    * Overrides iterate from ControllerNEOEuler to perform the extra calculations for 
    * Runge Kutta (second order midpoint method)
    * @throws Exception 
    */
   @Override
   public void solve() throws Exception 
   {
      rungeKuttaTwoSolver();
   }

   /**
    * Perform a runge kutta iteration
    * 
    * @throws Exception 
    */
   protected void rungeKuttaTwoSolver() throws Exception 
   {
      double fullTimeInterval = timeIntervalValue.n;
      double halfTimeInterval = timeIntervalValue.n / 2;
      
      // Save the initial store values
      saveStore();
      
      // Calculate trade values at beginning of time step
      update(tradeUpdaters);
      update(loadUpdaters);
      
      // Calculate store and trade values after half the time step
      timeIntervalValue.n = halfTimeInterval;
      update(storageUpdaters);
      update(stateUpdaters);
      
      update(tradeUpdaters);
      update(loadUpdaters);
      
      // Restore original store values
      restoreStore();
      
      // Calculate the store values over the full time step
      // using the Runge Kutta estimates of the loads
      timeIntervalValue.n = fullTimeInterval;
      update(storageUpdaters);
      update(stateUpdaters);
   }

   /**
    * Restore the saved value of states with store updaters
    */
   protected void restoreStore() 
   {
      for (UpdaterMemory updater: storeProcessorMemoryUpdaters)
      {
         updater.restoreSavedNumber();
      }
   }

   /**
    * Save the current value of the states with store updaters
    */
   protected void saveStore() 
   {
      for (UpdaterMemory updater: storeProcessorMemoryUpdaters)
      {
         updater.saveNumber();
      }
   }
   
   @Override
   public void addProcessor(Processor processor) throws Exception
   {
      super.addProcessor(processor);
      addStoreMemoryUpdater(processor);
   }

   /**
    * Add a memory updater to a processor if it is a store updater
    * 
    * @param processor
    */
   protected void addStoreMemoryUpdater(Processor processor) 
   {
      if ((UpdaterStorage.class.isInstance(processor) || UpdaterState.class.isInstance(processor)) 
            && UpdaterMemory.class.isInstance(processor))
      {
         storeProcessorMemoryUpdaters.add((UpdaterMemory)processor);
      }
      else if (ProcessorStorageDouble.class.isInstance(processor) || 
            ProcessorDoubleState.class.isInstance(processor))
      {
         storeProcessorMemoryUpdaters.add(new UpdaterMemoryHelper((ProcessorDouble)processor));
      }
   }

}
