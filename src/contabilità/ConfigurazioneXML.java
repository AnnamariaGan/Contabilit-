package contabilità;

import com.thoughtworks.xstream.*;
import java.io.File;
import java.io.FileInputStream;

public final class ConfigurazioneXML {  //00
    public int dimTesto, numRighe, dimAltezza, dimLarghezza, portaServer, portaDatabase;
    public String font, coloreBackground, ipServer, ipDatabase, ipClient;
  
    ConfigurazioneXML(String xml, String xsd){
        boolean validato;
        validato = ValidaXML.valida(xml,xsd); //01
        
        if(!validato || xml.equals("") ){  
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
        }
      
        if(!xml.equals("") && validato){  //02
            try {
                File xmlFile = new File(xml);
                Object deserialized = creaXStream().fromXML(new FileInputStream(xmlFile));
                System.out.println(deserialized);//////////////////////////////////////////
                try {
                    ConfigurazioneXML conf = (ConfigurazioneXML) deserialized;///////////////////////////////////////////
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
                } catch (Error e) { e.printStackTrace(); }
            } catch(Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();     
            }    
        } else { 
            System.out.println("Problemi con la configurazione (file di configurazione invalido)");            
        }  
    }

    public int getDimTesto(){ return dimTesto;} //03
    public int getNumRighe(){return numRighe;}
    public int getALtezza(){return dimAltezza;}
    public int getLarghezza(){return dimLarghezza;}
    public int getPortaServer(){return portaServer;}
    public int getPortaDB(){return portaDatabase;}
    
    public String getFont(){return font;} //04
    public String getColor(){return coloreBackground;}
    public String getIpServer(){return ipServer;}
    public String getIpDB(){return ipDatabase;}
    public String getIpClient(){return ipClient;}
    

    public XStream creaXStream(){//05
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

  /*
Note:
00) Classe ConfigurazioneXML, gestisce la configurazione dell'applicazione.
01) Viene chiamata la funzione valida per validare il l'xml di configurazione.
02) Se valida restituisce falso, o la stringa passata è vuota, vengono inizializzati
    i campi con valori di default.
03)Se xml viene validato correttamente, allora vengono settati i campi con i valori
    letti da xml.
04) Metodi get.
05)Funzione creaXStresm resitutisce lo stream di configurazione. Lo stream è un 
oggetto in cui possono passare i dati-----------------------------------------

*/