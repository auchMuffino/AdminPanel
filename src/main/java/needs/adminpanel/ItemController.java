package needs.adminpanel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import needs.importantclasses.Good;
import needs.intefaces.ClickItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class ItemController{

    @FXML
    private Label country;
    @FXML
    private Label description;
    @FXML
    private ImageView IMG,cross;
    @FXML
    private Label manufacturer;
    @FXML
    private Label title;
    @FXML
    private Label category;
    @FXML
    private void click(javafx.scene.input.MouseEvent mouseEvent) {
        if(mouseEvent.getSource()==cross){
            clickItem.clickDelete(good);
        }
        else{
            clickItem.clickListener(good);
        }
    }
    private Good good;
    private ClickItem clickItem;

    public void setData(Good good, ClickItem clickItem) throws FileNotFoundException, MalformedURLException {
        this.good=good;
        this.clickItem=clickItem;
        country.setText(good.getCountryOfOrigin());
        description.setText(good.getDescription());
        manufacturer.setText(good.getManufacturer());
        title.setText(good.getTitle());
        category.setText(good.getCategory().getTitle());
        System.out.println(good.getPicturePath());
        File file=new File(good.getPicturePath());
        Image img=new Image(file.toURI().toURL().toExternalForm());
        IMG.setImage(img);
    }
}
