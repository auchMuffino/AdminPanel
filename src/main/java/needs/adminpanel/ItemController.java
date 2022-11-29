package needs.adminpanel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import needs.importantclasses.Good;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private Label country;

    @FXML
    private Label description;

    @FXML
    private ImageView IMG;

    @FXML
    private Label manufacturer;

    @FXML
    private Label title;
    @FXML
    private Label category;

    private Good good;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Good good){
        this.good=good;
        country.setText(good.getCountryOfOrigin());
        description.setText(good.getDescription());
        manufacturer.setText(good.getManufacturer());
        title.setText(good.getTitle());
        category.setText(good.getCategory().getTitle());
//        IMG.setImage(new Image(getClass().getResourceAsStream(good.getPicturePath())));
    }
}
