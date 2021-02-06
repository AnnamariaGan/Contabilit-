  
package contabilità;

import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class RigaLogXML implements Serializable{ //00
    private final String lblEvento, ipClient, nomeApp, dataOraCorrente;

    public RigaLogXML(String lblEvento, String ipClient, String nomeApp) { //01
        this.lblEvento = lblEvento;
        this.ipClient = ipClient;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //02
        this.dataOraCorrente = sdf.format(new Date());
        this.nomeApp = nomeApp;
    }
    
     public String toStringXML(){ //03 //////////////////////////////////////////////////////////////////////////////
        XStream xs = new XStream();       //ogni campo [ stato scelto perche si tratta di stringa semplice
        xs.alias("RigaLogXML",RigaLogXML.class);
        //xs.useAttributeFor(RigaLogXML.class, "lblEvento");
        //xs.useAttributeFor(RigaLogXML.class, "ipClient");
        //xs.useAttributeFor(RigaLogXML.class, "nomeApp");
        //xs.useAttributeFor(RigaLogXML.class, "dataOraCorrente"); 
        return xs.toXML(this) + "\n"; //da object a stringa xml  
    }  
}

/*
Note:
    00)La classe RigaLogXMl rappresenta una riga di log che deve essere inviata
        al serve ogni qual volta si apre e si chiude l'applicazione ed ad ogni 
        pressione di pulsante.
    01)Costruttore della classe RigaLogXml, inizializza i campi con i valori 
       passati per argomento.
    02)Inizializza il campo dataOraCorrente creando 
       l'oggettpo Date() che ritorna l'istante di creazione in millisecondi.
       Quindi viene invocato il metodo format di SimpleDateFormat per avere 
       l'uscita personalizzata.
    03) Il metodo toStringXML converte un oggetto di tipo RigaLogXML in una 
       stringa xml. Ogni membro della classe viene considerato un attributo.
       Viene inserito anche un newline per andare accapo.
*/