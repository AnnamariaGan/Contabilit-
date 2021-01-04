
package contabilità;

import javafx.scene.input.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.collections.FXCollections;  
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;   
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Contabilità extends Application {  //01
 
    DatePicker dataInizio = new DatePicker(); //02
    DatePicker dataFine = new DatePicker();     
    DatePicker dataNewMov = new DatePicker(); 
  
    TextField mail = new TextField(); 
    TextField importo = new TextField(); 
    
    String saldoAttuale = new String(" ");
    
    RadioButton entrata = new RadioButton("entrata");
    RadioButton uscita = new RadioButton("uscita");

    Button cancella = new Button();
     
    ObservableList<String> voceTypes = 
    FXCollections.observableArrayList(
        "Stipendio",
        "Affitto",
        "Cure Mediche",
        "Spesa",
        "Altro"
    );
    final ComboBox<String> cbVoce = new ComboBox<String>(voceTypes);
    ObservableList<Movimento> voci = FXCollections.observableArrayList();   //questa per il grafico

    TableView<Movimento>  tabella = new TableView<>(voci);
    
    Movimento daCancellare = null;
    
    @Override
    public void start(Stage primaryStage) { //03
    
        Contabilità cont = this;//new Contabilità(); //04
        Controller ctrl = new Controller(cont);
        //ArchivioMov archivio = new ArchivioMov(ctrl.config.getIpDB(), ctrl.config.getPortaDB());
        
        ctrl.accensione(ctrl.config.getIpClient(), ctrl.config.getIpServer(), 
             ctrl.config.getPortaServer(), cont, "configurazione.xml", "configurazione.xsd");
       
        
        dataFine.setValue(LocalDate.now()); // 06)
        dataInizio.setValue(LocalDate.now().plusDays(-30));

        Label email = new Label("E-mail");
        email.setFont(new Font("Arial", 18));

        Label Importo = new Label("IMPORTO"); 
        Importo.setFont(new Font(ctrl.config.getFont(), ctrl.config.getDimTesto()));
       
        Label SaldoAVideo = new Label("SALDO: ");
        SaldoAVideo.setFont(new Font(ctrl.config.getFont(), 16));
      
        Label SaldoDaMostrare = new Label(saldoAttuale); 
        SaldoDaMostrare.setFont(new Font(ctrl.config.getFont(),16));
       
        Label Da = new Label("Da: ");
        Da.setFont(new Font(ctrl.config.getFont(), ctrl.config.getDimTesto()));
       
        Label Al = new Label("Al: ");
        Al.setFont(new Font(ctrl.config.getFont(), ctrl.config.getDimTesto()));
       
        Label Data = new Label("Data");
        Data.setFont(new Font(ctrl.config.getFont(), ctrl.config.getDimTesto()));
        
        ToggleGroup group = new ToggleGroup();
        entrata.setToggleGroup(group);
        uscita.setToggleGroup(group);

        Label bilancio = new Label("BILANCIO");
        bilancio.setFont(new Font("Arial", 24));
        bilancio.setTextFill(Color.BLUE);  

        cbVoce.setValue("seleziona voce");

        cancella.setText("CANCELLA");
        cancella.setDisable(true);
        
        cancella.setOnAction(new EventHandler<ActionEvent>() { //07
            @Override 
            public void handle(ActionEvent e) {
                if(daCancellare != null)
                 ;//   archivio.deleteMovimento(daCancellare, voci); //la voce selezionata
                
                saldoAttuale = "0";
                //archivio.calcolaSaldo(mail.getText()) + "  €";
                ctrl.gestorelog.creaLog("cancella", ctrl.config.getIpClient(), ctrl.config.getIpServer(),  ctrl.config.getPortaServer());
               
            }
        });
        
        Button inserisci = new Button();
        inserisci.setText("INSERISCI");
        inserisci.setOnAction(new EventHandler<ActionEvent>() {  //08
            @Override 
            public void handle(ActionEvent e) {
                String utente = mail.getText();
                String tipo = "u";
                if( entrata.isSelected() == true){
                    tipo = "e";
                }
                String voce = (String) cbVoce.getValue(); //conevrtito in stringa
                double imp = Double.parseDouble(importo.getText());
                String date = dataNewMov.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
                Movimento newMov = new Movimento(date, imp, voce, utente, tipo);  
               
                //archivio.setMovimento(newMov, voci);
                saldoAttuale = "0";// archivio.calcolaSaldo(utente) + " €";
                
                ctrl.gestorelog.creaLog("inserisci", ctrl.config.getIpClient(), 
                    ctrl.config.getIpServer(),  ctrl.config.getPortaServer());
            }
        });

        Button svuota = new Button();
        svuota.setText("SVUOTA");
        svuota.setOnAction(new EventHandler<ActionEvent>() { //09
            @Override 
            public void handle(ActionEvent e) {
                dataFine.setValue(LocalDate.now());
                dataInizio.setValue(LocalDate.now().plusDays(-30));
                //deve svuotare anche la tabella
                 saldoAttuale =  "0.00 €";
                
                ctrl.gestorelog.creaLog("svuota", ctrl.config.getIpClient(), ctrl.config.getIpServer(),  ctrl.config.getPortaServer());
             
            }
        });

        Button annulla = new Button();
        annulla.setText("ANNULLA"); 
        annulla.setOnAction(new EventHandler<ActionEvent>() { //10
             @Override
             public void handle(ActionEvent e) {
                dataNewMov.setValue(null);
                cbVoce.getSelectionModel().clearSelection();
                importo.setText("");
                entrata.setSelected(false);
                uscita.setSelected(false);
                cancella.setDisable(true);
     
                ctrl.gestorelog.creaLog("annulla", ctrl.config.getIpClient(), 
                        ctrl.config.getIpServer(),  ctrl.config.getPortaServer());
             }
         });
        
        mail.setOnKeyPressed(new EventHandler<KeyEvent>(){ //11
           @Override 
            public void handle(KeyEvent kevent){
                if(kevent.getCode().equals(KeyCode.ENTER)){
                    String email = mail.getText();
                    int atIndex = email.indexOf('@');
                    int dotIndex = email.indexOf('.',atIndex);
                    if(atIndex != -1 && dotIndex != -1 && dotIndex < email.length()-1){
                       //archivio.loadMovimento(voci,email, dataInizio.getValue(), dataFine.getValue());
                        saldoAttuale = "0";//archivio.calcolaSaldo(mail.getText()+ " €");
                    }else{
                        System.out.println("email non valida, riprova");
                    }
                } 
            } 
        });
       
        TableColumn dataCol = new TableColumn("DATA");     //12
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        TableColumn categoriaCol = new TableColumn("CATEGORIA");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        TableColumn importoCol = new TableColumn("IMPORTO");
        importoCol.setCellValueFactory(new PropertyValueFactory<>("importo"));
       
        dataCol.setPrefWidth(150); //13
        categoriaCol.setPrefWidth(165);
        importoCol.setPrefWidth(150);
        
        tabella.getColumns().addAll(dataCol, categoriaCol, importoCol);
      //  tabella.lookup(".scroll-bar:vertical");
      //  tabella.setStyle(ctrl.config.getColor());
      //  impostare il numro di rghe presneti in tabella con congif
  
        ObservableList<PieChart.Data> valueList;  //14
        valueList = FXCollections.observableArrayList( 
             new PieChart.Data("Stipendio",10),   
             new PieChart.Data("Altro",10),     
             new PieChart.Data("Affitto",10 ),
             new PieChart.Data("Viaggi",10 ),
             new PieChart.Data("Cure mediche",10),
             new PieChart.Data("Spesa",10)   //parametri di default
         );  

        PieChart pieChart = new PieChart(valueList); //15
        pieChart.getData().forEach(dato -> {
            String percentage = String.format("%.2f%%", (dato.getPieValue() / 100));
            Tooltip toolTip = new Tooltip(percentage);
            Tooltip.install(dato.getNode(), toolTip);
        });

        GridPane root = new GridPane(); //16
        root.add(email, 0, 0);
        root.add(mail, 1, 0);
        root.add(Da, 0, 1);
        root.add(Al, 1, 1);
        root.add(dataInizio, 0, 2);
        root.add(dataFine, 1, 2);
        root.add(bilancio, 0, 3);
        root.add(tabella, 0,7, 8,9);
        root.add(SaldoAVideo, 0, 5);
        root.add(SaldoDaMostrare, 1, 5);
        root.add(pieChart, 3, 7,8,9); 
        root.add(cancella, 0, 17);
        root.add(svuota, 1,17);
        root.add(entrata, 1, 19);
        root.add(uscita, 1, 20);
        root.add(Importo, 8, 18); 
        root.add(importo, 8, 19);
        root.add(annulla, 8, 20);
        root.add(inserisci, 9, 20);
        root.add(Data, 9, 18);
        root.add(dataNewMov, 9, 19);
        root.add(cbVoce, 2, 19);  

        Scene scene = new Scene(root, ctrl.config.getLarghezza(), ctrl.config.getALtezza()); //17
        primaryStage.setTitle("My accounting!");
        primaryStage.setScene(scene);
        primaryStage.show(); //18)
        
        
        primaryStage.setOnCloseRequest((WindowEvent we) -> { //19
          ctrl.chiusura(cont, ctrl.config.getIpClient(), ctrl.config.getIpServer(), 
                   ctrl.config.getPortaServer()); //20
        });
     }
 
    public Movimento onTableDoubleClick(MouseEvent me){ //21
            if(me.getClickCount() == 2){
                cancella.setDisable(false);
                daCancellare = (Movimento) tabella.getSelectionModel().getSelectedItem();
            }  
            return daCancellare;
        }; 
     
}

