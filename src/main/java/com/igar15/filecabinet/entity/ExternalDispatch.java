package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "external_dispatches")
public class ExternalDispatch extends Dispatch {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;



    public ExternalDispatch() {

    }

    public ExternalDispatch(Integer id, String waybill, LocalDate dispatchDate, Status status, Document document, String remark, Company company) {
        super(id, waybill, dispatchDate, status, document, remark);
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
