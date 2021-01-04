
package contabilità;

import java.io.*;
import javax.xml.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.xml.sax.*;

public class ValidaXML {
  
    public static boolean valida(String xsd, String xml){
        try {
            SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xml)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }
}

/*
Note:


*/