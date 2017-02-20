package org.payn.neoch;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.payn.chsm.HolonAbstract;
import org.payn.chsm.State;

/**
 * Matrix for a NEO lite simulation model
 * 
 * @author rob payn
 *
 */
public class HolonMatrix extends HolonAbstract {
   
   /**
    * Map of cells
    */
   private HashMap<String,HolonCell> cellMap;
   
   /**
    * Getter for map of cells
    * 
    * @return
    *       cell map
    */
   public HashMap<String,HolonCell> getCellMap() 
   {
      return cellMap;
   }

   /**
    * Map of boundaries
    */
   private HashMap<String,HolonBoundary> boundMap;

   /**
    * Getter for map of boundaries
    * 
    * @return
    *       boundary map
    */
   public HashMap<String,HolonBoundary> getBoundaryMap() 
   {
      return boundMap;
   }

   /**
    * Builder used to create the matrix
    */
   private MatrixBuilder builder;

   /**
    * Getter 
    * 
    * @return
    *       MatrixBuilderLoader used to create matrix
    */
   public MatrixBuilder getBuilder() 
   {
      return builder;
   }
   
   /**
    * Getter for the controller
    * 
    * @return
    *       Processor cast to NEO controller
    */
   public ControllerNEO getController()
   {
      return (ControllerNEO)processor;
   }

   /**
    * Construct a new matrix with the provided name and configuration xml document
    * 
    * @param name
    *       name of matrix
    * @param builder
    *       matrix builder used to create this matrix
    * @throws Exception
    *       if error in constructing matrix
    */
   public HolonMatrix(String name, MatrixBuilder builder) throws Exception 
   {
      super(name, null);
      this.builder = builder;
      cellMap = new LinkedHashMap<String,HolonCell>();
      boundMap = new LinkedHashMap<String,HolonBoundary>();
   }

   /**
    * Get the named cell
    * 
    * @param cellName
    *       name of cell to retrieve
    * @return
    *       cell
    */
   public HolonCell getCell(String cellName) 
   {
      return cellMap.get(cellName);
   }

   /**
    * Add a cell
    * 
    * @param cell
    *       cell to add
    */
   public void addCell(HolonCell cell) 
   {
      cellMap.put(cell.getName(), cell);
   }
   
   /**
    * Get the named boundary
    * 
    * @param boundName
    *       name of boundary to retrieve
    * @return
    *       boundary
    */
   public HolonBoundary getBoundary(String boundName)
   {
      return boundMap.get(boundName);
   }

   /**
    * Add a boundary
    * 
    * @param boundary
    *       boundary to add
    */
   public void addBoundary(HolonBoundary boundary) 
   {
      boundMap.put(boundary.getName(), boundary);
   }

   @Override
   public void trackProcessor(State state) throws Exception 
   {
      // Does not track processors
   }

}
