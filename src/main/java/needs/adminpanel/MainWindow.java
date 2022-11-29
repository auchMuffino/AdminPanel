package needs.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import needs.importantclasses.*;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    @FXML
    private HBox costs;

    @FXML
    private HBox functions;

    @FXML
    private HBox products;

    @FXML
    private HBox salles;

    @FXML
    private HBox shoppingList;

    @FXML
    private HBox stores;

    @FXML
    private HBox workSpace;

    static ObservableList<Price> prices = FXCollections.observableArrayList();
    static ObservableList<ChainStore> chainStores = FXCollections.observableArrayList();
    static ObservableList<Good> goods = FXCollections.observableArrayList();
    static ObservableList<Promotion> promotions = FXCollections.observableArrayList();
    static ObservableList<Category> categories = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getRecods();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        EventHandler<MouseEvent> eventHandlerShoppingList = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    workSpace.getChildren().removeAll(workSpace.getChildren());
                    FXMLLoader fxmlLoader=new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("shoppingList.fxml"));
                    AnchorPane anchorPane=fxmlLoader.load();
                    workSpace.getChildren().addAll(anchorPane);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        shoppingList.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerShoppingList);

        EventHandler<MouseEvent> eventHandlerStores = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    workSpace.getChildren().removeAll(workSpace.getChildren());
                    FXMLLoader fxmlLoader=new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("stores.fxml"));
                    AnchorPane anchorPane=fxmlLoader.load();
                    workSpace.getChildren().addAll(anchorPane);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        stores.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerStores);

        EventHandler<MouseEvent> eventHandlerSalles = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    workSpace.getChildren().removeAll(workSpace.getChildren());
                    FXMLLoader fxmlLoader=new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("salles.fxml"));
                    AnchorPane anchorPane=fxmlLoader.load();
                    workSpace.getChildren().addAll(anchorPane);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        salles.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerSalles);

        EventHandler<MouseEvent> eventHandlerProducts = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    workSpace.getChildren().removeAll(workSpace.getChildren());
                    FXMLLoader fxmlLoader=new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("good.fxml"));
                    BorderPane anchorPane=fxmlLoader.load();
                    workSpace.getChildren().addAll(anchorPane);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        products.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerProducts);

        EventHandler<MouseEvent> eventHandlerFunctions = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    workSpace.getChildren().removeAll(workSpace.getChildren());
                    FXMLLoader fxmlLoader=new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("functions.fxml"));
                    AnchorPane anchorPane=fxmlLoader.load();
                    workSpace.getChildren().addAll(anchorPane);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        functions.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerFunctions);

        EventHandler<MouseEvent> eventHandlerCosts = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    workSpace.getChildren().removeAll(workSpace.getChildren());
                    FXMLLoader fxmlLoader=new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("costs.fxml"));
                    AnchorPane anchorPane=fxmlLoader.load();
                    workSpace.getChildren().addAll(anchorPane);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        costs.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerCosts);

    }

    public void getRecods() throws SQLException {
        DBConnection.openConnection();

        ResultSet freshCategories= DBConnection.statementExecuteQuery("select * from Categories;");
        ResultSet freshChainStores= DBConnection.statementExecuteQuery("select * from ChainStores;");
        ResultSet freshGoods= DBConnection.statementExecuteQuery("select * from Goods;");
        ResultSet freshPromotions= DBConnection.statementExecuteQuery("select * from Promotions;");
        ResultSet freshPrices= DBConnection.statementExecuteQuery("select * from Prices;");

        while(freshCategories.next()){
            System.out.println(freshCategories.getString(2));
            categories.add(new Category(freshCategories.getInt(1), freshCategories.getString(2)));
        }
        while(freshChainStores.next()){
            chainStores.add(new ChainStore(freshChainStores.getString(1),freshChainStores.getString(2),freshChainStores.getString(3),freshChainStores.getString(4)));
        }
        while(freshGoods.next()){
            for(Category category:categories)
                if(category.getId()==freshGoods.getInt(2))
                    goods.add(new Good(freshGoods.getString(1),category,freshGoods.getString(3),freshGoods.getString(4),freshGoods.getString(5),freshGoods.getString(6),freshGoods.getString(7)));
        }
        while(freshPromotions.next()){
            Good g=null;
            ChainStore s=null;
            for(Good good:goods)
                if(good.getArticleNumber().equals(freshPromotions.getString(2)))
                    g=good;
            for(ChainStore store:chainStores)
                if(store.getPayersRegistrationNumber().equals(freshPromotions.getString(4)))
                    s=store;
            promotions.add(new Promotion(freshPromotions.getInt(1),g,freshPromotions.getString(3),s,freshPromotions.getString(5),freshPromotions.getString(6), freshPromotions.getDate(7), freshPromotions.getDate(8)));
        }
        while(freshPrices.next()){
            Good g=null;
            ChainStore s=null;
            for(Good good:goods)
                if(good.getArticleNumber().equals(freshPrices.getString(2)))
                    g=good;
            for(ChainStore store:chainStores)
                if(store.getPayersRegistrationNumber().equals(freshPrices.getString(3)))
                    s=store;
            prices.add(new Price(freshPrices.getInt(1),g,s,freshPrices.getDouble(4)));
        }
        DBConnection.closeConnection();
    }
}
