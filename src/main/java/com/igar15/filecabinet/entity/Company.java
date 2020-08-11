package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractNamedEntity;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Table(name = "companies")
public class Company extends AbstractNamedEntity {

    @Embedded
    @Valid
    private Address address;

    @Column(name = "contact_person")
    private String contactPerson;


    public Company() {
        address = new Address();
    }

    public Company(Integer id, String name, Address address, String contactPerson) {
        super(id, name);
        this.address = address;
        this.contactPerson = contactPerson;
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

}
