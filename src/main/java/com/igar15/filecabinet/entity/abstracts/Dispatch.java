package com.igar15.filecabinet.entity.abstracts;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@MappedSuperclass
public abstract class Dispatch extends AbstractBaseEntity {

    @NotBlank
    @Column(name = "waybill")
    private String waybill;

    @NotNull
    @Column(name = "dispatch_date")
    private LocalDate dispatchDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "remark")
    private String remark;



    public Dispatch() {

    }

    public Dispatch(Integer id, String waybill, LocalDate dispatchDate, Status status, String remark) {
        super(id);
        this.waybill = waybill;
        this.dispatchDate = dispatchDate;
        this.status = status;
        this.remark = remark;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public LocalDate getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(LocalDate dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}