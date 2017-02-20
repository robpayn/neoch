package org.payn.neoch;

import java.io.File;
import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.chsm.State;
import org.payn.chsm.processors.ControllerTimeStep;
import org.payn.chsm.processors.interfaces.InitializerAutoSimple;
import org.payn.chsm.processors.interfaces.UpdaterAutoSimple;

/**
 * Controller for a NEO-CH simulation
 * 
 * @author robpayn
 *
 */
public abstract class ControllerNEO extends ControllerTimeStep {

   /**
    * Main entry point.  Construct a NEO-CH matrix and processor and execute.
    * 
    * @param args
    *       command line arguments
    */
   public static void main(String[] args)
   {
      try 
      {
         MatrixBuilder builder = MatrixLoader.loadBuilder(
               new File(System.getProperty("user.dir")),
               null,
               args
               );
         HolonMatrix matrix = builder.createModel();
         ControllerNEO controller = matrix.getController();
         controller.initializeController();
         controller.executeController();
      } 
      catch (Exception e) 
      {
         e.printStackTrace();
      }
   }

   /**
    * List of initializing processors
    */
   protected ArrayList<InitializerAutoSimple> initializers;
   
   /**
    * Getter
    * 
    * @return
    *       unsorted list of initializing processors
    */
   public ArrayList<InitializerAutoSimple> getInitializers() 
   {
      return initializers;
   }

   /**
    * List of updaters for the trade phase
    */
   protected ArrayList<UpdaterAutoSimple> tradeUpdaters;
   
   /**
    * Getter
    * 
    * @return
    *       list of trade updaters
    */
   public ArrayList<UpdaterAutoSimple> getTradeUpdaters() 
   {
      return tradeUpdaters;
   }

   /**
    * List of updaters for the load phase
    */
   protected ArrayList<UpdaterAutoSimple> loadUpdaters;
   
   /**
    * Getter
    * 
    * @return
    *       list of load updaters
    */
   public ArrayList<UpdaterAutoSimple> getLoadUpdaters() 
   {
      return loadUpdaters;
   }

   /**
    * List of updaters for the storage phase
    */
   protected ArrayList<UpdaterAutoSimple> storageUpdaters;

   /**
    * Getter
    * 
    * @return
    *       list of storage updaters
    */
   public ArrayList<UpdaterAutoSimple> getStorageUpdaters() 
   {
      return storageUpdaters;
   }

   /**
    * List of updaters for the update phase
    */
   protected ArrayList<UpdaterAutoSimple> stateUpdaters;
   
   /**
    * Getter
    * 
    * @return
    *       list of storage updaters
    */
   public ArrayList<UpdaterAutoSimple> getStateUpdaters() 
   {
      return stateUpdaters;
   }

   /**
    * List of updaters for the update phase
    */
   protected ArrayList<UpdaterAutoSimple> infoUpdaters;
   
   /**
    * Getter
    * 
    * @return
    *       list of storage updaters
    */
   public ArrayList<UpdaterAutoSimple> getInfoUpdaters() 
   {
      return infoUpdaters;
   }

   
   /**
    * Constructor
    */
   public ControllerNEO()
   {
      initializers = new ArrayList<InitializerAutoSimple>();
      tradeUpdaters = new ArrayList<UpdaterAutoSimple>();
      loadUpdaters = new ArrayList<UpdaterAutoSimple>();
      storageUpdaters = new ArrayList<UpdaterAutoSimple>();
      stateUpdaters = new ArrayList<UpdaterAutoSimple>();
      infoUpdaters = new ArrayList<UpdaterAutoSimple>();
   }
   
   /**
    * Overrides the implementation in ProcessorAbstract because
    * this processor operates directly on the state's reference
    * to the value when creating a new value.
    */
   @Override
   public void setState(State state) throws Exception
   {
      this.state = state;
      if (state.getValue() == null)
      {
         createValue();
      }
      else
      {
         setValueFromState();
      }
   }

   /**
    * Overrides the implementation in ControllerHolon to set the reference
    * from the state and to run the builder
    */
   @Override
   public void createValue() throws Exception 
   {
      super.createValue();
      state.setValue(value);
      runBuilder();
   }
   
   /**
    * Overrides the implementation in ControllerHolon to run the builder
    */
   @Override
   public void setValueFromState() throws Exception
   {
      super.setValueFromState();
      runBuilder();
   }
   
   /**
    * Run the builder
    * 
    * @throws Exception
    *       if builder has not been specified
    */
   protected void runBuilder() throws Exception
   {
      if (builder == null)
      {
         throw new Exception("NEO controller requires a builder to create the matrix.");
      }
      builder.buildModel();
   }

   /**
    * Get the class loader
    */
   @Override 
   public ClassLoader getClassLoader()
   {
      return classLoader;
   }

   /**
    * Add a processor to the controller
    */
   @Override
   public void addProcessor(Processor processor) throws Exception 
   {
      if (InitializerAutoSimple.class.isInstance(processor))
      {
         initializers.add((InitializerAutoSimple)processor);
      }
      
      if (UpdaterTrade.class.isInstance(processor))
      {
         tradeUpdaters.add((UpdaterTrade)processor);
      }
      else if (UpdaterLoad.class.isInstance(processor))
      {
         loadUpdaters.add((UpdaterLoad)processor);
      }
      else if (UpdaterStorage.class.isInstance(processor))
      {
         storageUpdaters.add((UpdaterStorage)processor);
      }
      else if (UpdaterState.class.isInstance(processor))
      {
         stateUpdaters.add((UpdaterAutoSimple)processor);
      }
      else if (UpdaterInfo.class.isInstance(processor))
      {
         infoUpdaters.add((UpdaterAutoSimple)processor);
      }
   }
   
}
