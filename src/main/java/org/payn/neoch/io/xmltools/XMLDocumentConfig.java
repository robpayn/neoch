package org.payn.neoch.io.xmltools;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.payn.chsm.io.xml.ElementHelper;
import org.payn.chsm.io.xml.ElementHolon;
import org.payn.chsm.io.xml.ElementOutput;
import org.payn.chsm.io.xml.ElementProcessor;
import org.payn.chsm.io.xml.ElementResource;
import org.payn.chsm.io.xml.XMLDocument;
import org.payn.neoch.HolonMatrix;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML document for configuration information for a NEO lite simulation
 * 
 * @author robpayn
 *
 */
public class XMLDocumentConfig extends XMLDocument {
   
   /**
    * Element containing the base holon information
    */
   private ElementHolon holonElem;

   /**
    * Get a holon helper element for the root element
    * 
    * @return
    *       holon element
    */
   public ElementHolon getHolonElement() 
   {
      return holonElem;
   }

   /**
    * Element containing information about the builder
    */
   private ElementBuilder builderElem;

   /**
    * Get the builder element
    * 
    * @return
    *       Element for the builder
    */
   public ElementBuilder getBuilderElement() 
   {
      return builderElem;
   }

   /**
    * List of output elements for the iterator
    */
   private ArrayList<ElementOutput> outputList;
   
   /**
    * Create the config document based on the provided xml file
    * 
    * @param configFile
    *       xml file
    * @throws Exception
    *       if error in reading xml file
    */
   public XMLDocumentConfig(File configFile) throws Exception 
   {
      super(configFile);
      builderElem = new ElementBuilder(rootElementHelper.getFirstChildElement(ElementBuilder.TAG_NAME));
      holonElem = new ElementHolon(rootElementHelper.getFirstChildElement(ElementHolonMatrix.TAG_NAME));
   }

   /**
    * Get the name attribute of the document element
    * 
    * @return
    *       name of the document element
    */
   public String getHolonName() 
   {
      return holonElem.getName();
   }

   /**
    * Get the path root attribute of the document element
    * 
    * @return
    *       path root for the document
    */
   public String getPathRoot() 
   {
      return rootElementHelper.getAttribute("pathroot");
   }

   /**
    * Get the processor element
    * 
    * @return
    *       processor element
    */
   public ElementProcessor getProcessorElement() 
   {
      Element element = rootElementHelper.getFirstChildElement(ElementProcessor.TAG_NAME);
      if (element != null)
      {
         return new ElementProcessor(element);
      }
      else
      {
         return null;
      }
   }

   /**
    * Get the iterator over the output elements
    * 
    * @return 
    *       iterator over output handler elements
    */
   public Iterator<ElementOutput> getOutputIterator() 
   {
      Element outElement = (Element)rootElementHelper.getFirstChildElement("outputters");
      NodeList nodeList = outElement.getChildNodes();
      outputList = new ArrayList<ElementOutput>();
      for(int i = 0; i < nodeList.getLength(); i++)
      {
         if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE &&
               ((Element)nodeList.item(i)).getTagName().equals(ElementOutput.TAG_NAME))
         {
            outputList.add(new ElementOutput((Element)nodeList.item(i)));
         }
      }
      return outputList.iterator();
   }

   /**
    * Add the behaviors in the matrix holon
    * 
    * @param matrix
    */
   public void changeRootBehaviors(HolonMatrix matrix) 
   {
      holonElem.clearBehaviorElements();
      holonElem.createBehaviorElements(matrix);
   }

   /**
    * Create an iterator over the resources
    * 
    * @return
    *       iterator for resources, null if list is inactive or absent
    */
   public Iterator<ElementResource> getResourceIterator() 
   {
      ElementHelper resourceElement = rootElementHelper.getFirstChildElementHelper("resources");
      if (resourceElement != null && resourceElement.isActive())
      {
         NodeList nodeList = resourceElement.getElement().getChildNodes();
         ArrayList<ElementResource> list = new ArrayList<ElementResource>();
         for(int i = 0; i < nodeList.getLength(); i++)
         {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE &&
                  ((Element)nodeList.item(i)).getTagName().equals(ElementResource.TAG_NAME))
            {
               list.add(new ElementResource((Element)nodeList.item(i)));
            }
         }
         return list.iterator();
      }
      else
      {
         return null;
      }
   }

   /**
    * Get an iterator over the logger elements
    * 
    * @return
    *       iterator over logger elements, null if loggers are not specified or inactive
    */
   public Iterator<ElementLogger> getLoggerIterator() 
   {
      ElementHelper loggerElement = rootElementHelper.getFirstChildElementHelper("loggers");
      if (loggerElement != null && loggerElement.isActive())
      {
         NodeList nodeList = loggerElement.getElement().getChildNodes();
         ArrayList<ElementLogger> list = new ArrayList<ElementLogger>();
         for(int i = 0; i < nodeList.getLength(); i++)
         {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE &&
                  ((Element)nodeList.item(i)).getTagName().equals(ElementLogger.TAG_NAME))
            {
               list.add(new ElementLogger((Element)nodeList.item(i)));
            }
         }
         return list.iterator();
      }
      else
      {
         return null;
      }
   }

}
