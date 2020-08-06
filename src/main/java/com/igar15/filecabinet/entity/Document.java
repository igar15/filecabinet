package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractNamedEntity;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.entity.enums.Stage;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "documents")
public class Document extends AbstractNamedEntity {

    @NotBlank(message = "document decimal number must not be blank")
    @Column(name = "decimal_number")
    private String decimalNumber;

    @NotNull(message = "document inventory number must not be empty")
    @Min(value = 1, message = "document inventory number must be greater than 0")
    @Column(name = "inventory_number")
    private Integer inventoryNumber;

    @NotNull(message = "document receipt date must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "receipt_date")
    private LocalDate receiptDate;

    @NotNull(message = "document status must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "applicabilities",
    joinColumns = @JoinColumn(name = "inner_id"),
    inverseJoinColumns = @JoinColumn(name = "outer_id"))
    private Set<Document> applicabilities;

    @NotNull(message = "document form must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "form")
    private Form form;

    @Min(value = 1, message = "document change number must be greater than 0")
    @Column(name = "change_number")
    private Integer changeNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage")
    private Stage stage;

    @Min(value = 1, message = "document sheets amount must be greater than 0")
    @Column(name = "sheets_amount")
    private Integer sheetsAmount;

    @Column(name = "format")
    private String format;

    @Min(value = 1, message = "document A4 amount must be greater than 0")
    @Column(name = "a4_amount")
    private Integer a4Amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "developer_id")
    private Developer developer;

    @NotNull(message = "document original holder must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "original_holder_id")
    private Company originalHolder;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "document_change_notices",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "change_notice_id"))
    @MapKeyColumn(name = "change")
    private Map<Integer, ChangeNotice> changeNotices;

//    @Transient
    @ManyToMany
    @JoinTable(name = "document_external_dispatches",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "external_dispatch_id"))
    private Set<ExternalDispatch> externalDispatches;

//    @Transient
    @ManyToMany
    @JoinTable(name = "document_internal_dispatches",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "internal_dispatch_id"))
    private Set<InternalDispatch> internalDispatches;




    //private List<Sheet> sheets;



    public Document() {
    }

    public Document(Integer id, String name, String decimalNumber, Integer inventoryNumber, LocalDate receiptDate,
                    Status status, Set<Document> applicabilities, Form form, Integer changeNumber, Stage stage,
                    Integer sheetsAmount, String format, Integer a4Amount, Developer developer, Company originalHolder) {
        super(id, name);
        this.decimalNumber = decimalNumber;
        this.inventoryNumber = inventoryNumber;
        this.receiptDate = receiptDate;
        this.status = status;
        this.applicabilities = applicabilities;
        this.form = form;
        this.changeNumber = changeNumber;
        this.stage = stage;
        this.sheetsAmount = sheetsAmount;
        this.format = format;
        this.a4Amount = a4Amount;
        this.developer = developer;
        this.originalHolder = originalHolder;
    }

    public Document(Integer id, String name, String decimalNumber, Integer inventoryNumber, LocalDate receiptDate, Status status,
                    Set<Document> applicabilities, Form form, Integer changeNumber, Stage stage, Integer sheetsAmount, String format,
                    Integer a4Amount, Developer developer, Company originalHolder, Map<Integer, ChangeNotice> changeNotices,
                    Set<ExternalDispatch> externalDispatches, Set<InternalDispatch> internalDispatches) {
        super(id, name);
        this.decimalNumber = decimalNumber;
        this.inventoryNumber = inventoryNumber;
        this.receiptDate = receiptDate;
        this.status = status;
        this.applicabilities = applicabilities;
        this.form = form;
        this.changeNumber = changeNumber;
        this.stage = stage;
        this.sheetsAmount = sheetsAmount;
        this.format = format;
        this.a4Amount = a4Amount;
        this.developer = developer;
        this.originalHolder = originalHolder;
        this.changeNotices = changeNotices;
        this.externalDispatches = externalDispatches;
        this.internalDispatches = internalDispatches;
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

    public Set<Document> getApplicabilities() {
        return applicabilities;
    }

    public void setApplicabilities(Set<Document> documents) {
        this.applicabilities = documents;
    }

        public Set<ExternalDispatch> getExternalDispatches() {
        return externalDispatches;
    }

    public void setExternalDispatches(Set<ExternalDispatch> externalDispatches) {
        this.externalDispatches = externalDispatches;
    }

    public Set<InternalDispatch> getInternalDispatches() {
        return internalDispatches;
    }

    public void setInternalDispatches(Set<InternalDispatch> internalDispatches) {
        this.internalDispatches = internalDispatches;
    }

    @Override
    public String toString() {
        return decimalNumber;
    }
}
