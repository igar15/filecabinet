package com.igar15.filecabinet.dto;

import com.igar15.filecabinet.entity.Stage;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DocumentTo {

    private Integer id;

    @NotBlank
    @Size(min=5, max=100)
    private String name;

    @NotBlank
    @Size(min = 6, max = 100)
    private String decimalNumber;

    @NotNull
    @Min(value = 1)
    private Integer inventoryNumber;

    private Stage stage;

    @NotNull
    private String developerName;

    private Stage[] stages = Stage.values();

    private String[] developerNames;

    public DocumentTo() {

    }

    public DocumentTo(Integer id, String name, String decimalNumber, Integer inventoryNumber, Stage stage, String developerName, String[] developerNames) {
        this.id = id;
        this.name = name;
        this.decimalNumber = decimalNumber;
        this.inventoryNumber = inventoryNumber;
        this.stage = stage;
        this.developerName = developerName;
        this.developerNames = developerNames;
    }

    public DocumentTo(String[] developerNames) {
        this.developerNames = developerNames;
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

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public Stage[] getStages() {
        return stages;
    }

    public void setStages(Stage[] stages) {
        this.stages = stages;
    }

    public String[] getDeveloperNames() {
        return developerNames;
    }

    public void setDeveloperNames(String[] developerNames) {
        this.developerNames = developerNames;
    }

    @Override
    public String toString() {
        return "DocumentTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", decimalNumber='" + decimalNumber + '\'' +
                ", inventoryNumber=" + inventoryNumber +
                ", stage=" + stage +
                ", developer='" + developerName + '\'' +
                '}';
    }
}
