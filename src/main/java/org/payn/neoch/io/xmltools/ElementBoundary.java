package org.payn.neoch.io.xmltools;

import org.w3c.dom.Element;

/**
 * XML element for a boundary
 * 
 * @author v78h241
 *
 */
public class ElementBoundary extends ElementHolonMatrix {
   
   /**
    * Tag name for the element
    */
   public static final String TAG_NAME = "boundary";
   
   /**
    * Tag name for the adjacent boundary element
    */
   private static final String TAG_NAME_ADJ = "adjacent";

   /**
    * Construct a new instance based on the provided XML element
    * 
    * @param element
    *       XML element
    */
   public ElementBoundary(Element element)
   {
      super(element);
   }

   /**
    * Get the cell name from the element
    * 
    * @return
    *       cell name
    */
   public String getCellName() 
   {
      return element.getAttribute("cell");
   }

   /**
    * Get the adjacent boundary element from the element
    * 
    * @return
    *       boundary element for adjacent boundary
    */
   public ElementBoundary getAdjacentBoundElement() 
   {
      return new ElementBoundary(this.getFirstChildElement("adjacent"));
   }

   /**
    * Set the cell name in the element
    * 
    * @param cellName
    */
   public void setCell(String cellName) 
   {
      setAttribute("cell", cellName);
   }

   /**
    * Create the adjacent boundary element for the boundary element
    * 
    * @param name
    *       name of adjacent boundary
    * @param cellName
    *       name of cell attached to adjacent boundary
    * @return
    *       the boundary element
    */
   public ElementBoundary createAdjacentElement(String name, String cellName) 
   {
      ElementBoundary boundElement = new ElementBoundary(
            element.getOwnerDocument().createElement(ElementBoundary.TAG_NAME_ADJ)
            );
      boundElement.setName(name);
      boundElement.setCell(cellName);
      boundElement.setParentElement(element);
      return boundElement;
   }

}
