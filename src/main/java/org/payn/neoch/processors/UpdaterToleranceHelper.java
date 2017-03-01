package org.payn.neoch.processors;

import org.payn.chsm.values.ValueDouble;
import org.payn.neoch.processors.interfaces.UpdaterTolerance;

/**
 * Helper for a processor implementing the tolerance updater
 * 
 * @author v78h241
 *
 */
public class UpdaterToleranceHelper implements UpdaterTolerance {

   /**
    * The last estimate for checking tolerance of the current estimate
    */
   protected double lastEstimate;
   
   /**
    * Value containing the current estimate
    */
   protected ValueDouble value;
   
   /**
    * Controller tracking the results of the tolerance check
    */
   protected ControllerNEOBackEuler controller;
   
   /**
    * Decorated processor
    */
   protected ProcessorDoubleStorage proc;

   /**
    * Construct a new instance decorating the provided processor
    * 
    * @param proc
    *       processor to be decorated
    */
   public UpdaterToleranceHelper(ProcessorDoubleStorage proc) 
   {
      this.proc = proc;
      value = proc.getValue();
      controller = (ControllerNEOBackEuler)proc.getController();
   }

   @Override
   public void setLastEstimate()
   {
      lastEstimate = value.n;
   }

   @Override
   public void checkTolerance()
   {
      double error = Math.abs(value.n - lastEstimate);
      if (error > controller.getDefaultTolerance())
      {
         ((ControllerNEOBackEuler)controller).convergenceFailed();
      }
   }

}
