 package contabilità;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

public class CacheMovimento implements Serializable{  
    private LocalDate dataInizio, dataFine, dataMov;
    private String saldo, email,voce, tipo, importo;
  
    public CacheMovimento(Contabilità cont){ //00
       dataInizio = cont.dataInizio.getValue();
       dataFine = cont.dataFine.getValue();
       dataMov = cont.dataNewMov.getValue();
       saldo = cont.saldoAttuale;
       email = cont.mail.getText();
       voce = cont.cbVoce.getValue();
       if(cont.entrata.isSelected() == true )
           tipo = "e";
       else
           tipo = "u";
       importo = cont.importo.getText();
    }

    public final static void salva(Contabilità cont, String file) {  //01
        try(ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(file))) { 
            oout.writeObject(new CacheMovimento(cont));  //02
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
     public final static CacheMovimento carica(String file) {  //03
        try(ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file))) { 
            return (CacheMovimento)oin.readObject(); 
        } catch(IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return null; 
    }
   
    public LocalDate getDataInizio() { return dataInizio; }
    public LocalDate getDataFine() { return dataFine; }
    public LocalDate getDataMov(){ return dataMov; }
    public String getSaldo(){ return saldo; }
    public String getMail(){ return email; }
    public String getVoce(){ return voce; }
    public String getTipo(){ return tipo; }
    public String getImporto(){ return importo; }

    public void setDataInizio( LocalDate d){ dataInizio = d;}  
    public void setDataFine( LocalDate d){ dataFine = d;}
    public void setDataMov( LocalDate d){ dataMov = d;}
    public void setSaldo(String s){ saldo = s ;}
    public void setMail(String m){ email = m;}
    public void setVoce(String v){voce = v;}
    public void setTipo(String t){tipo = t;}
    public void setImporto(String i){importo = i;}
}

/*
  00) L'oggetto CacheMovimento deve essere salvato all'avvio e alla chiusura 
       dell'applicazione. 
       All'avvio deve caricare i dati inseriti dall'utente precedentemente.
       Alla chiusura deve salvare i dati che l'untente ha inserito prelevandoli 
       da Contabilita.
 01) Il metodo salva(), salva la cache degli input in un file passato per 
    argomento prelevando i dai da contabilita.
 02) La scrittura sul file avviene creando nel frattempo un oggetto CacheMovimento.
 03) Il metodo carica, carica appunto dal file binario la cahce degli input di una 
    esecuzione precedente. Verrà cosi rispristinata l'interfaccia.
    Il metodo restituisce l'oggetto di tipo CacheMovimento 
    letto dal file. In caso di errore restituisce null.

*/