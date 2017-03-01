package org.payn.neoch;

import java.io.File;
import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.chsm.State;
import org.payn.chsm.processors.ControllerTimeStep;
import org.payn.chsm.processors.interfaces.InitializerSimpleAuto;
import org.payn.chsm.processors.interfaces.UpdaterSimpleAuto;

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
   protected ArrayList<InitializerSimpleAuto> initializers;
   
   /**
    * Getter
    * 
    * @return
    *       unsorted list of initializing processors
    */
   public ArrayList<InitializerSimpleAuto> getInitializers() 
   {
      return initializers;
   }

   /**
    * List of updaters for the trade phase
    */
   protected ArrayList<UpdaterSimpleAuto> tradeUpdaters;
   
   /**
    * Getter
    * 
    * @return
    *       list of trade updaters
    */
   public ArrayList<UpdaterSimpleAuto> getTradeUpdaters() 
   {
      return tradeUpdaters;
   }

   /**
    * List of updaters for the load phase
    */
   protected ArrayList<UpdaterSimpleAuto> loadUpdaters;
   
   /**
    * Getter
    * 
    * @return
    *       list of load updaters
    */
   public ArrayList<UpdaterSimpleAuto> getLoadUpdaters() 
   {
      return loadUpdaters;
   }

   /**
    * List of updaters for the storage phase
    */
   protected ArrayList<UpdaterSimpleAuto> storageUpdaters;

   /**
    * Getter
    * 
    * @return
    *       list of storage updaters
    */
   public ArrayList<UpdaterSimpleAuto> getStorageUpdaters() 
   {
      return storageUpdaters;
   }

   /**
    * List of updaters for the update phase
    */
   protected ArrayList<UpdaterSimpleAuto> stateUpdaters;
   
   /**
    * Getter
    * 
    * @return
    *       list of storage updaters
    */
   public ArrayList<UpdaterSimpleAuto> getStateUpdaters() 
   {
      return stateUpdaters;
   }

   /**
    * List of updaters for the update phase
    */
   protected ArrayList<UpdaterSimpleAuto> infoUpdaters;
   
   /**
    * Getter
    * 
    * @return
    *       list of storage updaters
    */
   public ArrayList<UpdaterSimpleAuto> getInfoUpdaters() 
   {
      return infoUpdaters;
   }

   
   /**
    * Constructor
    */
   public ControllerNEO()
   {
      initializers = new ArrayList<InitializerSimpleAuto>();
      tradeUpdaters = new ArrayList<UpdaterSimpleAuto>();
      loadUpdaters = new ArrayList<UpdaterSimpleAuto>();
      storageUpdaters = new ArrayList<UpdaterSimpleAuto>();
      stateUpdaters = new ArrayList<UpdaterSimpleAuto>();
      infoUpdaters = new ArrayList<UpdaterSimpleAuto>();
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
      if (InitializerSimpleAuto.class.isInstance(processor))
      {
         initializers.add((InitializerSimpleAuto)processor);
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
         stateUpdaters.add((UpdaterSimpleAuto)processor);
      }
      else if (UpdaterInfo.class.isInstance(processor))
      {
         infoUpdaters.add((UpdaterSimpleAuto)processor);
      }
   }
   
}