/*
Note:

01) Applicazione Java con interfaccia grafica realizzata estendendo la classe Application
02) Componenti grafici di Java FX.
03) Metodo start che viene chiamato automaticamente all'avvio, consente di inizializzare 
    l'applicazione.
04) Istanziate le classi: Contabilità, Controller, Archivio.
05) Invocata la funzione  ctrl.accensione(), che carica la cache e i parametri di configurazione;
    invia la riga di log.
06) Settati con valori di default e con i paramteri di configurazione i componenti grafici.
07) Gestione evento CANCELLA.
08) Gestione evento INSERISCI.
09) Gestione evento SVUOTA.
10) Gestione evento ANNULLA.
11) Gestione evento INVIO in textField mail.
12) Colonne della tabella.
13) Fissate dimensioni delle colonne della tabella.
14) Lista  delle componenti del diagramma a torta.
15) Istanziato componente grafico del diagramma a torta.
16) GridPane: contenitore a griglia per gli oggetti osservabili dentro la scena.
17) Scene: contenitore principale della finestra.
18) Mostra la finestra.
19)Gestione evento CHIUSURA.
20) Invocata la funzione  ctrl.chiusura(), che salva la cache e invia la riga di log.
21) Funzione che gestisce il doppio click su una riga della tabella.
*/