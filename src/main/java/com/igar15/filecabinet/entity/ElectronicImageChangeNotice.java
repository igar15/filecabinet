package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.ElectronicImage;

import javax.persistence.*;

@Entity
@Table(name = "electronic_image_change_notices")
public class ElectronicImageChangeNotice extends ElectronicImage {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "change_notice_id")
    private ChangeNotice changeNotice;



    public ChangeNotice getChangeNotice() {
        return changeNotice;
    }

    public void setChangeNotice(ChangeNotice changeNotice) {
        this.changeNotice = changeNotice;
    }
}
