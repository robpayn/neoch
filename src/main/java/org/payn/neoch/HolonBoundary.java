package org.payn.neoch;

import java.util.ArrayList;
import java.util.HashMap;

import org.payn.chsm.Holon;
import org.payn.chsm.Resource;
import org.payn.chsm.State;
import org.payn.chsm.values.ValueStateMap;
import org.payn.neoch.behavior.BehaviorMatrix;

/**
 * Controls a boundary to a cell, can be an external boundary linked to an adjacent boundary.
 * 
 * @author rob payn
 *
 */
public class HolonBoundary extends Holon {
   
   /**
    * Attached cell
    */
   protected HolonCell cell;
   
   /**
    * Getter for the attached cell
    * 
    * @return
    *       attached cell
    */
   public HolonCell getCell() 
   {
      return cell;
   }

   /**
    * Adjacent boundary
    */
   protected HolonBoundary adjBoundary;

   /**
    * Set the adjacent boundary
    * 
    * @param adjBoundary
    *       adjacent boundary
    */
   public void setAdjacentBoundary(HolonBoundary adjBoundary) 
   {
      this.adjBoundary = adjBoundary;
   }

   /**
    * Get the adjacent boundary
    * 
    * @return
    *       adjacent boundary
    */
   public HolonBoundary getAdjacentBoundary() 
   {
      return adjBoundary;
   }

   /**
    * The loads associated with this boundary
    */
   protected HashMap<Resource, ArrayList<State>> loads;
   
   /**
    * Get the load for the provided resource
    * 
    * @param resource
    *       resource for which load is requested
    * @return
    *       load
    */
   public ArrayList<State> getLoads(Resource resource) 
   {
      return loads.get(resource);
   }

   /**
    * Construct a new boundary with the provided attributes
    * 
    * @param boundName
    *       name of the boundary
    * @param cellName
    *       name of the attached cell
    * @param matrix
    *       name of the containing matrix
    * @throws Exception
    *       if error in creating the boundary
    */
   public HolonBoundary(String boundName, String cellName, HolonMatrix matrix) throws Exception 
   {
      super(boundName, matrix);
      cell = matrix.getCell(cellName);
      if (cell == null)
         throw new Exception(String.format(
               "Cell %s not found for boundary %s",
               cellName,
               boundName
               ));
      cell.addBoundary(this);
      value = new ValueStateMap();
      adjBoundary = null;
      matrix.addBoundary(this);
      loads = new HashMap<Resource, ArrayList<State>>();
   }

   @Override
   public void trackProcessor(State state) throws Exception
   {
      if (UpdaterLoad.class.isInstance(state.getProcessor()))
      {
         Resource resource = ((BehaviorMatrix)state.getBehavior()).getResource();
         if (loads.containsKey(resource))
         {
            loads.get(resource).add(state);
         }
         else
         {
            ArrayList<State> list = new ArrayList<State>();
            list.add(state);
            loads.put(resource, list);
         }
      }
   }

}
