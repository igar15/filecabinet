package com.igar15.filecabinet.entity;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company extends AbstractNamedEntity{

    @Embedded
    @Valid
    private Address address;

    @Column(name = "contact_person")
    private String contactPerson;

    @Transient
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "document_companies",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private List<Document> documents;

    public Company() {
        address = new Address();
    }

    public Company(Integer id, String name, Address address, String contactPerson, List<Document> documents) {
        super(id, name);
        this.address = address;
        this.contactPerson = contactPerson;
        this.documents = documents;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}