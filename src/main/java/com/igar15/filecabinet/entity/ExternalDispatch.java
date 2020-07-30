package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "external_dispatches")
public class ExternalDispatch extends Dispatch {

    @NotBlank
    private String letterOutgoingNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany
    @JoinTable(name = "document_external_dispatches",
            joinColumns = @JoinColumn(name = "external_dispatch_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> documents;



    public ExternalDispatch() {

    }

    public ExternalDispatch(Integer id, String waybill, LocalDate dispatchDate, Status status, String remark, String letterOutgoingNumber,
                            Company company, Set<Document> documents) {
        super(id, waybill, dispatchDate, status, remark);
        this.letterOutgoingNumber = letterOutgoingNumber;
        this.company = company;
        this.documents = documents;
    }

    public String getLetterOutgoingNumber() {
        return letterOutgoingNumber;
    }

    public void setLetterOutgoingNumber(String letterOutgoingNumber) {
        this.letterOutgoingNumber = letterOutgoingNumber;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}
