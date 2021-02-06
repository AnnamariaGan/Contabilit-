
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
     private final static String
        FILE_CONFIGURAZIONE = "C:\\Users\\annam\\Documents\\Contabilità\\configurazione.xml",
        SCHEMA_CONFIGURAZIONE = "C:\\Users\\annam\\Documents\\Contabilità\\configurazione.xsd",
        FILE_CACHE = "C:\\Users\\annam\\Documents\\Contabilità\\cache.bin";
    
    DatePicker dataInizio = new DatePicker(); //02
    DatePicker dataFine = new DatePicker();     
    DatePicker dataNewMov = new DatePicker(); 
  
    TextField mail = new TextField(); 
    TextField importo = new TextField(); 
    
    String saldoAttuale = new String(" ");
    
    RadioButton entrata = new RadioButton("entrata");
    RadioButton uscita = new RadioButton("uscita");

    Button cancella = new Button();
     
    ObservableList<String> voceTypes =  FXCollections.observableArrayList("Stipendio",
        "Affitto", "Cure Mediche", "Spesa", "Altro");
    final ComboBox<String> cbVoce = new ComboBox<String>(voceTypes);
    ObservableList<Movimento> voci = FXCollections.observableArrayList();   //questa per il grafico

    TableView<Movimento>  tabella = new TableView<>(voci);
    
    Movimento daCancellare = null;
    
    boolean validaMail(String email) { 
        int atIndex = email.indexOf('@');
        int dotIndex = email.indexOf('.',atIndex);
        if(atIndex != -1 && dotIndex != -1 && dotIndex < email.length()-1){
            return true;
        }else{ System.out.println("email non valida, riprova"); }
        return false;    
    }
    
    @Override
    public void start(Stage primaryStage) { //03
        
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

        Controller ctrl = new Controller(this, FILE_CONFIGURAZIONE, SCHEMA_CONFIGURAZIONE, FILE_CACHE); //04
        CacheMovimento cache = ctrl.accensione();
        ArchivioMov archivio = new ArchivioMov(ctrl.config.getIpDB(), ctrl.config.getPortaDB());

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
                if(daCancellare != null) {
                    archivio.deleteMovimento(daCancellare, voci, valueList); //la voce selezionata
                 //   voci.remove(daCancellare);  /questa proprio no
                    //tabella.getItems().removeAll(daCancellare);
                    saldoAttuale = archivio.calcolaSaldo(mail.getText()) + " €";
                    SaldoDaMostrare.setText(saldoAttuale);
                    tabella.getItems().clear();
                    archivio.loadMovimento(voci, mail.getText(), dataInizio.getValue(),
                            dataFine.getValue(), valueList);     //perche passo value list//////////////////////////////////
                    
                    GestoreLogXML.creaLog("cancella", ctrl.config.getIpClient(),
                        ctrl.config.getIpServer(), ctrl.config.getPortaServer());
                    
                    System.out.println("cancella handled");
                    cancella.setDisable(true);
                } else {
                    System.out.println("daCancellare = null");
                }              
            }
        });
        
        Button inserisci = new Button();
        inserisci.setText("INSERISCI");
        inserisci.setOnAction(new EventHandler<ActionEvent>() {  //08
            @Override 
            public void handle(ActionEvent e) {
                String utente = mail.getText();
                validaMail(utente);
                // TO DO: aggiungere l'utente al db se non e` presente
                String tipo = "u";  
                if( entrata.isSelected() ){
                    tipo = "e";
                }
                String voce = (String) cbVoce.getValue(); 
                
                try {
                    double imp = Double.parseDouble(importo.getText());;
                    String date = dataNewMov.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Movimento newMov = new Movimento(date, imp, voce, utente, tipo);
                    
                    archivio.setMovimento(newMov, voci, valueList);
                    saldoAttuale = archivio.calcolaSaldo(utente) + " €";
                    SaldoDaMostrare.setText(saldoAttuale);
                    
                    tabella.getItems().clear();
                    archivio.loadMovimento(voci, mail.getText(), dataInizio.getValue(),
                        dataFine.getValue(), valueList);   
                   
                    GestoreLogXML.creaLog("inserisci", ctrl.config.getIpClient(),
                        ctrl.config.getIpServer(), ctrl.config.getPortaServer());    
                } catch (NullPointerException  | NumberFormatException exception) {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                };
            }
        });

        Button svuota = new Button();
        svuota.setText("SVUOTA");
        svuota.setOnAction(new EventHandler<ActionEvent>() { //09
            @Override 
            public void handle(ActionEvent e) {
                dataFine.setValue(LocalDate.now());
                dataInizio.setValue(LocalDate.now().plusDays(-30));
                tabella.getItems().clear();
                pieChart.getData().clear();
                saldoAttuale =  "0.00 €";
                SaldoDaMostrare.setText(saldoAttuale);
                mail.clear();
                
                GestoreLogXML.creaLog("svuota", ctrl.config.getIpClient(),
                   ctrl.config.getIpServer(),  ctrl.config.getPortaServer());
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
     
                GestoreLogXML.creaLog("annulla", ctrl.config.getIpClient(), 
                    ctrl.config.getIpServer(),  ctrl.config.getPortaServer());
             }
         });
        
        mail.setOnKeyPressed(new EventHandler<KeyEvent>(){ //11
           @Override 
            public void handle(KeyEvent kevent){
                if(kevent.getCode().equals(KeyCode.ENTER)){
                    String email = mail.getText();
                    tabella.getItems().clear();
                    if (validaMail(email)) {
                        archivio.loadMovimento(voci, email,
                                dataInizio.getValue(), dataFine.getValue(),
                                valueList);
                        saldoAttuale = archivio.calcolaSaldo(mail.getText())
                                + " €";
                        SaldoDaMostrare.setText(saldoAttuale);
                    }
                } 
            } 
        });
       
        TableColumn dataCol = new TableColumn("DATA");     //12
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        TableColumn categoriaCol = new TableColumn("CATEGORIA");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("voce"));
        
        TableColumn importoCol = new TableColumn("IMPORTO");
        importoCol.setCellValueFactory(new PropertyValueFactory<>("importo"));
       
        dataCol.setPrefWidth(150); //13
        categoriaCol.setPrefWidth(165);
        importoCol.setPrefWidth(150);
       
        tabella.getColumns().addAll(dataCol, categoriaCol, importoCol);
     
        tabella.setFixedCellSize(25); 
        tabella.prefHeightProperty().set((ctrl.config.getNumRighe() + 1) * 25); 
        tabella.setStyle("-fx-background-color:" + ctrl.config.getColor());
          
        importoCol.setCellFactory(column -> {
            return new TableCell<Movimento, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty); 
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else { 
                        setText(item.toString()); 
                    //getting info of this row's Movimento
                        Movimento mov = getTableView().getItems().get(getIndex());
                        if (mov.getTipo().equals("u")) {
                            setTextFill(Color.RED);                                                   
                        } else {
                            setTextFill(Color.GREEN);
                        }                                                                                                                                                                                                    
                    }
                }
            };
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
            ctrl.chiusura(); //20
            System.out.println("Closing...");
        });
        
        tabella.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
                    cancella.setDisable(false);
                    daCancellare = (Movimento) tabella.getSelectionModel()
                            .getSelectedItem();
                    
                    System.out.println("double click");
                }
            }
        });
        
        if(cache == null){ 
            dataInizio.setValue(LocalDate.now().plusDays(-30)); //06
            dataFine.setValue(LocalDate.now()); // 06) 
        } else {
            dataInizio.setValue(cache.getDataInizio());
            dataFine.setValue(cache.getDataFine());
            dataNewMov.setValue(cache.getDataMov());
            saldoAttuale = cache.getSaldo();
            mail.setText(cache.getMail());
            cbVoce.getSelectionModel().select(cache.getVoce());
            if(cache.getTipo().equals("e")){
                entrata.fire();
            } else {
                uscita.fire();
            }
            importo.setText(cache.getImporto());
            String mailUtil = cache.getMail();
             // svuota in ogni caso la tabella
             tabella.getItems().clear();
            if (validaMail(mailUtil)) {
                archivio.loadMovimento(voci, mailUtil,
                         dataInizio.getValue(), dataFine.getValue(), valueList);
                saldoAttuale = archivio.calcolaSaldo(mail.getText()) + " €";
                SaldoDaMostrare.setText(saldoAttuale);  
            }
        }   
    }
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