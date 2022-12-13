package needs.adminpanel;

import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import needs.importantclasses.*;
import needs.intefaces.ClickItem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {

    @FXML
    private GridPane goodPlace;
    @FXML
    private TextField search, title, manufacturer, countryOfOrigin;
    @FXML
    private TextArea description;
    @FXML
    private ComboBox<Category> category, categorySearch;
    @FXML
    private Button photo, export;
    @FXML
    private CheckBox inEveryStore, activePromo;
    public static ObservableList<Good> records = FXCollections.observableArrayList();
    private ClickItem clickItem;
    private String photoPath;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillGrid();
        EventHandler<KeyEvent> eventHandlerSearch = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                goodPlace.getChildren().clear();
                fillGrid();
//                if(search.getText().equals("") && categorySearch.getSelectionModel().isEmpty())
//                    fillGrid();
//                else {
//                    if(!search.getText().equals("") && !categorySearch.getSelectionModel().isEmpty())
//                        fillGrid(search.getText(), categorySearch.getSelectionModel().getSelectedItem());
//                    else if (search.getText().equals(""))
//                        fillGrid(categorySearch.getSelectionModel().getSelectedItem());
//                    else
//                        fillGrid(search.getText());
//                }
            }
        };
        EventHandler<ActionEvent> eventCategoryCombo = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println(category.getSelectionModel().getSelectedItem());
            }
        };
        EventHandler<ActionEvent> eventCategorySearchCombo = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                goodPlace.getChildren().clear();
                fillGrid();
