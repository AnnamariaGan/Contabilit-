 /* package contabilità; 

import com.thoughtworks.xstream.*;

public class GestoreXML {
    
   public XStream creaXStream(){//00
        XStream xs = new XStream();  //stringa numero semplice e non cambia frequentemente 
        xs.useAttributeFor(ConfigurazioneXML.class, "numRighe");
        xs.useAttributeFor(ConfigurazioneXML.class, "ipClient");
        xs.useAttributeFor(ConfigurazioneXML.class, "ipDatabase");
        xs.useAttributeFor(ConfigurazioneXML.class, "ipServer");
        xs.useAttributeFor(ConfigurazioneXML.class, "portaServer");
        xs.useAttributeFor(ConfigurazioneXML.class, "portaDatabase");
        return xs ;
    }   */
  /*  
    public static String toStringXML(ConfigurazioneXML c) { //01  //////////////////////////////////////////////ma mi serve bho
        return creaXStream().toXML(c); //stringa xml
    }
    */
// }

/**
 * 00)Il metodo creaXStrem() ritorna lo stream di un oggetto di tipo
 *  ConfigurazioneXML così da porter deserializzare e desializzare l'oggetto.
 *  Ogni membro della classe viene considerato un attributo.
   01) Il metodo toString() ritorna la stringa in xml di un oggetto di tipo
   * ConfigurazioneXML utilizzando il metodo sopra descritto: creaXStrema().
 */