package contabilità;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Movimento {  //01
    private  SimpleStringProperty data; //02
    private  SimpleDoubleProperty importo;
    private  SimpleStringProperty voce;
    private  SimpleStringProperty email;
    private  SimpleStringProperty tipo;
 
    Movimento(String data, double importo, String voce, String email,  
            String tipo) {      
        this.data = new SimpleStringProperty(data);
        this.importo = new SimpleDoubleProperty(importo);
        this.voce = new SimpleStringProperty(voce);
        this.email = new SimpleStringProperty(email);
        this.tipo = new SimpleStringProperty(tipo);
    } 

    public String getData() { return data.get(); }  //03
    public Double getImporto() { return importo.get(); } 
    public String getVoce(){ return voce.get(); }
    public String getEmail() { return email.get(); }
    public String getTipo(){return tipo.get();}
    
    public void setData(String d) { data = new SimpleStringProperty(d); }  //04
    public void setImporto(Double i) { importo = new SimpleDoubleProperty(i); } 
    public void setVoce(String s){ voce = new SimpleStringProperty(s); }
    public void setEmail(String s) { email = new SimpleStringProperty(s); }
    public void setTipo(String s){ tipo = new SimpleStringProperty(s); }
    
    //converte il movimento in stringa separata da +
    // non so ancora a che mi serve pero perche per accensione e spagnimento non mi 
    //serve questo ma creare una nuova stringa presa dai campi
    public String toString() {  //05
        String x = new String();
        x = getData() + "+" + Double.toString(getImporto()) + "+" + getVoce() + "+" + getEmail() + "+" + getTipo();
        return x;
    }
    
}


/*
Note:
01) Classe bean, che serve per tenere sincronizzata l`interfaccia con i dati presenti nel database.
02) Campi della classe Movimento dotati di osservabilita`.
03) Medoti get che consentono di ottenere il valore di ogni singolo campo della classe.
04) Metodi set che consentono di settare il valore di ogni singolo campo della classe.
05) Funzione che converte in stringa l`oggetto movimento.
*/