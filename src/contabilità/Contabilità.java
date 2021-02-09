package contabilità;

import javafx.scene.input.KeyEvent; 
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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


public class Contabilità extends Application {  //00
     private final static String
        FILE_CONFIGURAZIONE = "C:\\Users\\annam\\Documents\\Contabilità\\configurazione.xml",
        SCHEMA_CONFIGURAZIONE = "C:\\Users\\annam\\Documents\\Contabilità\\configurazione.xsd",
        FILE_CACHE = "C:\\Users\\annam\\Documents\\Contabilità\\cache.bin";
    
    DatePicker dataInizio = new DatePicker(); //01
    DatePicker dataFine = new DatePicker();     
    DatePicker dataNewMov = new DatePicker(); 
  
    TextField mail = new TextField(); 
    TextField importo = new TextField(); 
    
    String saldoAttuale = new String(" ");
    
    RadioButton entrata = new RadioButton("entrata");
    RadioButton uscita = new RadioButton("uscita");

    Button cancella = new Button();
     
    ObservableList<String> voceTypes =  FXCollections.observableArrayList("Stipendio",
        "Affitto", "Cure Mediche", "Spesa", "Altro", "Viaggi");
    final ComboBox<String> cbVoce = new ComboBox<String>(voceTypes);
    ObservableList<Movimento> voci = FXCollections.observableArrayList();  

    TableView<Movimento>  tabella = new TableView<>(voci);
    
    Movimento daCancellare = null;
    
    private boolean validaMail(String email) {  //02
        int atIndex = email.indexOf('@');
        int dotIndex = email.indexOf('.',atIndex);
        if(atIndex != -1 && dotIndex != -1 && dotIndex < email.length()-1){
            return true;
        }else{ 
            System.out.println("email non valida, riprova");
        }
        return false;    
    }
    
    private void dateHandlerHelper(ArchivioMov archivio,
            Label SaldoDaMostrare, ObservableList<PieChart.Data> valueList) {
        String email = mail.getText();
        if(email == null)
            System.err.println("On dataInizio action's handler: email is null.");
        
        else if (dataInizio.getValue() == null)
            System.err.println("On dataInizio action's handler: value of dataInizio is null.");
        
        else if (dataFine.getValue() == null)
            System.err.println("On dataInizio action's handler: value of dataFine is null."); 
        
        else if (email.equals(""))
            System.out.println("Inserire email");
        
        else if (validaMail(email)) {
            tabella.getItems().clear();
            archivio.loadMovimento(voci, email, dataInizio.getValue(),
                    dataFine.getValue(), valueList);
            saldoAttuale = archivio.calcolaSaldo(email) + " €";
            SaldoDaMostrare.setText(saldoAttuale);

            System.out.println("Cambio data riuscito.");
        } else 
            System.out.println("Cambio data non riuscito, email invalida.");

    }
    
