package needs.adminpanel;


import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import needs.importantclasses.ChainStore;
import needs.importantclasses.DBConnection;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StoreController implements Initializable {
    @FXML
    TableView<ChainStore> storesTable;
    @FXML
    TextField UNP,title, legalName, businessAddress;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getChainOfStores();
    }
    private void getChainOfStores(){
        //create columns for table "stores"
        TableColumn<ChainStore,String> storeUNP = new TableColumn<ChainStore, String>("УНП сети");
        storeUNP.setCellValueFactory(new PropertyValueFactory<ChainStore,String>("payersRegistrationNumber"));
        TableColumn<ChainStore,String> storeTitle = new TableColumn<ChainStore, String>("Название");
        storeTitle.setCellValueFactory(new PropertyValueFactory<ChainStore,String>("title"));
        TableColumn<ChainStore, String> storeLegalName = new TableColumn<ChainStore, String>("Юридическое название");
        storeLegalName.setCellValueFactory(new PropertyValueFactory<ChainStore,String>("legalName"));
        TableColumn<ChainStore,String> storeBusinessAddress=new TableColumn<ChainStore, String>("Юридический адрес");
        storeBusinessAddress.setCellValueFactory(new PropertyValueFactory<ChainStore,String>("businessAddress"));

        storesTable.getColumns().addAll(storeUNP, storeTitle, storeLegalName, storeBusinessAddress); //adding columns in table "stores"
        storesTable.setItems(MainWindow.chainStores); //filling table "stores"
    }
    @FXML
    private void insert(){
        Alert insert;
        if(UNP.getText().equals("")){
            insert=new Alert(Alert.AlertType.ERROR,"Поле 'УНП сети' не заполнено!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(title.getText().equals("")){
            insert=new Alert(Alert.AlertType.ERROR,"Поле 'Название' не заполнено!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(legalName.getText().equals("")){
            insert=new Alert(Alert.AlertType.ERROR,"Поле 'Юридическое название' не заполнено!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(businessAddress.getText().equals("")){
            insert=new Alert(Alert.AlertType.ERROR,"Поле 'Юридический адрес' не заполнено!", ButtonType.OK);
            insert.showAndWait();
        }
        else{
            DBConnection.openConnection();
            DBConnection.statementExecute("insert into ChainStores values ('"+UNP.getText()+"','"+title.getText()+"','"+legalName.getText()+"','"+businessAddress.getText()+"');");

            MainWindow.chainStores.add(new ChainStore(UNP.getText(), title.getText(),legalName.getText(),businessAddress.getText()));

            UNP.setText("");
            title.setText("");
            legalName.setText("");
            businessAddress.setText("");

            insert=new Alert(Alert.AlertType.INFORMATION,"Сеть успешно добавлена!",ButtonType.OK);
            insert.showAndWait();

            DBConnection.closeConnection();
        }
    }
    @FXML
    private void delete(){
        TableView.TableViewSelectionModel<ChainStore> selectionModel = storesTable.getSelectionModel();
        if(selectionModel.getSelectedItem()!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите удалить запись?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                alert = new Alert( Alert.AlertType.CONFIRMATION,"Вы точно в этом уверены?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    DBConnection.openConnection();

                    DBConnection.statementExecute("Delete from ChainStores where payersRegistrationNumber="+selectionModel.getSelectedItem().getPayersRegistrationNumber());
                    MainWindow.chainStores.remove(selectionModel.getSelectedItem());

                    DBConnection.closeConnection();
                }
            }
        }
    }
    @FXML
    private void exportToExcel() throws SQLException {
        DBConnection.openConnection();
        ResultSet ordersExcel = DBConnection.statementExecuteQuery("SELECT * from ChainStores; ");
        //Create a Workbook instance
        Workbook workbook = new Workbook();
        //Get the first worksheet
        Worksheet sheet = workbook.getWorksheets().get(0);
        //Set sheet name
        sheet.setName("Сети Магазинов");;
        //Hide gridlines
        sheet.setGridLinesVisible(true);

        //Add some data to the worksheet
        sheet.getRange().get(1,1).setValue("УНП");
        sheet.getRange().get(1,2).setValue("Название");
        sheet.getRange().get(1,3).setValue("Юридическое название");
        sheet.getRange().get(1,4).setValue("Юридический адрес");

        int indx=2;

        while(ordersExcel.next()){

            try {
                sheet.getRange().get(indx,1).setValue(ordersExcel.getString(1));
                sheet.getRange().get(indx,2).setValue(ordersExcel.getString(2));
                sheet.getRange().get(indx,3).setValue(ordersExcel.getString(3));
                sheet.getRange().get(indx,4).setValue(ordersExcel.getString(4));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            indx++;
        }
        DBConnection.closeConnection();
        workbook.saveToFile("StoresExport.xlsx", ExcelVersion.Version2016);
    }
}
