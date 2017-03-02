package org.payn.neoch.processors;

/**
 * Convenience abstraction for a symmetric load that will throw an error
 * if no initial value is provided
 * 
 * @author v78h241
 *
 */

public abstract class ProcessorDoubleLoadSymmetricInitRequired 
extends ProcessorDoubleLoadSymmetricInit {
   
   @Override
   public void initialize() throws Exception 
   {
      if (value.isNoValue())
      {
         throw new Exception(String.format(
               "%s must be assigned an initial value in holon %s",
               state.getName(),
               state.getParentHolon().getName()
               ));
      }
   }

}
