package needs.adminpanel;


import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import needs.importantclasses.*;
import org.controlsfx.control.SearchableComboBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

public class product implements Initializable {

    @FXML
    TableView<Good> goodsTable;
    @FXML
    Button img;
    @FXML
    TextField title, manufacturer, country;
    @FXML
    TextArea description;
    @FXML
    SearchableComboBox<Category> category;

    private String filePath="";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getGoods();
        category.setItems(MainWindow.categories);
        EventHandler<MouseEvent> eventHandlerIMG = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Stage stage = new Stage();
                FileChooser fileChooser=new FileChooser();
                fileChooser.setTitle("Выберите фото");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Select...","*.jpg","*.jpeg","*.png"));
                File file=fileChooser.showOpenDialog(stage);
                if(file.exists())
                    filePath=file.getPath();
            }
        };
        img.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerIMG);
    }
    private void getGoods(){
        //create columns for table "goods"
        TableColumn<Good,String> goodTitle = new TableColumn<Good, String>("Товар");
        goodTitle.setCellValueFactory(new PropertyValueFactory<Good,String>("title"));
        TableColumn<Good,String> goodCategory=new TableColumn<Good, String>("Категория");
        goodCategory.setCellValueFactory(categoryWrapper->new ReadOnlyStringWrapper(categoryWrapper.getValue().getCategory().getTitle()));
        TableColumn<Good, String> goodManufacturer = new TableColumn<Good, String>("Производитель");
        goodManufacturer.setCellValueFactory(new PropertyValueFactory<Good,String>("manufacturer"));
        TableColumn<Good,String> goodDescription=new TableColumn<Good, String>("Описание");
        goodDescription.setCellValueFactory(new PropertyValueFactory<Good,String>("description"));
        TableColumn<Good, Date> goodCountry=new TableColumn<Good, Date>("Страна производитель");
        goodCountry.setCellValueFactory(new PropertyValueFactory<Good,Date>("countryOfOrigin"));

        goodsTable.getColumns().addAll(goodTitle, goodCategory, goodManufacturer, goodDescription, goodCountry); //adding columns in table "goods"
        goodsTable.setItems(MainWindow.goods); //filling table "goods"
    }
    @FXML
    private void insert(){
//        if()
    }
}
