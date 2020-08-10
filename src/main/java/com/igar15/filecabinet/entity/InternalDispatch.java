package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.Dispatch;
import com.igar15.filecabinet.entity.enums.Stamp;
import com.igar15.filecabinet.entity.enums.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

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

    @Transient
    @ManyToMany
    @JoinTable(name = "document_internal_dispatches",
            joinColumns = @JoinColumn(name = "internal_dispatch_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> documentsSet;

    @ElementCollection
    @CollectionTable(name = "document_internal_dispatches",
            joinColumns = @JoinColumn(name = "internal_dispatch_id"))
    @MapKeyJoinColumn(name = "document_id")
    @Column(name = "is_active")
    private Map<Document, Boolean> documents;

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

    @NotNull
    @Column(name = "is_album")
    private Boolean isAlbum;

    @Column(name = "album_name")
    private String albumName;

    @Column(name = "is_active")
    private boolean isActive = true;



    public InternalDispatch() {

    }

    public InternalDispatch(Integer id, String waybill, LocalDate dispatchDate, Status status, String remark,
                            Stamp stamp, Developer dispatchHandler, Set<Document> documents, LocalDate receivedInternalDate,
                            String internalHandlerName, String internalHandlerPhoneNumber, Boolean isAlbum, String albumName) {
        super(id, waybill, dispatchDate, status, remark);
        this.stamp = stamp;
        this.dispatchHandler = dispatchHandler;
        this.documentsSet = documents;
        this.receivedInternalDate = receivedInternalDate;
        this.internalHandlerName = internalHandlerName;
        this.internalHandlerPhoneNumber = internalHandlerPhoneNumber;
        this.isAlbum = isAlbum;
        this.albumName = albumName;
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

    public Set<Document> getDocumentsSet() {
        return documentsSet;
    }

    public void setDocumentsSet(Set<Document> documents) {
        this.documentsSet = documents;
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

    public void setAlbumName(String albumEntry) {
        this.albumName = albumEntry;
    }

    public Map<Document, Boolean> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<Document, Boolean> documents) {
        this.documents = documents;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
