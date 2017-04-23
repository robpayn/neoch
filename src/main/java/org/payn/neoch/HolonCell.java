package org.payn.neoch;

import java.util.ArrayList;
import java.util.HashMap;

import org.payn.chsm.finitedifference.HolonFiniteDiff;
import org.payn.chsm.values.ValueStateMap;

/**
 * A cell in a NEO lite matrix
 * 
 * @author rob payn
 *
 */
public class HolonCell extends HolonFiniteDiff {
   
   /**
    * Boundaries attached to the cell
    */
   protected HashMap<String, HolonBoundary> boundaries;
   
   /**
    * Getter for the boundary map
    * 
    * @return
    *       boundary map
    */
   public HashMap<String, HolonBoundary> getBoundaryMap() 
   {
      return boundaries;
   }

   /**
    * Construct the cell in the given matrix
    * 
    * @param name
    *       name of cell
    * @param matrix
    *       matrix
    * @throws Exception 
    */
   public HolonCell(String name, HolonMatrix matrix) throws Exception 
   {
      super(name, matrix);
      matrix.addCell(this);
      value = new ValueStateMap();
      boundaries = new HashMap<String, HolonBoundary>();
   }
   
   /**
    * Add a boundary to the cell
    * 
    * @param boundary
    *       boundary to add
    */
   public void addBoundary(HolonBoundary boundary)
   {
      boundaries.put(boundary.getName(), boundary);
   }
   
   /**
    * Get the boundaries containing a given behavior
    * 
    * @param behaviorName
    *       name of the behavior
    * @return
    *       array list of boundaries
    */
   public ArrayList<HolonBoundary> getBoundaries(String behaviorName)
   {
       ArrayList<HolonBoundary> list = new ArrayList<HolonBoundary>();
       for (HolonBoundary bound: this.boundaries.values())
       {
           if(bound.getBehaviorMap().containsKey(behaviorName))
           {
               list.add(bound);
           }
       }
       return list;
   }

   /**
    * Get the boundary with the provided name
    * 
    * @param boundaryName
    *       name of boundary to get
    * @return
    *       boundary object
    */
   public HolonBoundary getBoundary(String boundaryName)
   {
      return boundaries.get(boundaryName);
   }
   
}
