package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractNamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "developers")
public class Developer extends AbstractNamedEntity {

    @Column(name = "chief_name")
    @NotBlank
    @Size(min = 5, max = 100)
    private String chiefName;

    @Column(name = "description")
    private String description;

    @Column(name = "workers_amount")
    @Min(1)
    private Integer workersAmount;


    public Developer() {

    }

    public Developer(Integer id, String name, String chiefName, String description, Integer workersAmount) {
        super(id, name);
        this.chiefName = chiefName;
        this.description = description;
        this.workersAmount = workersAmount;
    }

    public String getChiefName() {
        return chiefName;
    }

    public void setChiefName(String chiefName) {
        this.chiefName = chiefName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWorkersAmount() {
        return workersAmount;
    }

    public void setWorkersAmount(Integer workersAmount) {
        this.workersAmount = workersAmount;
    }
}
