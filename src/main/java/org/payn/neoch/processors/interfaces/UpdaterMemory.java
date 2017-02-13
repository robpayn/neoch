package org.payn.neoch.processors.interfaces;

/**
 * An updater with a simple single slot memory.
 * 
 * @author robpayn
 *
 */
public interface UpdaterMemory {

   /**
    * Save the current value
    */
   public abstract void saveNumber();
   
   /**
    * Restore the previously saved value
    */
   public abstract void restoreSavedNumber();

}
