package org.payn.neoch;

import java.util.ArrayList;

import org.payn.chsm.Processor;
import org.payn.chsm.processors.ControllerTimeStep;
import org.payn.chsm.processors.interfaces.InitializerSimpleAuto;
import org.payn.chsm.processors.interfaces.UpdaterSimpleAuto;

/**
 * Controller for a NEO-CH simulation
 * 
 * @author robpayn
 *
 */
public abstract class ControllerNEOCH extends ControllerTimeStep {

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
   public ControllerNEOCH()
   {
      initializers = new ArrayList<InitializerSimpleAuto>();
      tradeUpdaters = new ArrayList<UpdaterSimpleAuto>();
      loadUpdaters = new ArrayList<UpdaterSimpleAuto>();
      storageUpdaters = new ArrayList<UpdaterSimpleAuto>();
      stateUpdaters = new ArrayList<UpdaterSimpleAuto>();
      infoUpdaters = new ArrayList<UpdaterSimpleAuto>();
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