//                if(search.getText().equals("") && categorySearch.getSelectionModel().isEmpty())
//                    fillGrid();
//                else {
//                    if(!search.getText().equals("") && !categorySearch.getSelectionModel().isEmpty())
//                        fillGrid(search.getText(), categorySearch.getSelectionModel().getSelectedItem());
//                    else if (search.getText().equals(""))
//                        fillGrid(categorySearch.getSelectionModel().getSelectedItem());
//                    else
//                        fillGrid(search.getText());
//                }
            }
        };
        search.addEventFilter(KeyEvent.KEY_TYPED,eventHandlerSearch);
        category.setItems(MainWindow.categories);
        categorySearch.setItems(MainWindow.categories);
        categorySearch.setOnAction(eventCategorySearchCombo);
        category.setOnAction(eventCategoryCombo);
    }
    private void fillGrid(){
        int column=0;
        int row=1;
        this.clickItem=new ClickItem() {
            @Override
            public void clickListener(Good good) {
                records.add(good);
            }
            @Override
            public void clickDelete(Good good) {
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Вы действительно хотите удалить товар?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult()==ButtonType.YES){
                    delete(good);
                    alert=new Alert(Alert.AlertType.INFORMATION,"Товар удален!", ButtonType.APPLY);
                    alert.showAndWait();
                }
            }
        };
        try {
            for(int i=0;i<MainWindow.goods.size();i++){
                if(!search.getText().equals("") && !categorySearch.getSelectionModel().isEmpty() && inEveryStore.isSelected() && activePromo.isSelected()){
                    int storesCount=0;
                    boolean active=false;
                    for(Price pr:MainWindow.prices)
                        if(MainWindow.goods.get(i).getArticleNumber()==pr.getGood().getArticleNumber())
                            storesCount++;
                    for(Promotion promo : MainWindow.promotions)
                        if(promo.getGood().getArticleNumber()==MainWindow.goods.get(i).getArticleNumber())
                            active=true;
                    if(MainWindow.goods.get(i).toString().indexOf(search.getText())==-1
                            || MainWindow.goods.get(i).getCategory().getId()!=categorySearch.getSelectionModel().getSelectedItem().getId()
                            || storesCount<MainWindow.chainStores.size()
                            || !active){
                        continue;
                    }
                }
                else if(!search.getText().equals("") && !categorySearch.getSelectionModel().isEmpty() && activePromo.isSelected()){
                    boolean active=false;
                    for(Promotion promo : MainWindow.promotions)
                        if(promo.getGood().getArticleNumber()==MainWindow.goods.get(i).getArticleNumber())
                            active=true;
                    if(MainWindow.goods.get(i).toString().indexOf(search.getText())==-1
                            || MainWindow.goods.get(i).getCategory().getId()!=categorySearch.getSelectionModel().getSelectedItem().getId()
                            || !active){
                        continue;
                    }
                }
                else if(!search.getText().equals("") && !categorySearch.getSelectionModel().isEmpty() && inEveryStore.isSelected()){
                    int storesCount=0;
                    for(Price pr:MainWindow.prices)
                        if(MainWindow.goods.get(i).getArticleNumber()==pr.getGood().getArticleNumber())
                            storesCount++;
                    if(MainWindow.goods.get(i).toString().indexOf(search.getText())==-1
                            && MainWindow.goods.get(i).getCategory().getId()!=categorySearch.getSelectionModel().getSelectedItem().getId()
                            && storesCount<MainWindow.chainStores.size()){
                        continue;
                    }
                }
                else if(!search.getText().equals("") && inEveryStore.isSelected() && activePromo.isSelected()){
                    int storesCount=0;
                    boolean active=false;
                    for(Price pr:MainWindow.prices)
                        if(MainWindow.goods.get(i).getArticleNumber()==pr.getGood().getArticleNumber())
                            storesCount++;
                    for(Promotion promo : MainWindow.promotions)
                        if(promo.getGood().getArticleNumber()==MainWindow.goods.get(i).getArticleNumber())
                            active=true;
                    if(MainWindow.goods.get(i).toString().indexOf(search.getText())==-1
                            || storesCount<MainWindow.chainStores.size()
                            || !active){
                        continue;
                    }
                }
                else if(!categorySearch.getSelectionModel().isEmpty() && inEveryStore.isSelected() && activePromo.isSelected()){
                    int storesCount=0;
                    boolean active=false;
                    for(Price pr:MainWindow.prices)
                        if(MainWindow.goods.get(i).getArticleNumber()==pr.getGood().getArticleNumber())
                            storesCount++;
                    for(Promotion promo : MainWindow.promotions)
                        if(promo.getGood().getArticleNumber()==MainWindow.goods.get(i).getArticleNumber())
                            active=true;
                    if(MainWindow.goods.get(i).getCategory().getId()!=categorySearch.getSelectionModel().getSelectedItem().getId()
                            || storesCount<MainWindow.chainStores.size()
                            || !active){
                        continue;
                    }
                }
                else if(!search.getText().equals("") && !categorySearch.getSelectionModel().isEmpty()){
                    if(MainWindow.goods.get(i).toString().indexOf(search.getText())==-1 || MainWindow.goods.get(i).getCategory().getId()!=categorySearch.getSelectionModel().getSelectedItem().getId()){
                        continue;
                    }
                }
                else if(!search.getText().equals("") && inEveryStore.isSelected()){
                    int storesCount=0;
                    for(Price pr:MainWindow.prices)
                        if(MainWindow.goods.get(i).getArticleNumber()==pr.getGood().getArticleNumber())
                            storesCount++;
                    if(MainWindow.goods.get(i).toString().indexOf(search.getText())==-1 || storesCount<MainWindow.chainStores.size()){
                        continue;
                    }
                }
                else if(!search.getText().equals("") && activePromo.isSelected()){
                    boolean active=false;
                    for(Promotion promo : MainWindow.promotions)
                        if(promo.getGood().getArticleNumber()==MainWindow.goods.get(i).getArticleNumber())
                            active=true;
                    if(MainWindow.goods.get(i).toString().indexOf(search.getText())==-1 || !active){
                        continue;
                    }
                }
                else if(!categorySearch.getSelectionModel().isEmpty() && inEveryStore.isSelected()){
                    int storesCount=0;
                    for(Price pr:MainWindow.prices)
                        if(MainWindow.goods.get(i).getArticleNumber()==pr.getGood().getArticleNumber())
                            storesCount++;
                    if(MainWindow.goods.get(i).toString().indexOf(search.getText())==-1 || storesCount<MainWindow.chainStores.size()){
                        continue;
                    }
                }
                else if(!categorySearch.getSelectionModel().isEmpty() && activePromo.isSelected()){
                    boolean active=false;
                    for(Promotion promo : MainWindow.promotions)
                        if(promo.getGood().getArticleNumber()==MainWindow.goods.get(i).getArticleNumber())
                            active=true;
                    if(MainWindow.goods.get(i).toString().indexOf(search.getText())==-1 || !active){
                        continue;
                    }
                }
                else if(inEveryStore.isSelected() && activePromo.isSelected()){
                    int storesCount=0;
                    boolean active=false;
                    for(Price pr:MainWindow.prices)
                        if(MainWindow.goods.get(i).getArticleNumber()==pr.getGood().getArticleNumber())
                            storesCount++;
                    for(Promotion promo : MainWindow.promotions)
                        if(promo.getGood().getArticleNumber()==MainWindow.goods.get(i).getArticleNumber())
                            active=true;
                    if(storesCount<MainWindow.chainStores.size() || !active){
                        continue;
                    }
                }
                else if(!search.getText().equals("")){
                    if((MainWindow.goods.get(i).toString().indexOf(search.getText())==-1)){
                        continue;
                    }
                }
                else if(!categorySearch.getSelectionModel().isEmpty()){
                    if(!(MainWindow.goods.get(i).getCategory().getId()==categorySearch.getSelectionModel().getSelectedItem().getId())){
                        continue;
                    }
                }
                else if(inEveryStore.isSelected()){
                    int storesCount=0;
                    for(Price pr:MainWindow.prices)
                        if(MainWindow.goods.get(i).getArticleNumber()==pr.getGood().getArticleNumber())
                            storesCount++;
                    if(storesCount<MainWindow.chainStores.size()){
                        continue;
                    }
                }
                else if (activePromo.isSelected()) {
                    boolean active=false;
                    for(Promotion promo : MainWindow.promotions)
                        if(promo.getGood().getArticleNumber()==MainWindow.goods.get(i).getArticleNumber())
                            active=true;
                    if(!active){
                        continue;
                    }
                }
                FXMLLoader fxmlLoader=new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Views/item.fxml"));

                BorderPane anchorPane=fxmlLoader.load();

                ItemController itemController=fxmlLoader.getController();
                itemController.setData(MainWindow.goods.get(i), clickItem);

                if(column==2) {
                    column = 0;
                    row++;
                }

                goodPlace.add(anchorPane,column++,row);

                goodPlace.setMinWidth(Region.USE_COMPUTED_SIZE);
                goodPlace.setPrefWidth(Region.USE_COMPUTED_SIZE);
                goodPlace.setMaxWidth(Region.USE_COMPUTED_SIZE);
                goodPlace.setMinHeight(Region.USE_COMPUTED_SIZE);
                goodPlace.setPrefHeight(Region.USE_COMPUTED_SIZE);
                goodPlace.setMaxHeight(Region.USE_COMPUTED_SIZE);
                GridPane.setMargin(anchorPane,new Insets(20));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    private void insert(){
        Alert insert;
        if(title.getText().equals("")) {
            insert = new Alert(Alert.AlertType.ERROR, "Поле 'Название' не заполнено!",ButtonType.OK);
            insert.showAndWait();
        }
        else if(manufacturer.getText().equals("")){
            insert = new Alert(Alert.AlertType.ERROR, "Поле 'Производитель' не заполнено!",ButtonType.OK);
            insert.showAndWait();
        }
        else if(countryOfOrigin.getText().equals("")){
            insert = new Alert(Alert.AlertType.ERROR, "Поле 'Страна производства' не заполнено!",ButtonType.OK);
            insert.showAndWait();
        }
        else if(category.getSelectionModel().isEmpty()){
            insert = new Alert(Alert.AlertType.ERROR, "Не выбрана категория!",ButtonType.OK);
            insert.showAndWait();
        }
        else if(photoPath.equals("")){
            insert = new Alert(Alert.AlertType.ERROR, "Фото не выбрано!",ButtonType.OK);
            insert.showAndWait();
        }
        else if(description.equals("")){
            insert = new Alert(Alert.AlertType.ERROR, "Поле 'Описание' не заполнено!",ButtonType.OK);
            insert.showAndWait();
        }
        else{

            double article=((title.getText().length()+manufacturer.getText().length())/countryOfOrigin.getText().length()+MainWindow.goods.size());
            DBConnection.openConnection();
            DBConnection.statementExecute("insert into Goods values('"+Double.toString(article)+"',"+category.getSelectionModel().getSelectedItem().getId()+",'"+photoPath+"','"+title.getText()+"','"+manufacturer.getText()+"','"+description.getText()+"','"+countryOfOrigin.getText()+"');");

            MainWindow.goods.add(new Good(Double.toString(article),category.getSelectionModel().getSelectedItem(),photoPath, title.getText(), manufacturer.getText(), description.getText(),countryOfOrigin.getText()));

            goodPlace.getChildren().clear();
            fillGrid();

            title.setText("");
            manufacturer.setText("");
            countryOfOrigin.setText("");
            category.getSelectionModel().clearSelection();
            photoPath="";
            description.setText("");

            insert = new Alert(Alert.AlertType.INFORMATION, "Товар успешно добавлен!", ButtonType.OK);
            insert.showAndWait();

            DBConnection.closeConnection();
        }
    }
    private void delete(Good good){
        DBConnection.openConnection();
        DBConnection.statementExecute("Delete from Goods where articleNumber="+good.getArticleNumber());
        MainWindow.goods.remove(good);
        goodPlace.getChildren().clear();
        fillGrid();
        DBConnection.closeConnection();
    }
    @FXML
    private void exportToExcel() throws SQLException {
        DBConnection.openConnection();
        ResultSet ordersExcel = DBConnection.statementExecuteQuery("SELECT Goods.articleNumber, Categories.title, Goods.title, Goods.manufacturer, Goods.description, Goods.countryOfOrigin FROM Goods inner join Categories on Goods.category=Categories.number; ");
        //Create a Workbook instance
        Workbook workbook = new Workbook();
        //Get the first worksheet
        Worksheet sheet = workbook.getWorksheets().get(0);
        //Set sheet name
        sheet.setName("Товары");;
        //Hide gridlines
        sheet.setGridLinesVisible(true);

        //Add some data to the worksheet
        sheet.getRange().get(1,1).setValue("Артикул");
        sheet.getRange().get(1,2).setValue("Категория");
        sheet.getRange().get(1,3).setValue("Название");
        sheet.getRange().get(1,4).setValue("Производитель");
        sheet.getRange().get(1,5).setValue("Описание");
        sheet.getRange().get(1,6).setValue("Страна производства");

        int indx=2;

        while(ordersExcel.next()){

            try {
                sheet.getRange().get(indx,1).setValue(ordersExcel.getString(1));
                sheet.getRange().get(indx,2).setValue(ordersExcel.getString(2));
                sheet.getRange().get(indx,3).setValue(ordersExcel.getString(3));
                sheet.getRange().get(indx,4).setValue(ordersExcel.getString(4));
                sheet.getRange().get(indx,5).setValue(ordersExcel.getString(5));
                sheet.getRange().get(indx,6).setValue(ordersExcel.getString(6));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            indx++;
        }
        DBConnection.closeConnection();
        workbook.saveToFile("ProductsExport.xlsx", ExcelVersion.Version2016);
    }
//    поиск информации о товарах, которые есть не во всех магазинах;
//    поиск информации о товарах, на которые действуют акции в данный момент;
}
