package needs.importantclasses;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class Promotion{
    private SimpleIntegerProperty number;
    private SimpleObjectProperty<Good> good;
    private SimpleStringProperty title;
    private SimpleObjectProperty<ChainStore> store;
    private SimpleStringProperty description;
    private SimpleStringProperty picturePath;
    private SimpleObjectProperty<Date> startDate;
    private SimpleObjectProperty<Date> endDate;
    private SimpleDoubleProperty discount;

    public Promotion(int number, Good good, String title, ChainStore store, String description, String picturePath, Date startDate, Date endDate,double discount){
        this.number=new SimpleIntegerProperty(number);
        this.good=new SimpleObjectProperty<Good>(good);
        this.title=new SimpleStringProperty(title);
        this.store=new SimpleObjectProperty<ChainStore>(store);
        this.description=new SimpleStringProperty(description);
        this.picturePath=new SimpleStringProperty(picturePath);
        this.startDate=new SimpleObjectProperty<Date>(startDate);
        this.endDate=new SimpleObjectProperty<Date>(endDate);
        this.discount=new SimpleDoubleProperty(discount);
    }
    public int getNumber() {
        return number.get();
    }
    public Good getGood() {
        return good.get();
    }
    public String getTitle() {
        return title.get();
    }
    public ChainStore getStore() {
        return store.get();
    }
    public String getDescription() {
        return description.get();
    }
    public String getPicturePath() {
        return picturePath.get();
    }
    public Date getStartDate() {
        return startDate.get();
    }
    public Date getEndDate() {
        return endDate.get();
    }
    public double getDiscount() {
        return discount.get();
    }
    @Override
    public String toString(){
        return title.get();
    }
}
