package com.igar15.filecabinet.entity;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "documents")
public class Document extends AbstractNamedEntity {

    @NotBlank
    @Size(min = 6, max = 100)
    @Column(name = "decimal_number")
    private String decimalNumber;

    @Range(min = 1)
    @Column(name = "inventory_number")
    private Integer inventoryNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage")
    private Stage stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private Developer developer;

    @ManyToMany
    @JoinTable(name = "document_change_notices",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "change_notice_id"))
    private List<ChangeNotice> changeNotices;

    public Document() {
    }

    public Document(Integer id, String name, String decimalNumber, Integer inventoryNumber, Stage stage, Developer developer, List<ChangeNotice> changeNotices) {
        super(id, name);
        this.decimalNumber = decimalNumber;
        this.inventoryNumber = inventoryNumber;
        this.stage = stage;
        this.developer = developer;
        this.changeNotices = changeNotices;
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
