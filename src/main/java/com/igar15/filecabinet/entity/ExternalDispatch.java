package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.Dispatch;
import com.igar15.filecabinet.entity.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "external_dispatches")
public class ExternalDispatch extends Dispatch {

    @NotBlank(message = "Letter outgoing number must not be blank")
    private String letterOutgoingNumber;

    @NotNull(message = "Company must not be empty")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Transient
    @ManyToMany
    @JoinTable(name = "document_external_dispatches",
            joinColumns = @JoinColumn(name = "external_dispatch_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> documentsSet;


    @ElementCollection
    @CollectionTable(name = "document_external_dispatches",
        joinColumns = @JoinColumn(name = "external_dispatch_id"))
    @MapKeyJoinColumn(name = "document_id")
    @Column(name = "is_active")
    private Map<Document, Boolean> documents;


    public ExternalDispatch() {
    }

    public ExternalDispatch(Integer id, String waybill, LocalDate dispatchDate, Status status, String remark, String letterOutgoingNumber,
                            Company company, Map<Document, Boolean> documents) {
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

    public Set<Document> getDocumentsSet() {
        return documentsSet;
    }

    public void setDocumentsSet(Set<Document> documentsSet) {
        this.documentsSet = documentsSet;
    }

    public Map<Document, Boolean> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<Document, Boolean> documents) {
        this.documents = documents;
    }
}
