package contabilità;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GestoreLogXML {  //00
    private static final String XSD = "C:\\Users\\annam\\Documents\\Contabilità\\log.xsd";
    private static void invia(RigaLogXML log, String ipServer, int portaServer) { //invia il log al server
        try(Socket socket = new Socket(ipServer, portaServer);  //01
            ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream()); 
        ) {
            String xmlStr = log.toStringXML();
            if(ValidaXML.validaStringa(xmlStr, XSD))
                oout.writeObject(xmlStr);  
        } catch(IOException e) {
            System.err.println("gestoreLog "+ e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void creaLog(String evento, String ipClient, String ipServer, int portaServer) {  //02
        RigaLogXML logxml = new RigaLogXML(evento, ipClient, "My accounting!");
        invia(logxml, ipServer, portaServer);  //03
    } 
}

/**
Note: 
* 00) La classe GestoreLogXML si occupa di creare una riga di log e inviarla al
*     server dopo averla serializzata in stringa XML e validata correttamente.
*  01) Viene creato il Socket con il server, validata la stringa della riga di log
*     e inviata al server.
* 02) La funzione creaLog istanzia un oggetto di tipo RigaLogXML e invoca la
*     funzione invia.
* 03) La funzione invia ha il compito di inviare al server una nuova riga di log
*     ogni qual volta che viene creata.
*/