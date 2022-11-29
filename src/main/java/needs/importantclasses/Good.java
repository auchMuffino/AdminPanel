package needs.importantclasses;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Good {
    private SimpleStringProperty articleNumber;
    private SimpleObjectProperty<Category> category;
    private SimpleStringProperty picturePath;
    private SimpleStringProperty title;
    private SimpleStringProperty manufacturer;
    private SimpleStringProperty description;
    private SimpleStringProperty countryOfOrigin;

    public Good(String articleNumber, Category category, String picturePath, String title, String manufacturer, String description, String countryOfOrigin){
        this.articleNumber=new SimpleStringProperty(articleNumber);
        this.category=new SimpleObjectProperty<Category>(category);
        this.picturePath=new SimpleStringProperty(picturePath);
        this.title=new SimpleStringProperty(title);
        this.manufacturer=new SimpleStringProperty(manufacturer);
        this.description=new SimpleStringProperty(description);
        this.countryOfOrigin=new SimpleStringProperty(countryOfOrigin);
    }

    public String getArticleNumber() {
        return articleNumber.get();
    }
    public Category getCategory() {
        return category.get();
    }
    public String getPicturePath() {
        return picturePath.get();
    }
    public String getTitle() {
        return title.get();
    }
    public String getManufacturer() {
        return manufacturer.get();
    }
    public String getDescription() {
        return description.get();
    }
    public String getCountryOfOrigin() {
        return countryOfOrigin.get();
    }
}
