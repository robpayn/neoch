package org.payn.neoch;

import java.util.HashMap;

import org.payn.chsm.Behavior;
import org.payn.chsm.ModelBuilder;
import org.payn.chsm.resources.time.BehaviorTime;

/**
 * Builds a NEO lite matrix
 * 
 * @author robpayn
 *
 */
public abstract class MatrixBuilder extends ModelBuilder<HolonMatrix> {
   
   /**
    * List of global behaviors
    */
   protected HashMap<String, Behavior> globalBehaviors;
  
   /**
    * Constructor
    */
   public MatrixBuilder()
   {
      globalBehaviors = new HashMap<String, Behavior>();
   }

   @Override
   public ControllerNEO getController()
   {
      return (ControllerNEO)controller;
   }

   /**
    * Add a global behavior
    * 
    * @param behavior
    *       behavior to be added
    */
   public void addGlobalBeahvior(Behavior behavior)
   {
      globalBehaviors.put(behavior.getName(), behavior);
   }

   /**
    * Create a cell in the matrix
    * 
    * @param name
    *       name for the cell
    * @return
    *       the cell created
    * @throws Exception 
    */
   public HolonCell createCell(String name) throws Exception 
   {
      return new HolonCell(name, holon);
   }

   /**
    * Create a boundary in the matrix
    * 
    * @param boundName
    *       name of the boundary
    * @param cellName
    *       name of the cell attached to the boundary
    * @return
    *       boundary created
    * @throws Exception
    *       if error in creating the boundary
    */
   public HolonBoundary createBoundary(String boundName, String cellName) throws Exception 
   {
      return new HolonBoundary(boundName, cellName, holon);
   }

   /**
    * Create a boundary in the matrix with adjacency
    * 
    * @param boundName
    *       name of boundary
    * @param cellName
    *       name of attached cell
    * @param adjBoundary
    *       adjacent boundary
    * @return
    *       boundary created
    * @throws Exception
    *       if error in creating boundary
    */
   public HolonBoundary createBoundary(String boundName, String cellName, HolonBoundary adjBoundary) 
               throws Exception 
   {
      HolonBoundary boundary = createBoundary(boundName, cellName);
      boundary.setAdjacentBoundary(adjBoundary);
      adjBoundary.setAdjacentBoundary(boundary);
      return boundary;
   }

   /**
    * Static method to build the matrix based on the initial configuration information in the xml
    * file in the specified working directory.
    * 
    * @param argMap
    *       map of command line argument key value pairs
    * @param workingDir
    *       working directory
    * @param builderLoader 
    *       loader for the builder (will look for definition in command line if this is null)
    * @return
    *       the cell network controller created by the factory
    * @throws Exception
    *       if error in creating cell network
    */
   @Override
   public HolonMatrix createModel() throws Exception
   {
      // Build the matrix
      loggerManager.statusUpdate("");
      HolonMatrix matrix = buildMatrix();
      loggerManager.statusUpdate(String.format(
            "Matrix build time = %s ...", 
            BehaviorTime.parseTimeInMillis(System.currentTimeMillis() - previousTime)
            ));
      loggerManager.statusUpdate("");
      
      return matrix;
   }
   
   @Override 
   public void buildModel() throws Exception
   {
      initializeBuildSources();
      loggerManager.statusUpdate("   Installing cells in matrix...");
      installCells();
      loggerManager.statusUpdate("   Installing boundaries in matrix...");
      installBoundaries();
      loggerManager.statusUpdate("   Installing behaviors in cells and setting state variable initial values...");
      installCellBehaviors();
      loggerManager.statusUpdate("   Installing behaviors in boundaries and setting state variable initial values...");
      installBoundaryBehaviors();
   }
   
   /**
    * Initialize the data sources for the build
    */
   protected abstract void initializeBuildSources() throws Exception;

   /**
    * Install behaviors in the boundaries
    * 
    * @throws Exception
    */
   protected abstract void installBoundaryBehaviors() throws Exception;

   /**
    * Install behaviors in the cells
    * 
    * @throws Exception
    */
   protected abstract void installCellBehaviors() throws Exception;

   /**
    * Install the boundaries in the matrix
    * 
    * @throws Exception
    */
   protected abstract void installBoundaries() throws Exception;

   /**
    * Install the cells in the matrix
    * 
    * @throws Exception
    */
   protected abstract void installCells() throws Exception;

   /**
    * Create the matrix
    * 
    * @return
    *       Matrix object
    * @throws Exception 
    */
   protected abstract HolonMatrix buildMatrix() throws Exception;

}
