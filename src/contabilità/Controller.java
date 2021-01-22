
package contabilità;

public class Controller {
    public CacheMovimento cacheMov;
    
    public ConfigurazioneXML config;
    public static GestoreLogXML gestorelog;
    
   Controller(Contabilità cont){
        cacheMov = new CacheMovimento(cont);
        config = new ConfigurazioneXML(null,null);
        gestorelog = new GestoreLogXML();
    }
   
    public CacheMovimento accensione(String ipClient, String ipServer,
            int portaServer, Contabilità cont, String xml, String xsd){
        
        cacheMov.prelevaBin(cont);
        cacheMov.saveBin(cont); 
                
        cacheMov.conservaTxt(cont);   
        
        config = new ConfigurazioneXML(xml, xsd);
       
        //gestorelog.creaLog("accensione", ipClient, ipServer,  portaServer);
        return this.cacheMov;
    }

    public void chiusura(Contabilità cont,String ipClient,String ipServer,int portaServer ){ 
      //   cacheMov.prelevaTxt(cont);
         cacheMov.prelevaBin(cont);
         cacheMov.saveBin(cont);
         //null pointer EXECPTION!!!!!!
        // gestorelog.creaLog("chiusura", ipClient, ipServer,  portaServer); 
    }
}
