package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.ElectronicImage;

import javax.persistence.*;

@Entity
@Table(name = "electronic_image_change_notices")
public class ElectronicImageChangeNotice extends ElectronicImage {

    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId
    @JoinColumn(name = "change_notice_id")
    private ChangeNotice changeNotice;

    public ElectronicImageChangeNotice() {
    }

    public ElectronicImageChangeNotice(String fileName, String contentType, ElectronicImageData electronicImageData, ChangeNotice changeNotice) {
        super(fileName, contentType, electronicImageData);
        this.changeNotice = changeNotice;
    }


    public ChangeNotice getChangeNotice() {
        return changeNotice;
    }

    public void setChangeNotice(ChangeNotice changeNotice) {
        this.changeNotice = changeNotice;
    }
}
