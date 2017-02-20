package org.payn.neoch;

import java.util.ArrayList;
import java.util.HashMap;

import org.payn.chsm.HolonAbstract;
import org.payn.chsm.Resource;
import org.payn.chsm.State;
import org.payn.chsm.values.ValueStateMap;
import org.payn.neoch.behavior.BehaviorMatrix;

/**
 * A cell in a NEO lite matrix
 * 
 * @author rob payn
 *
 */
public class HolonCell extends HolonAbstract {
   
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
    * Storage state variables associated with the cell
    */
   protected HashMap<Resource, State> storages;
   
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
      storages = new HashMap<Resource, State>();
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
    * Get the storage associated with the provided resource
    * 
    * @param resource
    *       resource
    * @return
    *       storage associated with the resource
    */
   public State getStorage(Resource resource) 
   {
      return storages.get(resource);
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

   @Override
   public void trackProcessor(State state) throws Exception
   {
      if (UpdaterStorage.class.isInstance(state.getProcessor()))
      {
         storages.put(((BehaviorMatrix)state.getBehavior()).getResource(), state);
      }
   }
   
}
