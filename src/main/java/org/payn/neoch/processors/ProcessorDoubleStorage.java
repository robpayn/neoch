package org.payn.neoch.processors;

import java.util.ArrayList;

import org.payn.chsm.Holon;
import org.payn.chsm.State;
import org.payn.chsm.processors.ProcessorDouble;
import org.payn.chsm.resources.time.BehaviorTime;
import org.payn.chsm.values.ValueDouble;
import org.payn.neoch.HolonBoundary;
import org.payn.neoch.HolonCell;
import org.payn.neoch.UpdaterStorage;
import org.payn.neoch.behavior.BehaviorMatrix;

/**
 * A double precision floating point storage processor that uses a simple one
 * step mass balance algorithm
 * 
 * @author v78h241
 *
 */
public class ProcessorDoubleStorage extends ProcessorDouble implements UpdaterStorage {
   
   /**
    * List of loads used to update the storage
    */
   protected ArrayList<ValueDouble> loadValues;
   
   /**
    * Time interval of the model
    */
   private ValueDouble timeInterval;
   
   /**
    * Raw constructor
    */
   public ProcessorDoubleStorage()
   {
      loadValues = new ArrayList<ValueDouble>();
   }

   /**
    * Define the state variables necessary to calculate the storage
    */
   @Override
   public void setUpdateDependencies() 
   {
      Holon matrixHolon = (Holon)getController().getState();
      timeInterval = (ValueDouble)matrixHolon.getState(BehaviorTime.ITERATION_INTERVAL).getValue();
      for (HolonBoundary boundary: ((HolonCell)getState().getParentHolon()).getBoundaryMap().values())
      {
         ArrayList<State> loadList = boundary.getLoads(
               ((BehaviorMatrix)getState().getBehavior()).getResource()
               );
         if (loadList != null)
         {
            for (State load: loadList)
            {
               loadValues.add((ValueDouble)load.getValue());
            }
         }
      }
   }

   /**
    * Update the value by calculating the net load and adding
    */
   @Override
   public void update() 
   {
      double netLoad = 0;
      for (ValueDouble load: loadValues)
      {
         netLoad += load.n;
      }
      value.n = value.n + netLoad * timeInterval.n;
   }

}
