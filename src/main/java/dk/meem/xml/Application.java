package dk.meem.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Application {

    public static void main( String[] args ) throws IOException, SAXException, ParserConfigurationException,  XPathExpressionException {
    	String query = args[2];
    	
    	XPath xpath = buildXPath();

    	//String query = "//body[@status=\"accepted\"]";
    	//String query = "//body[@status=\"rejected\"]/text()";
    	//String query = "/note/body/text()";

    	Document doc = getDoc("note.xml", "note.xsd");

    	// Get just one hit:
    	//String result = xpath.evaluate(xp2, doc);
    	//System.out.println(result);
    	
    	// Get multiple hits:
    	XPathExpression expr = xpath.compile(query);
    	    	
    	NodeList list= (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    	System.out.println(list.getLength() + " hits for expr: " + "[" + query + "]");

    	for (int i = 0; i < list.getLength(); i++) {
    	    Node node = list.item(i);
    	    System.out.print(getFullpath(node) + ": ");
    	    System.out.println(node.getTextContent());
    	}

    }
    
    public static String getFullpath(Node n) {
    	String result="";
    	
    	while (n.getParentNode() != null) {
    		result = n.getNodeName() + "/" + result;
    		n = n.getParentNode();
    	}
    	
    	return result;
    	
    }
    
    
    public static Document getDoc(String xmlfilename, String xsdPath) throws IOException, SAXException, ParserConfigurationException {
    	DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
    	SchemaFactory factory = 
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(xsdPath));
        docbf.setSchema(schema);

    	DocumentBuilder docbuilder = docbf.newDocumentBuilder();
    	Document document = docbuilder.parse(new File(xmlfilename));
    	
    	return document;
    }

    public static XPath buildXPath() {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        //xpath.setNamespaceContext(new AtomNamespaceContext());
        return xpath;
    }

}
