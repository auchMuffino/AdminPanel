package needs.adminpanel;

import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import needs.importantclasses.ChainStore;
import needs.importantclasses.DBConnection;
import needs.importantclasses.Good;
import needs.importantclasses.Price;
import org.apache.commons.lang3.math.NumberUtils;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PriceContoller implements Initializable {

    @FXML
    private TableView<Price> pricesTable;
    @FXML
    private SearchableComboBox<Good> good;
    @FXML
    private SearchableComboBox<ChainStore> store;
    @FXML
    private TextField price;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getPrices();
        good.setItems(MainWindow.goods);
        store.setItems(MainWindow.chainStores);
    }
    private void getPrices(){
        //create columns for table "prices"
        TableColumn<Price,String> productPrice = new TableColumn<Price, String>("Товар");
        productPrice.setCellValueFactory(product->new ReadOnlyStringWrapper(product.getValue().getGood().getTitle()));
        TableColumn<Price,String> storePrice = new TableColumn<Price, String>("Сеть магазинов");
        storePrice.setCellValueFactory(store->new ReadOnlyStringWrapper(store.getValue().getStore().getTitle()));
        TableColumn<Price, Double> price = new TableColumn<Price, Double>("Цена");
        price.setCellValueFactory(new PropertyValueFactory<Price,Double>("price"));

        pricesTable.getColumns().addAll(productPrice, storePrice, price); //adding columns in table "prices"
        pricesTable.setItems(MainWindow.prices); //filling table "prices"
    }
    @FXML
    private void insert() throws SQLException {
        Alert insert;
        if(good.getSelectionModel().isEmpty()){
            insert=new Alert(Alert.AlertType.ERROR,"Товар не выбран!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(store.getSelectionModel().isEmpty()){
            insert=new Alert(Alert.AlertType.ERROR,"Сеть магазинов не выбрана!", ButtonType.OK);
            insert.showAndWait();
        }
        else if (price.getText().equals("") || !NumberUtils.isDigits(price.getText())) {
            insert=new Alert(Alert.AlertType.ERROR,"Поле 'Цена' не заполнено или заполнено неверно!", ButtonType.OK);
            insert.showAndWait();
        }
        else{
            int ID=0;

            DBConnection.openConnection();
            DBConnection.statementExecute("insert into Prices values(default,'"+good.getSelectionModel().getSelectedItem().getArticleNumber()+"','"+store.getSelectionModel().getSelectedItem().getPayersRegistrationNumber()+"',"+price.getText()+");");
            ResultSet pr = DBConnection.statementExecuteQuery("select id from Prices where price="+price.getText()+" and articleNumber="+good.getSelectionModel().getSelectedItem().getArticleNumber()+" and payersRegistrationNumber="+store.getSelectionModel().getSelectedItem().getPayersRegistrationNumber());
            while (pr.next()){
                ID=pr.getInt(1);
            }

            MainWindow.prices.add(new Price(ID, good.getSelectionModel().getSelectedItem(), store.getSelectionModel().getSelectedItem(),NumberUtils.createDouble(price.getText())));

            good.getSelectionModel().clearSelection();
            store.getSelectionModel().clearSelection();
            price.setText("");

            insert=new Alert(Alert.AlertType.INFORMATION,"Цена успешно добавлена!",ButtonType.OK);
            insert.showAndWait();

            DBConnection.closeConnection();
        }
    }
    @FXML
    private void delete(){
        TableView.TableViewSelectionModel<Price> selectionModel = pricesTable.getSelectionModel();
        if(selectionModel.getSelectedItem()!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите удалить запись?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                alert = new Alert( Alert.AlertType.CONFIRMATION,"Вы точно в этом уверены?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    DBConnection.openConnection();

                    DBConnection.statementExecute("Delete from Prices where id="+selectionModel.getSelectedItem().getId());
                    MainWindow.prices.remove(selectionModel.getSelectedItem());

                    DBConnection.closeConnection();
                }
            }
        }
    }
    @FXML
    private void exportToExcel() throws SQLException {
        DBConnection.openConnection();
        ResultSet ordersExcel = DBConnection.statementExecuteQuery("select Goods.title, ChainStores.title, Prices.price from Prices inner join Goods on Prices.articleNumber=Goods.articleNumber inner join ChainStores on Prices.payersRegistrationNumber=ChainStores.payersRegistrationNumber;");
        //Create a Workbook instance
        Workbook workbook = new Workbook();
        //Get the first worksheet
        Worksheet sheet = workbook.getWorksheets().get(0);
        //Set sheet name
        sheet.setName("Товары");;
        //Hide gridlines
        sheet.setGridLinesVisible(true);

        //Add some data to the worksheet
        sheet.getRange().get(1,1).setValue("Товар");
        sheet.getRange().get(1,2).setValue("Сеть магазинов");
        sheet.getRange().get(1,3).setValue("Цена");

        int indx=2;

        while(ordersExcel.next()){

            try {
                sheet.getRange().get(indx,1).setValue(ordersExcel.getString(1));
                sheet.getRange().get(indx,2).setValue(ordersExcel.getString(2));
                sheet.getRange().get(indx,3).setNumberValue(ordersExcel.getDouble(3));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            indx++;
        }
        DBConnection.closeConnection();
        workbook.saveToFile("PricesExport.xlsx", ExcelVersion.Version2016);
    }
}
