 
package contabilità;

import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.sql.Timestamp;

public class RigaLogXML implements Serializable{ //01
    private final String lblEvento, ipClient, nomeApp;
    private final Timestamp tm;

     public RigaLogXML(String lblEvento, String ipClient, Timestamp timestamp, String nomeApp) {
        this.lblEvento = lblEvento;
        this.ipClient = ipClient;
        this.tm = timestamp;
        this.nomeApp = nomeApp;
    }
    
     public String toString(){
        XStream xs = new XStream();  //flusso in cui possono viaggiare oggetti xml
        xs.useAttributeFor(RigaLogXML.class, lblEvento);
        xs.useAttributeFor(RigaLogXML.class, ipClient);
        xs.useAttributeFor(RigaLogXML.class, nomeApp);
       // xs.useAttributeFor(RigaLogXML.class, tm);  //questo da errore
        return xs.toXML(this) + "\n";
        
    }
     //dovrei avere una funzione che converte la string in xml no
     //e conservare in un file .
     //questa funzione converte in stringa una rigaxml. 
}

/*
Note:
01) Classe RigaLogXMl 
*/