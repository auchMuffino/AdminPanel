package needs.importantclasses;

import javafx.beans.property.SimpleStringProperty;

public class ChainStore {
    private SimpleStringProperty payersRegistrationNumber;
    private SimpleStringProperty title;
    private SimpleStringProperty legalName;
    private SimpleStringProperty businessAddress;

    public ChainStore(String payersRegistrationNumber, String title, String legalName, String businessAddress){
        this.payersRegistrationNumber=new SimpleStringProperty(payersRegistrationNumber);
        this.title=new SimpleStringProperty(title);
        this.legalName=new SimpleStringProperty(legalName);
        this.businessAddress=new SimpleStringProperty(businessAddress);
    }
    public String getPayersRegistrationNumber() {
        return payersRegistrationNumber.get();
    }
    public String getTitle() {
        return title.get();
    }
    public String getLegalName() {
        return legalName.get();
    }
    public String getBusinessAddress() {
        return businessAddress.get();
    }
    @Override
    public String toString() {
        return title.get();
    }
}
