package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.entity.enums.Stage;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "documents")
public class Document extends AbstractNamedEntity {

    @NotBlank
    @Size(min = 6, max = 100)
    @Column(name = "decimal_number")
    private String decimalNumber;

    @Min(1)
    @Column(name = "inventory_number")
    private Integer inventoryNumber;

    @NotNull
    @Column(name = "receipt_date")
    private LocalDate receiptDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "applicability")
    private String applicability;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "form")
    private Form form;

    @Min(1)
    @Column(name = "change_number")
    private Integer changeNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage")
    private Stage stage;

    @Min(1)
    @Column(name = "sheets_amount")
    private Integer sheetsAmount;

    @Column(name = "format")
    private String format;

    @Min(1)
    @Column(name = "a4_amount")
    private Integer a4Amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private Developer developer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_holder_id")
    private Company originalHolder;

    @ManyToMany
    @JoinTable(name = "document_change_notices",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "change_notice_id"))
    @MapKeyColumn(name = "change")
    private Map<Integer, ChangeNotice> changeNotices;

    @ManyToMany
    @JoinTable(name = "document_companies",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))
    private List<Company> externalSubscribers;


    //private List<Sheet> sheets;



    public Document() {
    }

    public Document(Integer id, String name, String decimalNumber, Integer inventoryNumber, LocalDate receiptDate, Status status,
                    String applicability, Form form, Integer changeNumber, Stage stage, Integer sheetsAmount, String format,
                    Integer a4Amount, Developer developer, Company originalHolder, Map<Integer, ChangeNotice> changeNotices,
                    List<Company> externalSubscribers) {
        super(id, name);
        this.decimalNumber = decimalNumber;
        this.inventoryNumber = inventoryNumber;
        this.receiptDate = receiptDate;
        this.status = status;
        this.applicability = applicability;
        this.form = form;
        this.changeNumber = changeNumber;
        this.stage = stage;
        this.sheetsAmount = sheetsAmount;
        this.format = format;
        this.a4Amount = a4Amount;
        this.developer = developer;
        this.originalHolder = originalHolder;
        this.changeNotices = changeNotices;
        this.externalSubscribers = externalSubscribers;
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

    public Map<Integer, ChangeNotice> getChangeNotices() {
        return changeNotices;
    }

    public void setChangeNotices(Map<Integer, ChangeNotice> changeNotices) {
        this.changeNotices = changeNotices;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Company getOriginalHolder() {
        return originalHolder;
    }

    public void setOriginalHolder(Company originalHolder) {
        this.originalHolder = originalHolder;
    }

    public String getApplicability() {
        return applicability;
    }

    public void setApplicability(String applicability) {
        this.applicability = applicability;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Integer getChangeNumber() {
        return changeNumber;
    }

    public void setChangeNumber(Integer changeNumber) {
        this.changeNumber = changeNumber;
    }

    public List<Company> getExternalSubscribers() {
        return externalSubscribers;
    }

    public void setExternalSubscribers(List<Company> externalSubscribers) {
        this.externalSubscribers = externalSubscribers;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Integer getSheetsAmount() {
        return sheetsAmount;
    }

    public void setSheetsAmount(Integer sheetsAmount) {
        this.sheetsAmount = sheetsAmount;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getA4Amount() {
        return a4Amount;
    }

    public void setA4Amount(Integer a4Amount) {
        this.a4Amount = a4Amount;
    }
}
