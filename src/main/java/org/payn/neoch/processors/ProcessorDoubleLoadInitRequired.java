package org.payn.neoch.processors;

/**
 * Load processor for which initialization is required
 * 
 * @author v78h241
 *
 */
public abstract class ProcessorDoubleLoadInitRequired extends ProcessorDoubleLoadInit {

   @Override
   public void setInitDependencies() throws Exception 
   {}

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
