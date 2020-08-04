package com.igar15.filecabinet.dto;

import com.igar15.filecabinet.entity.*;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Stage;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.util.validation.ChangeNoticeNameAndDocChangeNumberValid;
import com.igar15.filecabinet.util.validation.ChangeNoticeNameValid;
import com.igar15.filecabinet.util.validation.DecNumValid;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@ChangeNoticeNameAndDocChangeNumberValid(tempChangeNoticeName = "tempChangeNoticeName", tempChangeNoticeNumber = "tempChangeNoticeNumber")
public class DocumentTo {

    private Integer id;

    @NotBlank(message = "document name must not be blank")
    private String name;

    @NotBlank(message = "document decimal number must not be blank")
    private String decimalNumber;

    @NotNull(message = "document inventory number must not be empty")
    @Min(value = 1, message = "document inventory number must be greater than 0")
    private Integer inventoryNumber;

    @NotNull(message = "document receipt date must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiptDate;

    @NotNull(message = "document status must not be null")
    private Status status;

    private Set<Document> applicabilities;

    @DecNumValid
    private String tempApplicability;

    @NotNull(message = "document form must not be null")
    private Form form;

    @Min(value = 1, message = "document change number must be greater than 0")
    private Integer changeNumber;

    private Stage stage;

    @Min(value = 1, message = "document sheets amount must be greater than 0")
    private Integer sheetsAmount;

    private String format;

    @Min(value = 1, message = "document A4 amount must be greater than 0")
    private Integer a4Amount;

    //@NotNull
    private Developer developer;

    @NotNull(message = "document original holder must not be null")
    private Company originalHolder;

    private String tempChangeNoticeName;

    private String tempChangeNoticeNumber;

    //private Set<String> changeNotices;
    private Map<Integer, ChangeNotice> changeNotices;



    public DocumentTo() {

    }

    public DocumentTo(Integer id, String name, String decimalNumber, Integer inventoryNumber, LocalDate receiptDate, Status status,
                      Set<Document> applicabilities, Form form, Integer changeNumber, Stage stage, Integer sheetsAmount, String format,
                      Integer a4Amount, Developer developer, Company originalHolder,
                      Map<Integer, ChangeNotice> changeNotices) {
        this.id = id;
        this.name = name;
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

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Document> getApplicabilities() {
        return applicabilities;
    }

    public void setApplicabilities(Set<Document> applicabilities) {
        this.applicabilities = applicabilities;
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public Company getOriginalHolder() {
        return originalHolder;
    }

    public void setOriginalHolder(Company originalHolder) {
        this.originalHolder = originalHolder;
    }

    public String getTempChangeNoticeName() {
        return tempChangeNoticeName;
    }

    public void setTempChangeNoticeName(String tempChangeNoticeName) {
        this.tempChangeNoticeName = tempChangeNoticeName;
    }

    public String getTempChangeNoticeNumber() {
        return tempChangeNoticeNumber;
    }

    public void setTempChangeNoticeNumber(String tempChangeNoticeNumber) {
        this.tempChangeNoticeNumber = tempChangeNoticeNumber;
    }

    public Map<Integer, ChangeNotice> getChangeNotices() {
        return changeNotices;
    }

    public void setChangeNotices(Map<Integer, ChangeNotice> changeNotices) {
        this.changeNotices = changeNotices;
    }

    public String getTempApplicability() {
        return tempApplicability;
    }

    public void setTempApplicability(String tempApplicability) {
        this.tempApplicability = tempApplicability;
    }
}
