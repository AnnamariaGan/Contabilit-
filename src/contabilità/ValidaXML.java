
package contabilità;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Node;
import javax.xml.transform.dom.*;


import org.w3c.dom.*;

import org.xml.sax.*;
import javax.xml.validation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.*;



public class ValidaXML {
    /*
    public static void valida() {
        try {  
          DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); // (01)
          SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); // (02)
          Document d = db.parse(new File("cantina.xml")); // (03)
          Schema s = sf.newSchema(new StreamSource(new File("cantina.xsd"))); // (04)
          s.newValidator().validate(new DOMSource(d)); // (05)
        } catch (Exception e) {
          if (e instanceof SAXException) 
            System.out.println("Errore di validazione: " + e.getMessage());
          else
            System.out.println(e.getMessage());    
        }
    } 
    */
    
    public static boolean valida(String xml, String xsd) {
        try {  
          DocumentBuilder db = DocumentBuilderFactory.newInstance()
                  .newDocumentBuilder(); // (01)
          
          SchemaFactory sf = SchemaFactory
                  .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); // (02)
          
          Document d =  db.parse(new File(xml)); // (03) 
          Schema s = sf.newSchema(new StreamSource(new File(xsd))); // (04)

          s.newValidator().validate(new DOMSource(d)); // (05)
        } catch(Exception e) {
            if(e instanceof SAXException) // 12)
                System.err.println("Errore di validazione: "); // (13)
            
            //System.err.println(e.getMessage());
            e.printStackTrace();
            return false; // 14)
        }
        
        return true;
    }
    
    public static boolean validaStringa(String xml, String xsd) {
        try {  
          DocumentBuilder db = DocumentBuilderFactory.newInstance()
                  .newDocumentBuilder(); // (01)
          
          SchemaFactory sf = SchemaFactory
                  .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); // (02)
          
           
          Schema s = sf.newSchema(new StreamSource(new File(xsd))); // (04)
          //Document d =  db.parse(xml); // (03)
          s.newValidator().validate(new StreamSource(new StringReader(xml))); // (05)
        } catch(Exception e) {
            if(e instanceof SAXException) // 12)
                System.err.println("Errore di validazione: "); // (13)
            
            //System.err.println(e.getMessage());
            e.printStackTrace();
            return false; // 14)
        }
        
        return true;
    }
}
  


/*
Note:
00)Il metodo valida(), valida una stringa xml e ritorna un booleano,
   in particolare ritorna true se la validazione avvine con successo, altrimenti
   ritrna false.
01)Prepara per la convalida lo schema, secondo lo schema xsd.
02)La funzione validate() convalidata l'input specificato. 
*/