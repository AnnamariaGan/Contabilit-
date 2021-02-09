package contabilità;

public class Controller {  //00
    final String  xml, xsd, fileCache;
    Contabilità cont;
    ConfigurazioneXML config;
    CacheMovimento cache ;
    
    public Controller(Contabilità cont, String xml, String xsd, String fileCache){ //01
        this.xml = xml;
        this.xsd = xsd;
        this.fileCache = fileCache;
     
        this.cont = cont;
    }
    
    public CacheMovimento accensione(){ //02
        config = new ConfigurazioneXML(this.xml, this.xsd);
        GestoreLogXML.creaLog("accensione", config.ipClient, config.ipServer, config.portaServer);
        cache = CacheMovimento.carica(this.fileCache);
        
        return cache;
    } 

    public void chiusura(){ //03
         GestoreLogXML.creaLog("chiusura", config.ipClient, config.ipServer, config.portaServer);
         CacheMovimento.salva(this.cont,this.fileCache);
    }
}

/**
 * NOTE:
 * 00) La classe Controlle viene istanziata in Contabilità.
 * 01) Il costruttore inizializa i campi, in particolare inizializza xml, xsd,
 *      fileCache, e l'oggetto Cache.
 * 02) Metodo accensione: si occupa di istanziare la configurazione, di creare 
 *      e inviare il log e di caricare la cache.
 * 03)Metodo chiusura: si occupa di inviare il log al server e di salvare la cache.
 */


//ACCENSIONE: cache, log, configirazione
//CHIUSURA: cache, log