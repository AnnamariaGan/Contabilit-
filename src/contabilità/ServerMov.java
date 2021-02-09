 
package contabilità;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMov {
    //server multithread
    private static int n = 0; 
    
    public static void main(String[] args) {
        try ( ServerSocket servs = new ServerSocket(5939, 7) ){ //01
            while(true) {
                Socket s = servs.accept();
                Thread t = new Thread() {  //02
                    public void run() {  
                        try ( ObjectInputStream oin = new ObjectInputStream(s.getInputStream())) {
                            System.out.println(++n + ") ricevuto: " + (int) oin.readObject()); 
                            Thread.sleep(300000); //03
                        } catch (Exception e) {e.printStackTrace();}
                    }
                };
                t.start(); //04
            }
        } catch (IOException e) {e.printStackTrace();}
    }  
}

/*
Note:
01) Crea un TCP server socket, in grado di ricevere richieste di connessione e
    restituire un socket. Il parametro 7 è la coda di backlog.
02) Dopo ogni richiesta viene lanciato un thread per gestirla.
03) Attesa di 5 minuti, sufficiente per creare il massimo numero di thread e far
    scattare poi l'eccezione
04) Il metodo start() fa partire il nuovo thread.
*/