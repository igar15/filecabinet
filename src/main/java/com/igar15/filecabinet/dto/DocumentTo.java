package com.igar15.filecabinet.dto;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Stage;
import com.igar15.filecabinet.util.validation.ChangeNoticeNameValid;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class DocumentTo {

    private Integer id;

    @NotBlank(message = "Name must not be blank")
    @Size(min=5, max=100, message = "Name must be between 5 and 100 characters long")
    private String name;

    @NotBlank(message = "Decimal number must not be blank")
    @Size(min = 6, max = 100, message = "Decimal number must be between 6 and 100 characters long")
    private String decimalNumber;

    @NotNull(message = "Inventory number must not be empty")
    @Min(value = 1, message = "Inventory number must be greater than 0")
    private Integer inventoryNumber;

    private Stage stage;

    //@NotNull(message = "You must set developer name")
    private Developer developer;

    @ChangeNoticeNameValid
    private String tempChangeNoticeName;

    private List<ChangeNotice> changeNotices;

    public DocumentTo() {
        System.out.println("Hello from DocumentTO default constructor!");
    }

    public DocumentTo(Integer id, String name, String decimalNumber, Integer inventoryNumber, Stage stage, Developer developer, List<ChangeNotice> changeNotices) {
        this.id = id;
        this.name = name;
        this.decimalNumber = decimalNumber;
        this.inventoryNumber = inventoryNumber;
        this.stage = stage;
        this.developer = developer;
        this.changeNotices = changeNotices;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecimalNumber() {
        return decimalNumber;
    }

    public void setDecimalNumber(String decimalNumber) {
        this.decimalNumber = decimalNumber;
    }

    public Integer getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(Integer inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getTempChangeNoticeName() {
        return tempChangeNoticeName;
    }

    public void setTempChangeNoticeName(String tempChangeNoticeName) {
        this.tempChangeNoticeName = tempChangeNoticeName;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public List<ChangeNotice> getChangeNotices() {
        return changeNotices;
    }

    public void setChangeNotices(List<ChangeNotice> changeNotices) {
        this.changeNotices = changeNotices;
    }

}
