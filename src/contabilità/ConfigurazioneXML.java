package contabilità;

import java.io.Serializable;

public class ConfigurazioneXML implements Serializable{
    public int dimTesto, numRighe, dimAltezza, dimLarghezza, portaServer, portaDatabase;
    public String font, coloreBackground, ipServer, ipDatabase, ipClient;

    ConfigurazioneXML(String xml, String xsd){
        // TODO: aggiungere controllo su xsd == null
        //         creare il file xml 
        //if(xml == null || xml.compareTo("") == 0){
            dimTesto = 12;
            numRighe = 16;
            dimAltezza = 650;
            dimLarghezza = 790;
            portaServer = 6798; //da verificare
            portaDatabase = 3306;
            
            font = "futura";
            coloreBackground = "WHITE";
            ipServer = "127.0.0.1";
            ipDatabase = "127.0.0.1";
            ipClient = "127.0.0.1";    
            
            // e devo anche cerare il file xml bho
       /* } else{
            //devo validare
           if(ValidaXML.valida(xml,xsd) == true){
                ConfigurazioneXML conf = (ConfigurazioneXML)GestoreXML.creaStream().fromXML(xml); //da xml a java
                dimTesto = conf.dimTesto;
                numRighe = conf.numRighe;
                dimAltezza = conf.dimAltezza;
                dimLarghezza = conf.dimLarghezza;
                portaServer = conf.portaServer;
                portaDatabase = conf.portaDatabase;

                font = conf.font;
                coloreBackground = conf.coloreBackground;
                ipServer = conf.ipServer;
                ipDatabase = conf.ipDatabase;
                ipClient = conf.ipClient;   
           }
        }*/
            
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
}

  