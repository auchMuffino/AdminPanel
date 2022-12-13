package needs.adminpanel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import needs.importantclasses.*;
import needs.intefaces.ClickItem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ShoppingListController implements Initializable {

    @FXML
    private ComboBox<ChainStore> store;
    @FXML
    private Label totalCost;
    @FXML
    private GridPane list;
    private ClickItem clickItem;
    static double totalCostQ=0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillList();

        EventHandler<ActionEvent> eventStoreSelected = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                list.getChildren().clear();
                if(store.getSelectionModel().isEmpty())
                    fillList();
                else {
                    fillList(store.getSelectionModel().getSelectedItem());
                }
            }
        };

        store.setOnAction(eventStoreSelected);
        store.setItems(MainWindow.chainStores);

    }
    private void fillList(){
        final int COLUMN=0;
        int row=0;
        this.clickItem=new ClickItem() {
            @Override
            public void clickListener(Good good) {
                totalCostQ=0;
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Вы действительно хотите убрать товар из списка?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult()==ButtonType.YES){
                    delete(good);
                }
            }
            @Override
            public void clickDelete(Good good) {
                System.out.println("Ты как это сделал?????");
            }
        };
        try {
            for(int i=0;i<ProductsController.records.size();i++){

                FXMLLoader fxmlLoader=new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Views/ListRecord.fxml"));

                BorderPane anchorPane=fxmlLoader.load();

                ListController listController=fxmlLoader.getController();
                listController.setData(ProductsController.records.get(i), clickItem);

                list.add(anchorPane,COLUMN,row++);

                list.setMinWidth(Region.USE_COMPUTED_SIZE);
                list.setPrefWidth(Region.USE_COMPUTED_SIZE);
                list.setMaxWidth(Region.USE_COMPUTED_SIZE);
                list.setMinHeight(Region.USE_COMPUTED_SIZE);
                list.setPrefHeight(Region.USE_COMPUTED_SIZE);
                list.setMaxHeight(Region.USE_COMPUTED_SIZE);
                GridPane.setMargin(anchorPane,new Insets(3));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        totalCost.setText("0.0");
    }
    private void fillList(ChainStore shop){
        totalCostQ=0;
        if(!ProductsController.records.isEmpty()) {
            final int COLUMN = 0;
            int row = 0;
            this.clickItem = new ClickItem() {
                @Override
                public void clickListener(Good good) {
                    totalCostQ=0;
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Вы действительно хотите убрать товар из списка?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if(alert.getResult()==ButtonType.YES){
                        delete(good);
                    }
                }

                @Override
                public void clickDelete(Good good) {
                    System.out.println("Ты как это сделал?????");
                }
            };
            try {
                for (int i = 0; i < ProductsController.records.size(); i++) {

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("Views/ListRecord.fxml"));

                    BorderPane anchorPane = fxmlLoader.load();

                    ListController listController = fxmlLoader.getController();
                    listController.setData(ProductsController.records.get(i), shop, clickItem);

                    list.add(anchorPane, COLUMN, row++);

                    list.setMinWidth(Region.USE_COMPUTED_SIZE);
                    list.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    list.setMaxWidth(Region.USE_COMPUTED_SIZE);
                    list.setMinHeight(Region.USE_COMPUTED_SIZE);
                    list.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    list.setMaxHeight(Region.USE_COMPUTED_SIZE);
                    GridPane.setMargin(anchorPane, new Insets(3));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            totalCost.setText(totalCostQ + "BYN");
        }
        else
            totalCost.setText("0.0BYN");
    }
    private void delete(Good good){
        ProductsController.records.remove(good);
        list.getChildren().clear();

        if(!store.getSelectionModel().isEmpty())
            fillList(store.getSelectionModel().getSelectedItem());
        else
            fillList();
    }
}
