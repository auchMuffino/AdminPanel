package needs.importantclasses;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Price {
    private SimpleIntegerProperty id;
    private SimpleObjectProperty<Good> good;
    private SimpleObjectProperty<ChainStore> store;
    private SimpleDoubleProperty price;

    public Price(int id,Good good, ChainStore store, double price){
        this.id=new SimpleIntegerProperty(id);
        this.good=new SimpleObjectProperty<Good>(good);
        this.store=new SimpleObjectProperty<ChainStore>(store);
        this.price=new SimpleDoubleProperty(price);
    }

    public int getId() {
        return id.get();
    }
    public Good getGood() {
        return good.get();
    }
    public ChainStore getStore() {
        return store.get();
    }
    public double getPrice() {
        return price.get();
    }
}
