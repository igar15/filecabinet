package com.igar15.filecabinet.dto;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.util.validation.DecNumValid;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class ExternalDispatchTo {


    private Integer id;

    @NotBlank(message = "waybill must not be blank")
    private String waybill;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dispatchDate;

    @NotNull
    private Status status;

    private String remark;

    @NotBlank(message = "letter outgoing number must not be null")
    private String letterOutgoingNumber;

    @NotNull
    private Company company;

    @DecNumValid
    private String tempDocumentDecimalNumber;

    private Set<Document> documents;




    public ExternalDispatchTo() {

    }

    public ExternalDispatchTo(Integer id, String waybill, LocalDate dispatchDate, Status status, String remark,
                              String letterOutgoingNumber,Company company, Set<Document> documents) {
        this.id = id;
        this.waybill = waybill;
        this.dispatchDate = dispatchDate;
        this.status = status;
        this.remark = remark;
        this.letterOutgoingNumber = letterOutgoingNumber;
        this.company = company;
        this.documents = documents;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public LocalDate getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(LocalDate dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLetterOutgoingNumber() {
        return letterOutgoingNumber;
    }

    public void setLetterOutgoingNumber(String letterOutgoingNumber) {
        this.letterOutgoingNumber = letterOutgoingNumber;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getTempDocumentDecimalNumber() {
        return tempDocumentDecimalNumber;
    }

    public void setTempDocumentDecimalNumber(String tempDocumentDecimalNumber) {
        this.tempDocumentDecimalNumber = tempDocumentDecimalNumber;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}
