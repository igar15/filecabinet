package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.Dispatch;
import com.igar15.filecabinet.entity.enums.Stamp;
import com.igar15.filecabinet.entity.enums.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "internal_dispatches")
public class InternalDispatch extends Dispatch {

    @Enumerated(EnumType.STRING)
    @Column(name = "stamp")
    private Stamp stamp;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private Developer dispatchHandler;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "received_internal_date")
    private LocalDate receivedInternalDate;

    @NotBlank
    @Column(name = "internal_handler_name")
    private String internalHandlerName;

    @NotBlank
    @Column(name = "internal_handler_phone_number")
    private String internalHandlerPhoneNumber;

    @NotBlank
    @Column(name = "album_entry")
    private String albumEntry;


    public InternalDispatch() {

    }

    public InternalDispatch(Integer id, String waybill, LocalDate dispatchDate, Status status, String remark,
                            Stamp stamp, Developer dispatchHandler, Document document, LocalDate receivedInternalDate,
                            String internalHandlerName, String internalHandlerPhoneNumber, String albumEntry) {
        super(id, waybill, dispatchDate, status, remark);
        this.stamp = stamp;
        this.dispatchHandler = dispatchHandler;
        this.document = document;
        this.receivedInternalDate = receivedInternalDate;
        this.internalHandlerName = internalHandlerName;
        this.internalHandlerPhoneNumber = internalHandlerPhoneNumber;
        this.albumEntry = albumEntry;
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

    public void setDispatchHandler(Developer developer) {
        this.dispatchHandler = developer;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
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

    public String getAlbumEntry() {
        return albumEntry;
    }

    public void setAlbumEntry(String albumEntry) {
        this.albumEntry = albumEntry;
    }
}
