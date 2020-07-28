package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.enums.Stamp;
import com.igar15.filecabinet.entity.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "internal_dispatches")
public class InternalDispatch extends Dispatch {

    @Enumerated(EnumType.STRING)
    @Column(name = "stamp")
    private Stamp stamp;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private Developer developer;




    public InternalDispatch() {

    }

    public InternalDispatch(Integer id, String waybill, LocalDate dispatchDate, Status status, Document document, String remark, Stamp stamp, Developer developer) {
        super(id, waybill, dispatchDate, status, document, remark);
        this.stamp = stamp;
        this.developer = developer;
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
}
