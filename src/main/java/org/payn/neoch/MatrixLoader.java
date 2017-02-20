package org.payn.neoch;

import java.io.File;
import java.util.HashMap;

import org.payn.chsm.ModelLoader;

/**
 * Loader for a matrix builder
 * 
 * @author robpayn
 *
 */
public abstract class MatrixLoader extends ModelLoader<MatrixBuilder> {

   /**
    * Static method to build the matrix based on the initial configuration information in the xml
    * file in the specified working directory.
    * 
    * @param args
    *       command line arguments
    * @param workingDir
    *       working directory
    * @param builderLoader 
    *       loader for the builder (will look for definition in the command line if this is null)
    * @return
    *       the cell network controller created by the factory
    * @throws Exception
    *       if error in creating cell network
    */
   public static MatrixBuilder loadBuilder(String[] args, File workingDir, MatrixLoader builderLoader) 
         throws Exception
   {
      HashMap<String,String> argMap = createArgMap(args);
      return loadBuilder(argMap, workingDir, builderLoader);
   }
   
   /**
    * Load the matrix builder
    * 
    * @param argMap
    *       map of command line arguments
    * @param workingDir
    *       working directory
    * @param matrixLoader
    *       loader object to use for loading, can be null if 
    *       information about loader is in command line
    * @return
    *       matrix builder object
    * @throws Exception
    *       if error in loading
    */
   public static MatrixBuilder loadBuilder(HashMap<String, String> argMap,
         File workingDir, MatrixLoader matrixLoader) throws Exception 
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
      
      // Load and instantiate the builder
      System.out.println();
      MatrixLoader loader = matrixLoader;
      if (loader == null || 
            (argMap.containsKey(MatrixLoader.ARG_FILE_PATH) && 
                  argMap.containsKey(MatrixLoader.ARG_CLASS_PATH))
            )
      {
         loader = (MatrixLoader)MatrixLoader.createObjectInstance(
               MatrixBuilder.class.getClassLoader(),
               new File(argMap.get(MatrixLoader.ARG_FILE_PATH)),
               argMap.get(MatrixLoader.ARG_CLASS_PATH),
               "Matrix loader"
               );
      }
      System.out.println(String.format(
            "Loading with %s ...",
            loader.getClass().getCanonicalName()
            ));
      MatrixBuilder builder = loader.load(argMap, workingDir);
      
      return builder;

   }

}
