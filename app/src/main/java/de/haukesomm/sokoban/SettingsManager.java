package de.haukesomm.sokoban;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SettingsManager {
    
    private final String filename = "settings.xml";
    
    public String getValue(String xmlTag) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));
            NodeList nodeList = doc.getElementsByTagName("settings");
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;
                System.out.println("Got settings value from XML-Tag '" + xmlTag + "': " + element.getElementsByTagName(xmlTag).item(0).getTextContent() + "");
                return element.getElementsByTagName(xmlTag).item(0).getTextContent();
            }            
        } catch (IOException i) {
            System.err.println("Missing XML-file: " + filename);
            return null;
        } catch (ParserConfigurationException | SAXException | DOMException e) {
            System.err.println("Error parsing XML-file: " + filename);
            return null;
        } catch (NullPointerException n) {
            System.err.println("Missing XML-tag in " + filename + ": <" + xmlTag + ">...</" + xmlTag + ">");
            return null;
        }
        
        return null;
    }
    
}
