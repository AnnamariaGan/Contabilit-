
package contabilità;

public class Controller {
    final String  xml, xsd, fileCache;
    Contabilità cont;
    ConfigurazioneXML config;
    CacheMovimento cache ;
    
    public Controller(Contabilità cont, String xml, String xsd, String fileCache){
        this.xml = xml;
        this.xsd = xsd;
        this.fileCache = fileCache;
     
        this.cont = cont;
    }
    
    public CacheMovimento accensione(){
        config = new ConfigurazioneXML(this.xml, this.xsd);
        GestoreLogXML.creaLog("accensione", config.ipClient, config.ipServer, config.portaServer);
        cache = CacheMovimento.carica(this.fileCache);
        
        return cache;
    } 

    public void chiusura(){ 
         GestoreLogXML.creaLog("chiusura", config.ipClient, config.ipServer, config.portaServer);
         CacheMovimento.salva(this.cont,this.fileCache);
    }
}



//ACCENSIONE: cache, log, configirazione
//CHIUSURA: cache, log