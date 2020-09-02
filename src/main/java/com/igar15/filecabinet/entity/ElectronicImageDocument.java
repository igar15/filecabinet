package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.ElectronicImage;

import javax.persistence.*;

@Entity
@Table(name = "electronic_image_documents")
public class ElectronicImageDocument extends ElectronicImage {

    @Column(name = "non_annulled")
    private Boolean nonAnnulled;

    @Column(name = "change_number")
    private Integer changeNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    public ElectronicImageDocument() {
    }

    public ElectronicImageDocument(String fileName, String fileType, ElectronicImageData electronicImageData, Boolean nonAnnulled, Integer changeNumber, Document document) {
        super(fileName, fileType, electronicImageData);
        this.nonAnnulled = nonAnnulled;
        this.changeNumber = changeNumber;
        this.document = document;
    }

    public Boolean getNonAnnulled() {
        return nonAnnulled;
    }

    public void setNonAnnulled(Boolean nonAnnulled) {
        this.nonAnnulled = nonAnnulled;
    }

    public Integer getChangeNumber() {
        return changeNumber;
    }

    public void setChangeNumber(Integer changeNumber) {
        this.changeNumber = changeNumber;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
