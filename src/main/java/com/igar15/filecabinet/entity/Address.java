package com.igar15.filecabinet.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Address {

    @NotBlank(message = "City must not be blank")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "Street must not be blank")
    @Column(name = "street")
    private String street;

    @NotBlank(message = "Building must not be blank")
    @Column(name = "building")
    private String building;

    @NotBlank(message = "Zipcode must not be blank")
    @Column(name = "zipcode")
    private String zipcode;


    public Address() {
    }

    public Address(String city, String street, String building, String zipcode) {
        this.city = city;
        this.street = street;
        this.building = building;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}
