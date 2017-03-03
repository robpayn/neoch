package org.payn.neoch;

import org.payn.chsm.processors.interfaces.UpdaterSimpleAuto;

/**
 * Updater for a load.  Used to distinguish loads from other trade processors.
 * 
 * @author v78h241
 *
 */
public interface UpdaterLoad extends UpdaterSimpleAuto {
   
   /**
    * Set the update dependencies for the load
    * 
    * @throws Exception 
    */
   void setUpdateDependenciesLoad() throws Exception;

   /**
    * Update the load value
    * 
    * @throws Exception
    */
   void updateLoad() throws Exception;

   /**
    * Update the storage processor net load
    * 
    * @throws Exception
    */
   void updateStorageProcessor() throws Exception;

}
