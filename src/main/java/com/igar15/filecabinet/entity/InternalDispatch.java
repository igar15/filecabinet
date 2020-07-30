package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.enums.Stamp;
import com.igar15.filecabinet.entity.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "internal_dispatches")
public class InternalDispatch extends Dispatch {

    @Enumerated(EnumType.STRING)
    @Column(name = "stamp")
    private Stamp stamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private Developer developer;

    @ManyToMany
    @JoinTable(name = "document_internal_dispatches",
            joinColumns = @JoinColumn(name = "internal_dispatch_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> documents;




    public InternalDispatch() {

    }

    public InternalDispatch(Integer id, String waybill, LocalDate dispatchDate, Status status, String remark,
                            Stamp stamp, Developer developer, Set<Document> documents) {
        super(id, waybill, dispatchDate, status, remark);
        this.stamp = stamp;
        this.developer = developer;
        this.documents = documents;
    }

    public Stamp getStamp() {
        return stamp;
    }

    public void setStamp(Stamp stamp) {
        this.stamp = stamp;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}
