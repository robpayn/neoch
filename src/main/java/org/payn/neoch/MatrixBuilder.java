package org.payn.neoch;

import java.io.File;
import java.util.HashMap;

import org.payn.chsm.Behavior;
import org.payn.chsm.ModelBuilder;
import org.payn.chsm.ModelLoader;
import org.payn.chsm.resources.time.BehaviorTime;

import com.oracle.webservices.internal.api.databinding.Databinding.Builder;

/**
 * Builds a matrix for a NEOCH model
 * 
 * @author robpayn
 *
 */
public abstract class MatrixBuilder extends ModelBuilder<HolonMatrix> {
   
   /**
    * Main entry point.  Construct a NEOCH model and execute.
    * Model will be configured based on the loader and configuration
    * specified in the command line.
    * 
    * @param args
    *       command line arguments
    */
   public static void main(String[] args)
   {
      try 
      {
         MatrixBuilder builder = MatrixBuilder.loadBuilder(
               new File(System.getProperty("user.dir")),
               args
               );
         HolonMatrix matrix = builder.buildModel();
         matrix.getController().initializeController();
         matrix.getController().executeController();
      } 
      catch (Exception e) 
      {
         e.printStackTrace();
      }
   }

   /**
    * Static method to load the builder for the matrix,
    * as configured by command line arguments.
    * 
    * <p>Command line arguments must be formated by space-delimited
    * &ltkey&gt=&ltvalue&gt format</p>
    * 
    * 
    * @param args
    *       command line arguments
    * @param workingDir
    *       working directory for the model
    * @return
    *       the builder
    * @throws Exception
    *       if error in creating cell network
    */
   public static MatrixBuilder loadBuilder(File workingDir, String[] args) 
         throws Exception
   {
      HashMap<String,String> argMap = ModelLoader.createArgMap(args);
      return loadBuilder(workingDir, argMap, null);
   }
   
   /**
    * Static method to load the builder for the matrix,
    * as configured by key-value pairings in command line 
    * arguments.
    * 
    * @param workingDir
    *       working directory
    * @param argMap
    *       map of command line arguments
    * @param modelLoader
    *       loader object to use for loading.  If null,
    *       loader specified in command line will be
    *       loaded and executed
    * @return
    *       matrix builder object
    * @throws Exception
    *       if error in loading
    */
   public static MatrixBuilder loadBuilder(File workingDir, 
         HashMap<String, String> argMap, ModelLoader modelLoader) 
         throws Exception 
   {
      // Check for valid working directory
      if (!workingDir.exists()) 
      {
         throw new Exception(String.format(
               "Specified working directory does not exist.",
               workingDir.getAbsolutePath()
               ));
      }
      else if (workingDir.isFile()) 
      {
         throw new Exception(String.format(
               "Working directory %s cannot be a file.",
               workingDir.getAbsolutePath()
               ));
      }
      
      // Load the loader
      // If the provided loader is null, attempt to load the loader
      // specified in command line arguments
      System.out.println();
      ModelLoader loader = modelLoader;
      if (loader == null || 
            (argMap.containsKey(ModelLoader.ARG_FILE_PATH) && 
                  argMap.containsKey(ModelLoader.ARG_CLASS_PATH))
            )
      {
         loader = (ModelLoader)ModelLoader.createObjectInstance(
               MatrixBuilder.class.getClassLoader(),
               new File(argMap.get(ModelLoader.ARG_FILE_PATH)),
               argMap.get(ModelLoader.ARG_CLASS_PATH),
               "Matrix loader"
               );
      }
      System.out.println(String.format(
            "Loading with %s ...",
            modelLoader.getClass().getCanonicalName()
            ));
      
      // Load all model components and return a reference to the builder
      // Throw an error if the builder is not a MatrixBuilder type
      try
      {
         return (MatrixBuilder)loader.load(workingDir, argMap);
      }
      catch (Exception e)
      {
         throw new Exception(String.format(
               "Loader class %s cannot load a matrix builder",
               Builder.class.getCanonicalName()
               ), e);
      }
   }
   
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
   public ControllerNEOCH getController()
   {
      return (ControllerNEOCH)controller;
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
   public HolonBoundary createBoundary(String boundName, String cellName) 
         throws Exception 
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
   public HolonBoundary createBoundary(String boundName, String cellName, 
         HolonBoundary adjBoundary) throws Exception 
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
   public HolonMatrix buildModel() throws Exception
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
   public void installStates() throws Exception
   {
      initializeBuildSources();
      loggerManager.statusUpdate(
            "   Installing cells in matrix..."
            );
      installCells();
      loggerManager.statusUpdate(
            "   Installing boundaries in matrix..."
            );
      installBoundaries();
      loggerManager.statusUpdate(
            "   Installing behaviors in cells and setting state variable initial values..."
            );
      installCellBehaviors();
      loggerManager.statusUpdate(
            "   Installing behaviors in boundaries and setting state variable initial values..."
            );
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
