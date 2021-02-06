package contabilità;

import com.thoughtworks.xstream.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.*;

public final class ConfigurazioneXML {
    public int dimTesto, numRighe, dimAltezza, dimLarghezza, portaServer, portaDatabase;
    public String font, coloreBackground, ipServer, ipDatabase, ipClient;
  
    ConfigurazioneXML(String xml, String xsd){
        boolean validato;
        validato = ValidaXML.valida(xml,xsd);  
        // quella funzione valida che hai copincollato manca di void
          dimTesto = 12;
            dimAltezza = 650;
            dimLarghezza = 790;
            font = "futura";
            coloreBackground = "WHITE";
            
            numRighe = 16;
            ipServer = "127.0.0.1";
            ipDatabase = "127.0.0.1";
            ipClient = "127.0.0.1";
            portaDatabase = 3306;
            portaServer = 5939; // TO DO: da verificare
        //  portaServer = 8080;
            
        if(!xml.equals("") && validato){   //dice che non e valido
            try {
                File xmlFile = new File(xml);
                Object deserialized = creaXStream()
                        .fromXML(new FileInputStream(xmlFile));
                System.out.println(deserialized);
                try {
                ConfigurazioneXML conf = (ConfigurazioneXML) deserialized;
                dimTesto = conf.dimTesto;
                dimAltezza = conf.dimAltezza;
                dimLarghezza = conf.dimLarghezza;
                font = conf.font;
                coloreBackground = conf.coloreBackground;

                numRighe = conf.numRighe;
                ipServer = conf.ipServer;
                ipDatabase = conf.ipDatabase;
                ipClient = conf.ipClient;
                portaDatabase = conf.portaDatabase;
                portaServer = conf.portaServer;
                } catch (Error e) {
                    e.printStackTrace();
                }
                
            } catch(Exception e) {
                e.printStackTrace();
                //System.err.println(e.getMessage());
            }
            
            /*conf.dimTesto = 12;
            conf.dimAltezza = 650;
            conf.dimLarghezza = 790;
            conf.font = "futura";
            conf.coloreBackground = "WHITE";*/      
        } else { 
            System.out.println("Problemi con la configurazione (file di configurazione invalido)");            
        }
        
    }

    public int getDimTesto(){ return dimTesto;}
    public int getNumRighe(){return numRighe;}
    public int getALtezza(){return dimAltezza;}
    public int getLarghezza(){return dimLarghezza;}
    public int getPortaServer(){return portaServer;}
    public int getPortaDB(){return portaDatabase;}
    
    public String getFont(){return font;}
    public String getColor(){return coloreBackground;}
    public String getIpServer(){return ipServer;}
    public String getIpDB(){return ipDatabase;}
    public String getIpClient(){return ipClient;}
    

    public XStream creaXStream(){//00
        XStream xs = new XStream();  //stringa numero semplice e non cambia frequentemente 
        xs.alias("ConfigurazioneXML", ConfigurazioneXML.class);
        xs.useAttributeFor(ConfigurazioneXML.class, "numRighe");
        xs.useAttributeFor(ConfigurazioneXML.class, "ipClient");
        xs.useAttributeFor(ConfigurazioneXML.class, "ipDatabase");
        xs.useAttributeFor(ConfigurazioneXML.class, "ipServer");
        xs.useAttributeFor(ConfigurazioneXML.class, "portaServer");
        xs.useAttributeFor(ConfigurazioneXML.class, "portaDatabase");
        return xs ;
    }
}

  