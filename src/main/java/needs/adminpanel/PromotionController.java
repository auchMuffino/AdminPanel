package needs.adminpanel;

import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import needs.importantclasses.ChainStore;
import needs.importantclasses.DBConnection;
import needs.importantclasses.Good;
import needs.importantclasses.Promotion;
import org.apache.commons.lang3.math.NumberUtils;


import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class PromotionController implements Initializable {

    @FXML
    private TableView<Promotion> promoTable;
    @FXML
    private TextField title, discount;
    @FXML
    private ComboBox<Good> good;
    @FXML
    private ComboBox<ChainStore> store;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker startDate, endDate;
    private String photoPath="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getPromo();
        good.setItems(MainWindow.goods);
        store.setItems(MainWindow.chainStores);
    }
    private void getPromo(){

        //create columns for table "promotions"
        TableColumn<Promotion,String> promoTitle = new TableColumn<Promotion, String>("Название Акции");
        promoTitle.setCellValueFactory(new PropertyValueFactory<Promotion,String>("title"));
        TableColumn<Promotion,String> promoInShop=new TableColumn<Promotion, String>("Магазин");
        promoInShop.setCellValueFactory(inShopWrapper->new ReadOnlyStringWrapper(inShopWrapper.getValue().getStore().getTitle()));
        TableColumn<Promotion,String> promoProduct=new TableColumn<Promotion, String>("Товар");
        promoProduct.setCellValueFactory(product->new ReadOnlyStringWrapper(product.getValue().getGood().getTitle()));
        TableColumn<Promotion, String> promoDescription = new TableColumn<Promotion, String>("Описание");
        promoDescription.setCellValueFactory(new PropertyValueFactory<Promotion,String>("description"));
        TableColumn<Promotion, String> promoDiscount=new TableColumn<Promotion, String>("Скидка");
        promoDiscount.setCellValueFactory(promo->new ReadOnlyStringWrapper(promo.getValue().getDiscount()*100+"%"));
        TableColumn<Promotion, Date> promoStart=new TableColumn<Promotion, Date>("Дата начала");
        promoStart.setCellValueFactory(new PropertyValueFactory<Promotion,Date>("startDate"));
        TableColumn<Promotion, Date> promoEnd=new TableColumn<Promotion, Date>("Дата окончания");
        promoEnd.setCellValueFactory(new PropertyValueFactory<Promotion,Date>("endDate"));

        promoTable.getColumns().addAll(promoTitle, promoInShop, promoProduct, promoDescription,promoDiscount, promoStart, promoEnd); //adding columns in table "promotions"
        promoTable.setItems(MainWindow.promotions); //filling table "promotions"
    }
    private void getPromo(Date date){

        Date.from(startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        ObservableList<Promotion> promo= FXCollections.observableArrayList();

        for(Promotion prm:MainWindow.promotions){
            if(prm.getStartDate()<date && prm.getEndDate()>date){
                promo.add(prm);
            }
        }

        //create columns for table "promotions"
        TableColumn<Promotion,String> promoTitle = new TableColumn<Promotion, String>("Название Акции");
        promoTitle.setCellValueFactory(new PropertyValueFactory<Promotion,String>("title"));
        TableColumn<Promotion,String> promoInShop=new TableColumn<Promotion, String>("Магазин");
        promoInShop.setCellValueFactory(inShopWrapper->new ReadOnlyStringWrapper(inShopWrapper.getValue().getStore().getTitle()));
        TableColumn<Promotion,String> promoProduct=new TableColumn<Promotion, String>("Товар");
        promoProduct.setCellValueFactory(product->new ReadOnlyStringWrapper(product.getValue().getGood().getTitle()));
        TableColumn<Promotion, String> promoDescription = new TableColumn<Promotion, String>("Описание");
        promoDescription.setCellValueFactory(new PropertyValueFactory<Promotion,String>("description"));
        TableColumn<Promotion, String> promoDiscount=new TableColumn<Promotion, String>("Скидка");
        promoDiscount.setCellValueFactory(promo->new ReadOnlyStringWrapper(promo.getValue().getDiscount()*100+"%"));
        TableColumn<Promotion, Date> promoStart=new TableColumn<Promotion, Date>("Дата начала");
        promoStart.setCellValueFactory(new PropertyValueFactory<Promotion,Date>("startDate"));
        TableColumn<Promotion, Date> promoEnd=new TableColumn<Promotion, Date>("Дата окончания");
        promoEnd.setCellValueFactory(new PropertyValueFactory<Promotion,Date>("endDate"));

        promoTable.getColumns().addAll(promoTitle, promoInShop, promoProduct, promoDescription,promoDiscount, promoStart, promoEnd); //adding columns in table "promotions"
        promoTable.setItems(MainWindow.promotions); //filling table "promotions"
    }
    @FXML
    private void selectPhoto(){
        Stage stage = new Stage();
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Выберите фото");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Select...","*.jpg","*.jpeg","*.png"));
        File file=fileChooser.showOpenDialog(stage);
        photoPath=file.getPath();
    }
    @FXML
    private void insert() throws SQLException {
        Alert insert;
        if(title.getText().equals("")){
            insert=new Alert(Alert.AlertType.ERROR,"Поле 'Название' не заполнено!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(good.getSelectionModel().isEmpty()){
            insert=new Alert(Alert.AlertType.ERROR,"Товар не выбран!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(store.getSelectionModel().isEmpty()){
            insert=new Alert(Alert.AlertType.ERROR,"Сеть магазинов не выбрана!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(description.getText().equals("")){
            insert=new Alert(Alert.AlertType.ERROR,"Поле 'Описание' не заполнено!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(photoPath.equals("")){
            insert=new Alert(Alert.AlertType.ERROR,"Фото не выбрано!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(startDate.equals("")){
            insert=new Alert(Alert.AlertType.ERROR,"Дата начала не выбрана!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(endDate.equals("")){
            insert=new Alert(Alert.AlertType.ERROR,"Дата окончания не выбрана!", ButtonType.OK);
            insert.showAndWait();
        }
        else if(discount.getText().equals("") || !NumberUtils.isDigits(discount.getText())){
            insert=new Alert(Alert.AlertType.ERROR,"Скидка не введена или введена неверно!", ButtonType.OK);
            insert.showAndWait();
        }
        else{
            DBConnection.openConnection();
            DBConnection.statementExecute("insert into Promotions values (default,'"+good.getSelectionModel().getSelectedItem().getArticleNumber()+"','"+title.getText()+"','"+store.getSelectionModel().getSelectedItem().getPayersRegistrationNumber()+"','"+description.getText()+"','"+photoPath+"','"+startDate.getValue()+"','"+endDate.getValue()+"',"+(NumberUtils.createDouble(discount.getText())/100)+")");

            ResultSet set= DBConnection.statementExecuteQuery("select max(number) from Promotions");
            int number=0;

            while(set.next()){
                number=set.getInt(1);
            }

            MainWindow.promotions.add(new Promotion(number,good.getSelectionModel().getSelectedItem(),title.getText(),store.getSelectionModel().getSelectedItem(),description.getText(),photoPath,Date.from(startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(endDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),NumberUtils.createDouble(discount.getText())/100));

            good.getSelectionModel().clearSelection();
            store.getSelectionModel().clearSelection();
            title.setText("");
            description.setText("");
            photoPath="";
            startDate.setValue(null);
            endDate.setValue(null);
            discount.setText("");

            insert=new Alert(Alert.AlertType.INFORMATION,"Акция добавлена!",ButtonType.OK);
            insert.showAndWait();

            DBConnection.closeConnection();
        }
    }
    @FXML
    private void delete(){
        TableView.TableViewSelectionModel<Promotion> selectionModel = promoTable.getSelectionModel();
        if(selectionModel.getSelectedItem()!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите удалить запись?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                alert = new Alert( Alert.AlertType.CONFIRMATION,"Вы точно в этом уверены?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    DBConnection.openConnection();

                    DBConnection.statementExecute("Delete from Promotions where number="+selectionModel.getSelectedItem().getNumber());
                    MainWindow.promotions.remove(selectionModel.getSelectedItem());

                    DBConnection.closeConnection();
                }
            }
        }
    }
    @FXML
    private void exportToExcel() throws SQLException {
        DBConnection.openConnection();
        ResultSet ordersExcel = DBConnection.statementExecuteQuery(
                "select Promotions.number,Goods.title,Promotions.title,ChainStores.title, Promotions.description, Promotions.startDate, Promotions.endDate, Promotions.discount from Promotions inner join Goods on Promotions.articleNumber=Goods.articleNumber inner join ChainStores on Promotions.payersRegistrationNumber=ChainStores.payersRegistrationNumber; "
        );
        //Create a Workbook instance
        Workbook workbook = new Workbook();
        //Get the first worksheet
        Worksheet sheet = workbook.getWorksheets().get(0);
        //Set sheet name
        sheet.setName("Акции");;
        //Hide gridlines
        sheet.setGridLinesVisible(true);

        //Add some data to the worksheet
        sheet.getRange().get(1,1).setValue("Номер");
        sheet.getRange().get(1,2).setValue("Товар");
        sheet.getRange().get(1,3).setValue("Название");
        sheet.getRange().get(1,4).setValue("Сеть Магазинов");
        sheet.getRange().get(1,5).setValue("Описание");
        sheet.getRange().get(1,6).setValue("Дата начала");
        sheet.getRange().get(1,7).setValue("Дата окончания");
        sheet.getRange().get(1,8).setValue("Скидка");

        int indx=2;

        while(ordersExcel.next()){

            try {
                sheet.getRange().get(indx,1).setValue(ordersExcel.getString(1));
                sheet.getRange().get(indx,2).setValue(ordersExcel.getString(2));
                sheet.getRange().get(indx,3).setValue(ordersExcel.getString(3));
                sheet.getRange().get(indx,4).setValue(ordersExcel.getString(4));
                sheet.getRange().get(indx,5).setValue(ordersExcel.getString(5));
                sheet.getRange().get(indx,6).setDateTimeValue(ordersExcel.getDate(6));
                sheet.getRange().get(indx,7).setDateTimeValue(ordersExcel.getDate(7));
                sheet.getRange().get(indx,8).setNumberValue(ordersExcel.getDouble(8));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            indx++;
        }
        DBConnection.closeConnection();
        workbook.saveToFile("PromoExport.xlsx", ExcelVersion.Version2016);
    }
}
