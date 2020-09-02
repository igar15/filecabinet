package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "db_files")
public class DbFile extends AbstractBaseEntity {

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "data")
    private byte[] data;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    public DbFile() {
    }

    public DbFile(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
