package needs.adminpanel;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import needs.importantclasses.Good;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {

    @FXML
    GridPane goodPlace;
//    @FXML
//    SearchableComboBox<Good> search;
//
//    private StringBuilder s=new StringBuilder();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillGrid();
//        search.setItems(MainWindow.goods);
//        EventHandler<KeyEvent> eventHandlerSearch = new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent e) {
//                if(e.getCode()== KeyCode.BACK_SPACE)
//                    s.deleteCharAt(s.length());
//                else
//                    s.append(e.getCharacter());
//                fillGrid(s.toString());
//            }
//        };
//        search.addEventFilter(KeyEvent.KEY_PRESSED, eventHandlerSearch);
    }

    public void fillGrid(){
        int column=0;
        int row=0;

        try {
            for(int i=0;i<MainWindow.goods.size();i++){

                FXMLLoader fxmlLoader=new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Views/item.fxml"));

                BorderPane anchorPane=fxmlLoader.load();

                ItemController itemController=fxmlLoader.getController();
                itemController.setData(MainWindow.goods.get(i));

                if(column==3) {
                    column = 0;
                    row++;
                }

                goodPlace.add(anchorPane,column++,row);
                GridPane.setMargin(anchorPane,new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//    public void fillGrid(String titleSearch){
//        int column=0;
//        int row=0;
//
//        try {
//            for(int i=0;i<MainWindow.goods.size();i++){
//                if(!MainWindow.goods.get(i).getTitle().startsWith(titleSearch))
//                    continue;
//
//                FXMLLoader fxmlLoader=new FXMLLoader();
//                fxmlLoader.setLocation(getClass().getResource("Views/item.fxml"));
//
//                BorderPane anchorPane=fxmlLoader.load();
//
//                ItemController itemController=fxmlLoader.getController();
//                itemController.setData(MainWindow.goods.get(i));
//
//                if(column==3) {
//                    column = 0;
//                    row++;
//                }
//
//                goodPlace.add(anchorPane,column++,row);
//                GridPane.setMargin(anchorPane,new Insets(10));
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
