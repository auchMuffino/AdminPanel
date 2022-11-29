package needs.importantclasses;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Category {

    private SimpleIntegerProperty id;
    private SimpleStringProperty title;

    public Category(int id, String title){
        this.id=new SimpleIntegerProperty(id);
        this.title=new SimpleStringProperty(title);
    }

    public int getId() {
        return id.get();
    }
    public String getTitle(){
        return title.get();
    }
    public String toString(){
        return title.get();
    }
}
