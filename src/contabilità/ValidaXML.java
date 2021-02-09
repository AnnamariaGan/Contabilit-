
package contabilità;

import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import java.io.StringReader;

public class ValidaXML {  
   
    public static boolean valida(String xml, String xsd) {
        try {  
          DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); // 01
          SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); // 02
          Document d =  db.parse(new File(xml)); //03 
          Schema s = sf.newSchema(new StreamSource(new File(xsd))); //04
           s.newValidator().validate(new DOMSource(d)); //05
        } catch(Exception e) {
            if(e instanceof SAXException) 
                System.err.println("Errore di validazione: "); 
            e.printStackTrace();
            return false; 
        }
        
        return true;
    }
    
    public static boolean validaStringa(String xml, String xsd) { //06
        try {  
          DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
          SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
          Schema s = sf.newSchema(new StreamSource(new File(xsd))); 
          s.newValidator().validate(new StreamSource(new StringReader(xml))); 
        } catch(Exception e) {
            if(e instanceof SAXException) 
                System.err.println("Errore di validazione: "); 
            e.printStackTrace();
            return false; 
        }
        return true;
    }
}
  


/*
Note:
01) DocumentBuilderFactory istanzia dei parser che producono oggetti DOM da documenti XML
02) SchemaFactory legge rappresentazioni esterne di schemi, per la validazine
03) Document e' l'oggetto documento DOM vero e proprio caricato dal file XML
04) Schema e' l'oggetto schema vero e proprio caricato da file XSD
05) Crea un oggetto validatore che valida un documento xml sullo schema caricato
    In caso di errore vi validazione viene generata una SAXException.
06) LA funzione validaStringa fa esattamnete quello che fa la fuzione valida, 
    ad eccezione che non viene usato un docuemto poiche -------------------------------------------------------------
*/


