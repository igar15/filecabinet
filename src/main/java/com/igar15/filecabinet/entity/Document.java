package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractNamedEntity;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.entity.enums.Stage;
import com.igar15.filecabinet.util.validation.DecimalNumberNotDuplicate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@DecimalNumberNotDuplicate
@Entity
@Table(name = "documents")
public class Document extends AbstractNamedEntity {

    @NotBlank(message = "Decimal number must not be blank")
    @Column(name = "decimal_number")
    private String decimalNumber;

    @NotNull(message = "Inventory number must not be empty")
    @Min(value = 1, message = "Inventory number must be greater than 0")
    @Column(name = "inventory_number")
    private Integer inventoryNumber;

    @NotNull(message = "Issue date must not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "receipt_date")
    private LocalDate receiptDate;

    @NotNull(message = "Status must not be empty")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @NotNull(message = "Form must not be empty")
    @Enumerated(EnumType.STRING)
    @Column(name = "form")
    private Form form;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage")
    private Stage stage;

    @Min(value = 0, message = "Sheets amount must be greater than 0")
    @Column(name = "sheets_amount")
    private Integer sheetsAmount;

    @Column(name = "format")
    private String format;

    @Min(value = 0, message = "A4 amount must be greater than 0")
    @Column(name = "a4_amount")
    private Integer a4Amount;

    @NotNull(message = "Developer must not be empty")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @NotNull(message = "Original holder must not be empty")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "original_holder_id")
    private Company originalHolder;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "applicabilities",
            joinColumns = @JoinColumn(name = "inner_id"),
            inverseJoinColumns = @JoinColumn(name = "outer_id"))
    private Set<Document> applicabilities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "document_change_notices",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "change_notice_id"))
    @MapKeyColumn(name = "change")
    private Map<Integer, ChangeNotice> changeNotices;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "document_external_dispatches",
            joinColumns = @JoinColumn(name = "document_id"))
    @MapKeyJoinColumn(name = "external_dispatch_id")
    @Column(name = "is_active")
    private Map<ExternalDispatch, Boolean> externalDispatches;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "document_internal_dispatches",
            joinColumns = @JoinColumn(name = "document_id"))
    @MapKeyJoinColumn(name = "internal_dispatch_id")
    @Column(name = "is_active")
    private Map<InternalDispatch, Boolean> internalDispatches;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    private Set<ElectronicImageDocument> electronicImageDocuments;


    public Document() {
    }

    public Document(Integer id, String name, String decimalNumber, Integer inventoryNumber, LocalDate receiptDate, Status status,
                    Set<Document> applicabilities, Form form, Stage stage, Integer sheetsAmount, String format,
                    Integer a4Amount, Department department, Company originalHolder, Map<Integer, ChangeNotice> changeNotices,
                    Map<ExternalDispatch, Boolean> externalDispatches, Map<InternalDispatch, Boolean> internalDispatches) {
        super(id, name);
        this.decimalNumber = decimalNumber;
        this.inventoryNumber = inventoryNumber;
        this.receiptDate = receiptDate;
        this.status = status;
        this.applicabilities = applicabilities;
        this.form = form;
        this.stage = stage;
        this.sheetsAmount = sheetsAmount;
        this.format = format;
        this.a4Amount = a4Amount;
        this.department = department;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    public Map<ExternalDispatch, Boolean> getExternalDispatches() {
        return externalDispatches;
    }

    public void setExternalDispatches(Map<ExternalDispatch, Boolean> externalDispatches) {
        this.externalDispatches = externalDispatches;
    }

    public Map<InternalDispatch, Boolean> getInternalDispatches() {
        return internalDispatches;
    }

    public void setInternalDispatches(Map<InternalDispatch, Boolean> internalDispatches) {
        this.internalDispatches = internalDispatches;
    }

    public Set<ElectronicImageDocument> getElectronicImageDocuments() {
        return electronicImageDocuments;
    }

    public void setElectronicImageDocuments(Set<ElectronicImageDocument> electronicImageDocuments) {
        this.electronicImageDocuments = electronicImageDocuments;
    }
}
