package com.igar15.filecabinet.dto;

import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.enums.Stamp;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.util.validation.AlbumEntryValid;
import com.igar15.filecabinet.util.validation.DecNumValid;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class InternalDispatchTo {

    private Integer id;

    @NotBlank(message = "waybill must not be blank")
    private String waybill;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dispatchDate;

    @NotNull
    private Status status;

    private String remark;

    @NotNull
    private Stamp stamp;

    @NotNull
    private Developer dispatchHandler;

    @DecNumValid
    private String tempDocumentDecimalNumber;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receivedInternalDate;

    @NotBlank
    private String internalHandlerName;

    @NotBlank
    private String internalHandlerPhoneNumber;

    @NotNull
    private Boolean isAlbum;

    @AlbumEntryValid
    private String albumName;

    private Set<Document> documents;


    public InternalDispatchTo() {

    }

    public InternalDispatchTo(Integer id, String waybill, LocalDate dispatchDate, Status status, String remark, Stamp stamp,
                              Developer dispatchHandler, LocalDate receivedInternalDate,
                              String internalHandlerName, String internalHandlerPhoneNumber, Boolean isAlbum, String albumName, Set<Document> documents) {
        this.id = id;
        this.waybill = waybill;
        this.dispatchDate = dispatchDate;
        this.status = status;
        this.remark = remark;
        this.stamp = stamp;
        this.dispatchHandler = dispatchHandler;
        this.receivedInternalDate = receivedInternalDate;
        this.internalHandlerName = internalHandlerName;
        this.internalHandlerPhoneNumber = internalHandlerPhoneNumber;
        this.isAlbum = isAlbum;
        this.albumName = albumName;
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

    public Stamp getStamp() {
        return stamp;
    }

    public void setStamp(Stamp stamp) {
        this.stamp = stamp;
    }

    public Developer getDispatchHandler() {
        return dispatchHandler;
    }

    public void setDispatchHandler(Developer dispatchHandler) {
        this.dispatchHandler = dispatchHandler;
    }

    public String getTempDocumentDecimalNumber() {
        return tempDocumentDecimalNumber;
    }

    public void setTempDocumentDecimalNumber(String tempDocumentDecimalNumber) {
        this.tempDocumentDecimalNumber = tempDocumentDecimalNumber;
    }

    public LocalDate getReceivedInternalDate() {
        return receivedInternalDate;
    }

    public void setReceivedInternalDate(LocalDate receivedInternalDate) {
        this.receivedInternalDate = receivedInternalDate;
    }

    public String getInternalHandlerName() {
        return internalHandlerName;
    }

    public void setInternalHandlerName(String internalHandlerName) {
        this.internalHandlerName = internalHandlerName;
    }

    public String getInternalHandlerPhoneNumber() {
        return internalHandlerPhoneNumber;
    }

    public void setInternalHandlerPhoneNumber(String internalHandlerPhoneNumber) {
        this.internalHandlerPhoneNumber = internalHandlerPhoneNumber;
    }

    public Boolean getIsAlbum() {
        return isAlbum;
    }

    public void setIsAlbum(Boolean album) {
        isAlbum = album;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}
