package com.igar15.filecabinet.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company extends AbstractNamedEntity{

    @Embedded
    private Address address;

    @ManyToMany
    @JoinTable(name = "document_companies",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private List<Document> documents;

    public Company() {
        address = new Address();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
