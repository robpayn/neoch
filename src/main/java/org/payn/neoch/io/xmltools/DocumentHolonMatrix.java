package org.payn.neoch.io.xmltools;

import java.io.File;

import org.payn.chsm.io.xmltools.DocumentHolon;
import org.payn.chsm.io.xmltools.ElementHolon;
import org.w3c.dom.Element;

/**
 * XML document with the holon information for a matrix
 * 
 * @author robpayn
 *
 */
public class DocumentHolonMatrix extends DocumentHolon {

   /**
    * XML tag name for a cell holon element
    */
   public static final String TAG_NAME_CELL = "cell";
   
   /**
    * XML tag for the cells list
    */
   private static final String TAG_NAME_CELLS = "cells";
   
   /**
    * XML tag for the boundaries list
    */
   private static final String TAG_NAME_BOUNDARIES = "boundaries";
   
   /**
    * XML element for cells list
    */
   private Element cellsElement;
   
   /**
    * XML element for boundaries list
    */
   private Element boundariesElement;

   /**
    * Construct a new instance based on an existing file
    * 
    * @param file
    * @throws Exception
    */
   public DocumentHolonMatrix(File file) throws Exception 
   {
      super(file);
   }

   /**
    * Construct a new instance based on the provided file name
    * 
    * @param fileName
    * @throws Exception
    */
   public DocumentHolonMatrix(String fileName) throws Exception 
   {
      super(fileName);
      rootElementHelper.setName("matrix");
   }

   /**
    * Create a cell XML element in the cell list
    * 
    * @param cellName
    * @return
    *       holon XML element
    */
   public ElementHolon createCellElement(String cellName) 
   {
      ElementHolon cellElement = new ElementHolon(
            document.createElement(TAG_NAME_CELL)
            );
      cellElement.setName(cellName);
      cellElement.setParentElement(getCellsElement());
      return cellElement;
   }

   /**
    * Get the XML element for the cells list
    * 
    * @return
    *       cell list XML element
    */
   private Element getCellsElement() 
   {
      if (cellsElement == null)
      {
         cellsElement = document.createElement(TAG_NAME_CELLS);
         getRootElement().appendChild(cellsElement);
      }
      return cellsElement;
   }

   /**
    * Create an XML element for a boundary in the boundary list
    * 
    * @param boundaryName
    * @param cellName
    * @return
    *       boundary XML element
    */
   public ElementBoundary createBoundaryElement(String boundaryName,
         String cellName) 
   {
      ElementBoundary boundElement = new ElementBoundary(
            document.createElement(ElementBoundary.TAG_NAME)
            );
      boundElement.setName(boundaryName);
      boundElement.setCell(cellName);
      boundElement.setParentElement(getBoundariesElement());
      return boundElement;
   }

   /**
    * Get the XML element for the boundaries list
    * 
    * @return
    *       boundary list XML element
    */
   private Element getBoundariesElement() 
   {
      if (boundariesElement == null)
      {
         boundariesElement = document.createElement(TAG_NAME_BOUNDARIES);
         getRootElement().appendChild(boundariesElement);
      }
      return boundariesElement;
   }

}
