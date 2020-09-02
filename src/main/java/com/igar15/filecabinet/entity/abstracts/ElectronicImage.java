package com.igar15.filecabinet.entity.abstracts;

import com.igar15.filecabinet.entity.ElectronicImageData;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@MappedSuperclass
public class ElectronicImage extends AbstractBaseEntity {

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "electronic_image_data_id")
    private ElectronicImageData electronicImageData;

    public ElectronicImage() {
    }

    public ElectronicImage(String fileName, String fileType, ElectronicImageData electronicImageData) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.electronicImageData = electronicImageData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public ElectronicImageData getElectronicImageData() {
        return electronicImageData;
    }

    public void setElectronicImageData(ElectronicImageData electronicImageData) {
        this.electronicImageData = electronicImageData;
    }
}
