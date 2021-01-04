 package contabilità;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

public class CacheMovimento implements Serializable{
    private LocalDate dataInizio, dataFine, dataMov;
    private String saldo, email,voce, tipo, importo;
  
    public CacheMovimento(Contabilità cont){
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

     public String toString() { 
        String x = new String();
        x = getDataInizio() + "+" + getDataFine() + "+" + getDataMov()
                + "+" + getSaldo() + "+" + getMail() + "+" + getVoce()
                + "+" + getTipo() + "+" + getImporto();
        return x;
    }
    
    public CacheMovimento toCache(String str, Contabilità cont){
        String[] arr = str.split("+");
        //CacheMovimento cm = null;
        setDataInizio(LocalDate.parse(arr[0]));
        setDataFine(LocalDate.parse(arr[1]));
        setDataMov(LocalDate.parse(arr[2]));
        setSaldo(arr[3]);
        setMail(arr[4]);
        setVoce(arr[5]);
        setTipo(arr[6]);
        setImporto(arr[7]);
        return this;
    } 
    
    private void copyCache(CacheMovimento cache) {
       dataInizio = cache.getDataInizio();
       dataFine = cache.getDataFine();
       dataMov = cache.getDataMov();
       saldo = cache.getSaldo();
       email = cache.getMail();
       voce = cache.getVoce();
       tipo = cache.getTipo();
       importo = cache.getImporto();
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
    
   
    // percorso C:\Users\annam\Desktop\ProgettoContabilita\cache.bin <<< no: 
    //  ...\annam\Documenti\Contabilità\cache.bin al momento
    void saveBin(Contabilità cont) {
        try ( FileOutputStream fout = new FileOutputStream("./cache.bin");//ProgettoContabilita/cache.bin");
            ObjectOutputStream oout = new ObjectOutputStream(fout);) {
           oout.writeObject( new CacheMovimento(cont));
        } catch (IOException ex) {
             System.out.println(ex.getMessage());
        }
   }

   void prelevaBin(Contabilità cont) { 
        try ( FileInputStream fin = new FileInputStream("./cache.bin");//ProgettoContabilita/cache.bin");   
            ObjectInputStream oin = new ObjectInputStream(fin); ) { 
            
            CacheMovimento tmp = (CacheMovimento) oin.readObject();
            copyCache(tmp);

            cont.dataInizio.setValue(dataInizio);
            cont.dataFine.setValue(dataFine);
            cont.dataNewMov.setValue(dataMov);
            cont.importo.setText(importo);
            cont.saldoAttuale = saldo;
            cont.mail.setText(email);
            cont.cbVoce.setValue(voce);
            
            if(tipo == "e")
                cont.entrata.setSelected(true);
            else
                cont.uscita.setSelected(true);

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
   }
/*
   void conservaTxt(Contabilità cont) {
        String x = new CacheMovimento(cont).toString();
        try { 
            Files.write(Paths.get("./ProgettoContabilita/file.txt"), x.getBytes());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
   }

   //era private ora public
    public void prelevaTxt(Contabilità cont) {
        try { 
            String x = new String(Files.readAllBytes(Paths.get("./ProgettoContabilita/file.txt")));
            CacheMovimento cache = toCache(x, cont);
            cont.dataInizio.setValue(dataInizio);
            cont.dataFine.setValue(dataFine);
            cont.dataNewMov.setValue(dataMov);
            cont.importo.setText(importo);
            cont.saldoAttuale = saldo;
            cont.mail.setText(email);
            cont.cbVoce.setValue(voce);
            if(tipo == "e")
                cont.entrata.setSelected(true);
            else
                cont.uscita.setSelected(true);
        }catch (IOException ex) {
             System.out.println(ex.getMessage());
        }
   }
   */
}

