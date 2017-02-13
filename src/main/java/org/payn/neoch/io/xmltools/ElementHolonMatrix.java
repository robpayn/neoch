package org.payn.neoch.io.xmltools;

import java.util.ArrayList;
import java.util.Iterator;

import org.payn.chsm.Behavior;
import org.payn.chsm.io.xml.ElementBehavior;
import org.payn.chsm.io.xml.ElementHolon;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A holon element that can handle behaviors that are part of a resource
 * 
 * @author robpayn
 *
 */
public class ElementHolonMatrix extends ElementHolon {

   /**
    * Construct a new instance based on the provided XML element
    * 
    * @param element
    *       XML element associated with the holon element
    */
   public ElementHolonMatrix(Element element) 
   {
      super(element);
   }
   
   /**
    * Convenience method equivalent to calling createBehaviorElment(behavior, true)
    * 
    * @param behavior
    * @return
    *       a matrix behavior element
    *       
    */
   public ElementBehaviorMatrix createBehaviorElement(Behavior behavior)
   {
      return createBehaviorElement(behavior, true);
   }
   
   /**
    * Overrides the super method to return the matrix based type
    */
   @Override
   public ElementBehaviorMatrix createBehaviorElement(Behavior behavior, boolean install) 
   {
      ElementBehaviorMatrix element =
            (ElementBehaviorMatrix)super.createBehaviorElement(behavior, install);
      return element;
   }   
   
   /**
    * Overrides the super method to create the matrix based type
    */
   @Override
   protected ElementBehaviorMatrix newBehaviorInstance(Element element) 
   {
      return new ElementBehaviorMatrix(element);
   }

   /**
    * Overrides the super method to provide an iterator over the matrix types
    * 
    * @return 
    *       iterator
    */
   @Override
   public Iterator<ElementBehaviorMatrix> iterator() 
   {
      NodeList nodeList = element.getChildNodes();
      ArrayList<ElementBehaviorMatrix> list = new ArrayList<ElementBehaviorMatrix>();
      for (int i = 0; i < nodeList.getLength(); i++)
      {
         if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE &&
               ((Element)nodeList.item(i)).getTagName().equals(ElementBehavior.TAG_NAME))
         {
            list.add(newBehaviorInstance((Element)nodeList.item(i)));
         }
      }
      return list.iterator();
   }
   
}
