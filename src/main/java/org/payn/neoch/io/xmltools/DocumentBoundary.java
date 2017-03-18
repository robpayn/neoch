package org.payn.neoch.io.xmltools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.payn.chsm.io.xmltools.DocumentHolon;
import org.payn.neoch.HolonBoundary;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML document for boundary elements
 * 
 * @author rob payn
 *
 */
public class DocumentBoundary extends DocumentHolon {
   
   /**
    * Raw constructor
    * 
    * @throws Exception
    */
   public DocumentBoundary() throws Exception 
   {
      super("boundaries.xml", "boundaries");
   }
   
   /**
    * Construct an empty instance specifying the file name
    * 
    * @param fileName
    *       name of file
    * @throws Exception
    *       
    */
   public DocumentBoundary(String fileName) throws Exception
   {
      super(fileName, "boundaries");
   }

   /**
    * Construct a document based on an existing XML file
    * 
    * @param boundaryFile
    *       file with boundary XML information
    * @throws Exception
    *       if error in reading XML file
    */
   public DocumentBoundary(File boundaryFile) throws Exception 
   {
      super(boundaryFile);
   }

   /**
    * Create a new boundary element
    * 
    * @param name
    *       name of boundary
    * @param cellName
    *       name of cell attached to boundary
    * @return
    *       if error in creating element
    */
   public ElementBoundary createBoundaryElement(String name, String cellName) 
   {
      ElementBoundary boundElement = new ElementBoundary(
            document.createElement(ElementBoundary.TAG_NAME)
            );
      boundElement.setName(name);
      boundElement.setCell(cellName);
      boundElement.setParentElement(getRootElement());
      return boundElement;
   }

   /**
    * Create boundary elements from a hash map of boundaries
    * 
    * @param boundaries
    */
   public void createBoundaryElements(HashMap<String, HolonBoundary> boundaries) 
   {
      HashMap<String,HolonBoundary> adjBounds = new HashMap<String,HolonBoundary>();
      for (HolonBoundary boundary: boundaries.values())
      {
         if (!adjBounds.containsKey(boundary.getName()))
         {
            ElementBoundary boundElement = createBoundaryElement(
                  boundary.getName(), 
                  boundary.getCell().getName()
                  );

            ElementBoundary adjBoundElement = null;
            HolonBoundary adjBoundary = boundary.getAdjacentBoundary();
            if (adjBoundary != null)
            {
               adjBounds.put(adjBoundary.getName(), adjBoundary);
               adjBoundElement = boundElement.createAdjacentElement(
                     adjBoundary.getName(),
                     adjBoundary.getCell().getName()
                     );
            }
            
            boundElement.createBehaviorElements(boundary);
            
            if (adjBoundary != null)
            {
               adjBoundElement.createBehaviorElements(adjBoundary);
            }
         }
      }
   }

   /**
    * Get the list of boundary elements
    * 
    * @return 
    *       list of boundary elements
    */
   public ArrayList<ElementBoundary> getBoundaryList() 
   {
      NodeList nodeList = getRootElement().getChildNodes();
      ArrayList<ElementBoundary> list = new ArrayList<ElementBoundary>();
      for (int i = 0; i < nodeList.getLength(); i++)
      {
         if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE &&
               ((Element)nodeList.item(i)).getTagName().equals(ElementBoundary.TAG_NAME))
         {
            list.add(new ElementBoundary((Element)nodeList.item(i)));
         }
      }
      return list;
   }

}