    @Override
    public void start(Stage primaryStage) {  //03
        
        ObservableList<PieChart.Data> valueList;  //04
        valueList = FXCollections.observableArrayList( 
            new PieChart.Data("Stipendio",10),   
            new PieChart.Data("Altro",10),     
            new PieChart.Data("Affitto",10 ),
            new PieChart.Data("Viaggi",10 ),
            new PieChart.Data("Cure mediche",10),
            new PieChart.Data("Spesa",10)   //parametri di default
         );  

        PieChart pieChart = new PieChart(valueList); //05
        pieChart.getData().forEach(dato -> {
            String percentage = String.format("%.2f%%", (dato.getPieValue() / 100));
            Tooltip toolTip = new Tooltip(percentage);
            Tooltip.install(dato.getNode(), toolTip);
        });

        Controller ctrl = new Controller(this, FILE_CONFIGURAZIONE, SCHEMA_CONFIGURAZIONE, FILE_CACHE); //06
        CacheMovimento cache = ctrl.accensione(); //07
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
        
        dataInizio.setOnAction(new EventHandler<ActionEvent>() { //08
            @Override 
            public void handle(ActionEvent e) {
            // Carica nella tabella i movimenti del periodo ora selezionato
                dateHandlerHelper(archivio, SaldoDaMostrare, valueList);
                
            // Imposta nel DatePicker dataFine un range di selezione per la data
            //      precedente al corrente dataInizio.getValue()    
                dataFine.setDayCellFactory(d -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, 
                            boolean empty) {
                        super.updateItem(item,empty);
                        
                        setDisable(item.isBefore(dataInizio.getValue()));
                    }
                });
                
            }
            
        });
        
        dataFine.setOnAction(new EventHandler<ActionEvent>() { //08
            @Override 
            public void handle(ActionEvent e) {
            // Carica nella tabella i movimenti del periodo ora selezionato  
                dateHandlerHelper(archivio, SaldoDaMostrare, valueList);
                
            // Imposta nel DatePicker dataInizio un range di selezione per la data
            //      successivo al corrente dataFine.getValue()    
                dataInizio.setDayCellFactory(d -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, 
                            boolean empty) {
                        super.updateItem(item,empty);
                        
                        setDisable(item.isAfter(dataFine.getValue()));
                    }
                });

            }
            
        });
       
        cancella.setText("CANCELLA");
        cancella.setDisable(true);
        
        cancella.setOnAction(new EventHandler<ActionEvent>() { //09
            @Override 
            public void handle(ActionEvent e) {
                if(daCancellare != null) {
                    archivio.deleteMovimento(daCancellare); //la voce selezionata
                    saldoAttuale = archivio.calcolaSaldo(mail.getText()) + " €";
                    SaldoDaMostrare.setText(saldoAttuale);
                    tabella.getItems().clear();
                    archivio.loadMovimento(voci, mail.getText(), dataInizio.getValue(),
                            dataFine.getValue(), valueList);    
                    
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
        inserisci.setOnAction(new EventHandler<ActionEvent>() {  //10
            @Override 
            public void handle(ActionEvent e) {
                String utente = mail.getText();
                validaMail(utente);
                String voce = (String) cbVoce.getValue(); 
                String tipo = "u";
                if( entrata.isSelected()){ 
                    tipo = "e"; 
                } 
                if(voce.equals("Stipendio")){
                    tipo = "e";
                    uscita.setSelected(false);
                    entrata.fire();
                }
               
                try {
                    double imp = Double.parseDouble(importo.getText());;
                    String date = dataNewMov.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Movimento newMov = new Movimento(date, imp, voce, utente, tipo);
                    
                    archivio.setMovimento(newMov /*, voci, valueList*/);
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
        svuota.setOnAction(new EventHandler<ActionEvent>() { //11
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
        annulla.setOnAction(new EventHandler<ActionEvent>() { //12
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
        
        mail.setOnKeyPressed(new EventHandler<KeyEvent>(){ //13
           @Override 
            public void handle(KeyEvent kevent){
                if(kevent.getCode().equals(KeyCode.ENTER)){
                    String email = mail.getText();
                    tabella.getItems().clear();
                    if (validaMail(email)) {
                        archivio.loadMovimento(voci, email, dataInizio.getValue(),
                                dataFine.getValue(), valueList);
                        saldoAttuale = archivio.calcolaSaldo(mail.getText()) + " €";
                        SaldoDaMostrare.setText(saldoAttuale);
                    }
                } 
            } 
        });
       
        TableColumn dataCol = new TableColumn("DATA");     //14
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        TableColumn categoriaCol = new TableColumn("CATEGORIA");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("voce"));
        
        TableColumn importoCol = new TableColumn("IMPORTO");
        importoCol.setCellValueFactory(new PropertyValueFactory<>("importo"));
       
        dataCol.setPrefWidth(150); //15
        categoriaCol.setPrefWidth(165);
        importoCol.setPrefWidth(150);
       
        tabella.getColumns().addAll(dataCol, categoriaCol, importoCol);
     
        tabella.setFixedCellSize(25); 
        tabella.prefHeightProperty().set((ctrl.config.getNumRighe() + 1) * 25); 
        tabella.setStyle("-fx-background-color:" + ctrl.config.getColor());
          
        importoCol.setCellFactory(column -> { //16
            return new TableCell<Movimento, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {  //////////////////////////////////////////
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
   
        GridPane root = new GridPane(); //17
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

        Scene scene = new Scene(root, ctrl.config.getLarghezza(), ctrl.config.getALtezza()); //18
        primaryStage.setTitle("My accounting!");
        primaryStage.setScene(scene);
        primaryStage.show(); 
        
        
        primaryStage.setOnCloseRequest((WindowEvent we) -> { //19
            ctrl.chiusura();
            System.out.println("Closing...");
        });
        
        tabella.setOnMousePressed(new EventHandler<MouseEvent>() { //20
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
                    cancella.setDisable(false);
                    daCancellare = (Movimento) tabella.getSelectionModel().getSelectedItem();
                }
            }
        });
        
        if(cache == null){   //21
            dataInizio.setValue(LocalDate.now().plusDays(-30)); 
            dataFine.setValue(LocalDate.now()); 
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
            tabella.getItems().clear();
            if (validaMail(mailUtil)) {
                archivio.loadMovimento(voci, mailUtil, dataInizio.getValue(), 
                        dataFine.getValue(), valueList);
                saldoAttuale = archivio.calcolaSaldo(mail.getText()) + " €";
                SaldoDaMostrare.setText(saldoAttuale);  
            }
        }   
    }
}

/*
Note:

00) Applicazione Java con interfaccia grafica realizzata estendendo la classe Application
01) Componenti grafici di Java FX.
02)Funzione valida emal.
03) Metodo start che viene chiamato automaticamente all'avvio, consente di 
    inizializzare l'applicazione.
04) Lista  delle componenti del diagramma a torta.
05) Istanziato componente grafico del diagramma a torta.
06) Istanziate le classi: Contabilità, Controller, Archivio.
07) Chiamata la funzione accensione di controller.
08) Gestione cambio data.
09) Gestione evento CANCELLA.
10) Gestione evento INSERISCI.
11) Gestione evento SVUOTA.
12) Gestione evento ANNULLA.
13) Gestione evento INVIO in textField mail.
14) Colonne della tabella.
15) Fissate dimensioni delle colonne della tabella.
16)Viene aggiornato il contenuto della tabella.
17) GridPane: contenitore a griglia per gli oggetti osservabili dentro la scena.
18) Scene: contenitore principale della finestra.
19)Gestione evento CHIUSURA.
20)Gestione doppio click nella riga della tabella
21)Gestione della cache. se la cache è vuota vengono impostati dei valori di 
   default nelle date. Altrimenti vengono caricati nell'interfaccia i dati della cache.
*/