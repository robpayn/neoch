package org.payn.neoch.processors;

import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.neoch.UpdaterStorage;
import org.payn.neoch.processors.interfaces.UpdaterTolerance;

/**
 * A controller for a basic backwards Euler implicit solver, based on fixed point iteration
 * 
 * @author v78h241
 *
 */
public class ControllerNEOCHBackEuler extends ControllerNEOCHRKTwo {

   /**
    * Flag indicating if fixed point iteration has converged
    */
   protected boolean converged;
   
   /**
    * Total iterations (for calculating average)
    */
   protected long iterationTotal;
   
   /**
    * Total time steps including iterations (for calculating average)
    */
   protected long totalSteps;
   
   /**
    * Average number of fixed point iterations per time step
    */
   protected double averageIterations;
   
   /**
    * Default tolerance for convergence checks
    */
   protected double defaultTolerance;
   
   /**
    * Getter
    * 
    * @return
    *       tolerance
    */
   public double getDefaultTolerance() 
   {
      return defaultTolerance;
   }

   /**
    * Maximum number of fixed point iterations
    */
   protected int maxIterations;
   
   /**
    * Number of times maximum iteration limit has been reached
    */
   protected int countMaxIterationsReached;

   /**
    * List of storage processors for checking tolerance of convergence
    */
   protected ArrayList<UpdaterTolerance> storageProcessorToleranceUpdaters;

   /**
    * Constructor
    */
   public ControllerNEOCHBackEuler()
   {
      super();
      storageProcessorToleranceUpdaters = new ArrayList<UpdaterTolerance>();
   }
   
   /**
    * Overrides initialization from ControllerNEOEuler to set the value for the time interval
    */
   @Override
   public void initialize() throws Exception 
   {
      setIterationConfig();
      super.initialize();
   }

   /**
    * Set up the iteration configuration
    */
   protected void setIterationConfig() 
   {
      defaultTolerance = 0.0001;
      maxIterations = 50;
      iterationTotal = 0;
      totalSteps = 0;
      countMaxIterationsReached = 0;
   }


   /**
    * Overrides iterate from ControllerNEORKTwo to perform the 
    * backward euler algorithm using fixed point interpolation
    * 
    * @throws Exception 
    */
   @Override
   public void solve() throws Exception 
   {
      backEulerSolver();
   }
   
   /**
    * Perform a backwards Euler iteration
    * 
    * @throws Exception
    */
   protected void backEulerSolver() throws Exception 
   {
      // save the original state
      saveStore();
      // use the original state as the first storage estimate
      setLastStorageEstimates();
      
      // Iterate until converged or until maximum iterations are met
      for (int iterCount = 1; iterCount <= maxIterations; iterCount++)
      {
         converged = true;
         
         // calculate new estimates of loads
         update(tradeUpdaters);
         update(loadUpdaters);
         
         // restore to the original initial state
         restoreStore();
         
         // calculate storage and state based on new estimate of loads
         update(storageUpdaters);
         update(stateUpdaters);
         
         // check if new estimates of storages has converged
         checkTolerances();
         if (converged || iterCount == maxIterations)
         {
            iterationTotal += iterCount;
            totalSteps += 1;
            averageIterations = (double)iterationTotal / (double)totalSteps;
            if (iterCount == maxIterations)
            {
               countMaxIterationsReached += 1;
            }
            break;
         }
      }
   }


   /**
    * Call the store updaters and check tolerances on storages
    * 
    * @throws Exception
    */
   protected void checkTolerances() throws Exception
   {
      for (UpdaterTolerance updater: storageProcessorToleranceUpdaters)
      {
         updater.checkTolerance();
         updater.setLastEstimate();
      }
   }

   /**
    * Set the last estimates for all storages
    */
   protected void setLastStorageEstimates() 
   {
      for (UpdaterTolerance updater: storageProcessorToleranceUpdaters)
      {
         updater.setLastEstimate();
      }
   }

   /**
    * Set the convergence flag to false to force another iteration
    */
   public void convergenceFailed() 
   {
      converged = false;
   }

   @Override
   public void executeController() throws Exception
   {
      super.executeController();
      loggerManager.statusUpdate("");
      loggerManager.statusUpdate(String.format("Average iterations before convergence: %4f ...", averageIterations));
      if (countMaxIterationsReached > 0)
      {
         loggerManager.statusUpdate(String.format("Maximum iterations reached %d times", countMaxIterationsReached));
      }
   }
   
   @Override
   public void addProcessor(Processor proc) throws Exception
   {
      super.addProcessor(proc);
      addStorageToleranceUpdater(proc);
   }

   /**
    * Add to the list of storage tolerance updaters if the appropriate type
    * 
    * @param proc
    */
   protected void addStorageToleranceUpdater(Processor proc) 
   {
      if (UpdaterStorage.class.isInstance(proc) && UpdaterTolerance.class.isInstance(proc))
      {
         storageProcessorToleranceUpdaters.add((UpdaterTolerance)proc);
      }
      else if (ProcessorDoubleStorage.class.isInstance(proc))
      {
         storageProcessorToleranceUpdaters.add(new UpdaterToleranceHelper((ProcessorDoubleStorage)proc));
      }
   }

}
