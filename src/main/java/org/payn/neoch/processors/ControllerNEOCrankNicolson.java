package org.payn.neoch.processors;

import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.neoch.UpdaterLoad;
import org.payn.neoch.processors.interfaces.UpdaterCrankNicolson;

/**
 * NEO controller that used a Crank Nicolson solver based on fixed point iteration
 * 
 * @author robpayn
 *
 */
public class ControllerNEOCrankNicolson extends ControllerNEOBackEuler {

   /**
    * List of trade processors to be updated by the crank nicolson updaters
    */
   protected ArrayList<UpdaterCrankNicolson> tradeProcessorCrankNicolsonUpdaters;
   
   /**
    * Constructor
    */
   public ControllerNEOCrankNicolson()
   {
      super();
      tradeProcessorCrankNicolsonUpdaters = new ArrayList<UpdaterCrankNicolson>();
   }
   
   @Override
   public void solve() throws Exception 
   {
      crankNicolsonSolver();
   }

   @Override
   protected void setIterationConfig() 
   {
      defaultTolerance = 1e-7;
      maxIterations = 80;
      iterationTotal = 0;
      totalSteps = 0;
      countMaxIterationsReached = 0;
   }

   /**
    * Perform a Crank Nicolson iteration
    * 
    * @throws Exception
    */
   private void crankNicolsonSolver() throws Exception 
   {
      // save the original state
      saveStore();
      // use the original state as the first storage estimate
      setLastStorageEstimates();
      
      // update the loads
      update(tradeUpdaters);
      update(loadUpdaters);
      
      // save the current loads a the load at the beginning of the time step
      saveLoads();
      
      // get a first estimate of the state at the end of the time step (forward Euler)
      update(storageUpdaters);
      update(stateUpdaters);
      
      // check for convergence (only converges if new state does not change)
      converged = true;
      checkTolerances();
      
      if (!converged)
      {
         // Fixed point iteration until converged or until maximum iterations are met
         for (int iterCount = 2; iterCount <= maxIterations; iterCount++)
         {
            converged = true;
            
            // estimate Crank Nicolson loads based on new state estimate
            update(tradeUpdaters);
            update(loadUpdaters);
            updateTradeCrankNicolson();
            
            // restore initial state
            restoreStore();
            
            // get new storage estimate
            update(storageUpdaters);
            update(stateUpdaters);
            
            // check if converged on a consistent estimate of storage
            checkTolerances();
            if (converged || iterCount == maxIterations)
            {
               iterationTotal += iterCount;
               totalSteps += 1;
               if (iterCount == maxIterations)
               {
                  countMaxIterationsReached += 1;
               }
               break;
            }
         }
      }
      else
      {
         iterationTotal += 1;
         totalSteps += 1;
      }
      averageIterations = (double)iterationTotal / (double)totalSteps;
   }

   /**
    * Calculate the Crank Nicolson estimate of all the trade values
    * 
    * @throws Exception
    */
   protected void updateTradeCrankNicolson() throws Exception 
   {
      for (UpdaterCrankNicolson updater: tradeProcessorCrankNicolsonUpdaters)
      {
         updater.updateCrankNicolson();
      }      
   }

   /**
    * Save all the current trade values to memory
    */
   protected void saveLoads() 
   {
      for (UpdaterCrankNicolson updater: tradeProcessorCrankNicolsonUpdaters)
      {
         updater.saveNumber();
      }      
   }
   
   @Override
   public void addProcessor(Processor proc) throws Exception
   {
      super.addProcessor(proc);
      addTradeProcessorCrankNicolson(proc);
   }

   /**
    * Add to the list of crank nicolson trade processor if the appropriate type
    * 
    * @param proc
    */
   protected void addTradeProcessorCrankNicolson(Processor proc) 
   {
      if (UpdaterLoad.class.isInstance(proc) && UpdaterCrankNicolson.class.isInstance(proc))
      {
         tradeProcessorCrankNicolsonUpdaters.add((UpdaterCrankNicolson)proc);
      }
      else if (ProcessorDoubleLoad.class.isInstance(proc))
      {
         tradeProcessorCrankNicolsonUpdaters.add(new UpdaterCrankNicolsonHelper((ProcessorDoubleLoad)proc));
      }
   }
   
}
