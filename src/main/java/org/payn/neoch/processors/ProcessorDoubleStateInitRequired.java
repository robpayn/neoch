package org.payn.neoch.processors;

/**
 * A processor for the state phase that requires initialization
 * 
 * @author v78h241
 *
 */
public abstract class ProcessorDoubleStateInitRequired extends ProcessorDoubleStateInit {

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
