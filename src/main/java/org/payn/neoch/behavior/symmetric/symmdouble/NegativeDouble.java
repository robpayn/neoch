package org.payn.neoch.behavior.symmetric.symmdouble;

import org.payn.chsm.processors.interfaces.InitializerAutoSimple;
import org.payn.chsm.values.ValueDouble;
import org.payn.neoch.HolonBoundary;
import org.payn.neoch.processors.ProcessorLoadDouble;

/**
 * A double precision floating point load for a negative behavior
 * 
 * @author robpayn
 *
 */
public class NegativeDouble 
extends ProcessorLoadDouble 
implements InitializerAutoSimple {
   
   /**
    * Value of the adjacent load
    */
   private ValueDouble adjValue;
   
   /**
    * Name of the state for the corresponding symmetric load
    */
   private String adjName;

   /**
    * Set the name of the adjacent state
    * 
    * @param adjName
    *       name of the adjacent state
    */
   public void setAdjacentName(String adjName) 
   {
      this.adjName = adjName;
   }

   /**
    * Define the load state variable needed for initialization (only necessary for reporting purposes)
    */
   @Override
   public void setInitDependencies() throws Exception 
   {
      setUpdateDependencies();
   }

   /**
    * Initialize the value (only necessary for reporting purposes
    */
   @Override
   public void initialize() 
   {
      update();
   }

   /**
    * Define the load state variable needed for calculation
    */
   @Override
   public void setUpdateDependencies() throws Exception 
   {
      HolonBoundary adjBound = ((HolonBoundary)getState().getParentHolon()).getAdjacentBoundary();
      adjValue = (ValueDouble)createDependency(adjBound, adjName).getValue();
   }

   /**
    * Update the value
    */
   @Override
   public void update() 
   {
      value.n = -adjValue.n;
   }

}
