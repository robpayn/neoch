package org.payn.neoch.io.xmltools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.payn.chsm.io.xmltools.ElementHolon;
import org.payn.chsm.io.xmltools.XMLDocument;
import org.payn.neoch.HolonCell;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An XML document for Cell elements
 * 
 * @author rob payn
 *
 */
public class DocumentCell extends XMLDocument implements Iterable<ElementHolon> {
   
   /**
    * XML tag name for a cell holon element
    */
   public static final String TAG_NAME_CELL = "cell";

   /**
    * Raw constructor
    * 
    * @throws Exception
    */
   public DocumentCell() throws Exception 
   {
      super("cells.xml", "cells");
   }
   
   /**
    * Construct an empty instance based on a file name
    * 
    * @param fileName
    * @throws Exception
    */
   public DocumentCell(String fileName) throws Exception
   {
      super(fileName, "cells");
   }

   /**
    * Construct from an existing cells XML file
    * 
    * @param cellFile
    *       existing XML file
    * @throws Exception
    *       if error in loading file
    */
   public DocumentCell(File cellFile) throws Exception 
   {
      super(cellFile);
   }
   
   /**
    * Create cell elements from a hashmap of cells
    * 
    * @param cells
    *       hashmap of cells
    */
   public void createCellElements(HashMap<String, HolonCell> cells) 
   {
      for (HolonCell cell: cells.values())
      {
         ElementHolon cellElement = createCellElement(cell.getName());
         cellElement.createBehaviorElements(cell);
      }
   }

   /**
    * Create a cell element with the provided cell name
    * 
    * @param name
    *       name of cell
    * @return
    *       cell element
    */
   public ElementHolon createCellElement(String name) 
   {
      ElementHolon cellElement = new ElementHolon(
            document.createElement(TAG_NAME_CELL)
            );
      cellElement.setName(name);
      cellElement.setParentElement(getRootElement());
      return cellElement;
   }

   /**
    * Get an iterator over cell elements
    */
   @Override
   public Iterator<ElementHolon> iterator() 
   {
      NodeList nodeList = getRootElement().getChildNodes();
      ArrayList<ElementHolon> list = new ArrayList<ElementHolon>();
      for (int i = 0; i < nodeList.getLength(); i++)
      {
         if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE &&
               ((Element)nodeList.item(i)).getTagName().equals("cell"))
         {
            list.add(new ElementHolon((Element)nodeList.item(i)));
         }
      }
      return list.iterator();
   }

}
