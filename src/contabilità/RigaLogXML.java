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
    
     public String toStringXML(){ //03 
        XStream xs = new XStream();  //04     
        xs.alias("RigaLogXML",RigaLogXML.class); //05   
        return xs.toXML(this) + "\n"; //06 
    }  
}

/*
Note:
    00)La classe RigaLogXML rappresenta una riga di log che deve essere inviata
        al serve ogni qual volta si apre e si chiude l'applicazione ed ad ogni 
        pressione di pulsante.
    01)Costruttore della classe RigaLogXml, inizializza i campi con i valori 
       passati per argomento.
    02)Inizializza il campo dataOraCorrente creando 
       l'oggetto Date() che ritorna l'istante di creazione in millisecondi.
       Quindi viene invocato il metodo format di SimpleDateFormat per avere 
       l'uscita personalizzata.
    03) Il metodo toStringXML converte un oggetto di tipo RigaLogXML in una 
       stringa xml. Ogni membro della classe viene considerato un attributo.
       Viene inserito anche un newline per andare accapo.
    04) Ogni campo di RigaLogXML è stato scelto per xstream poichè si tratta di 
       stringhe semplici.
    05)Il metodo alias per oggetti XStream viene usato perchè può capitare che 
        quando si passa un oggetto a XStream, l' elemento root in xml si chiama in 
        modo diverso dal nome della sua classe e viceversa nel momento in cui si
        deserializza da xml.
    06) Ritorna una Stringa. toXML converte l'oggetto RigaLogXML da oggetto a stringa.
*/