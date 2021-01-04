package contabilità; 

import com.thoughtworks.xstream.XStream;

public class GestoreXML {
    
    public static XStream creaStream(){
        XStream xs = new XStream(); 
        xs.useAttributeFor(ConfigurazioneXML.class, "font"); 
        xs.useAttributeFor(ConfigurazioneXML.class, "dimTesto");
        xs.useAttributeFor(ConfigurazioneXML.class, "dimLarghezza");
        xs.useAttributeFor(ConfigurazioneXML.class, "dimAltezza");
        xs.useAttributeFor(ConfigurazioneXML.class, "coloreBackground");
        xs.useAttributeFor(ConfigurazioneXML.class, "numRighe");
        xs.useAttributeFor(ConfigurazioneXML.class, "ipClient");
        xs.useAttributeFor(ConfigurazioneXML.class, "ipArchivio");
        xs.useAttributeFor(ConfigurazioneXML.class, "portaServer");
        xs.useAttributeFor(ConfigurazioneXML.class, "portaArchivio");

        return xs;
    }
//questa mi serve se devo creare il file xml
    public String toString(){
        return creaStream().toXML(this); // da java a xml
    }
    
    
    //qua ci vogliono altre funzioni tipo quelle della gestione del file
    
}
