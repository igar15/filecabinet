package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.abstracts.ElectronicImage;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "electronic_image_data")
public class ElectronicImageData extends AbstractBaseEntity {

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "data")
    private byte[] data;


    public ElectronicImageData() {
    }

    public ElectronicImageData(byte[] data) {
        this.data = data;
    }


    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
