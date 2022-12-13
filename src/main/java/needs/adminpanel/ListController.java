package needs.adminpanel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import needs.importantclasses.ChainStore;
import needs.importantclasses.Good;
import needs.importantclasses.Price;
import needs.importantclasses.Promotion;
import needs.intefaces.ClickItem;

import java.util.Objects;

public class ListController{

    @FXML
    private Label goodTitle,goodCost;
    @FXML
    private ImageView garbage;
    private Good good;
    private ClickItem clickItem;
    private double discount=0;

    @FXML
    private void click(javafx.scene.input.MouseEvent mouseEvent) {
        clickItem.clickListener(good);
    }
    public void setData(Good good, ClickItem clickItem){
        this.good=good;
        this.clickItem=clickItem;
        goodTitle.setText(good.getTitle());
        goodCost.setText("Выберите Магазин");
    }
    public void setData(Good good, ChainStore store, ClickItem clickItem){
        this.good=good;
        this.clickItem=clickItem;
        goodTitle.setText(good.getTitle());
        for(Price pr : MainWindow.prices){
            discount=0;
            if((Objects.equals(pr.getGood().getArticleNumber(), good.getArticleNumber())) && (Objects.equals(pr.getStore().getPayersRegistrationNumber(), store.getPayersRegistrationNumber()))){
                goodCost.setText(pr.getPrice()+"BYN");
                for (Promotion promo:MainWindow.promotions){
                    if((Objects.equals(promo.getGood().getArticleNumber(), good.getArticleNumber())) && (Objects.equals(promo.getStore().getPayersRegistrationNumber(), store.getPayersRegistrationNumber())))
                        discount=promo.getDiscount();
                }
                ShoppingListController.totalCostQ+=(pr.getPrice()-(pr.getPrice()*discount));
            }
        }
        if(goodCost.getText().equals("Label"))
            goodCost.setText("Нет в магазине");
    }
}
