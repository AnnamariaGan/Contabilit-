package contabilità;
//invia una riga di log ad ogni evento
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;

public class GestoreLogXML {
     private  void invia(RigaLogXML log, String ipServer, int portaServer) { //invia il log al server
        try(Socket socket = new Socket(ipServer, portaServer);
            ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream()); 
        ) {
            if(ValidaXML.valida(log.toString(), "log.xsd") == true)
                oout.writeObject(log.toString());   
        } catch(IOException e) {
            System.err.println("gestoreLog "+e.getMessage());
        }
    }
    
    public void creaLog(String evento, String ipClient, String ipServer, int portaServer) {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        RigaLogXML logxml = new RigaLogXML(evento, ipClient, tm, "My accounting!");
        invia(logxml, ipServer, portaServer);
       
    }
    
  
}
