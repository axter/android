package com.axter.tools.android;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DeleteColor {
  public static void main(String[] args) throws DocumentException {
    String path =
        "E:\\WorkSpace\\SVN\\shanpao\\Shanpao_studio\\shanpao\\src\\main\\res\\values\\colors.xml";
    HashMap<String, String> maps = new HashMap<String, String>();

    SAXReader reader = new SAXReader();
    Document document = reader.read(new File(path));
    Element root = document.getRootElement();

    for (Iterator i = root.elementIterator(); i.hasNext();) {
      Element element = (Element) i.next();
      String key = element.getText().toLowerCase();
      String value = element.attributeValue("name");

      if(maps.containsKey(key)){
        System.out.println(maps.get(key)+"|"+value);
      }else{
        maps.put(key, value);
      }
    }
  }
}
