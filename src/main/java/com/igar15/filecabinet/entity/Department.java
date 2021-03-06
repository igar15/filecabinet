package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractNamedEntity;
import com.igar15.filecabinet.util.validation.DepartmentNameNotDuplicate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@DepartmentNameNotDuplicate
@Entity
@Table(name = "departments")
public class Department extends AbstractNamedEntity {

    @Column(name = "chief_name")
    @NotBlank(message = "Chief name must not be blank")
    private String chiefName;

    @Column(name = "description")
    private String description;

    @Column(name = "workers_amount")
    @Min(1)
    private Integer workersAmount;

    @NotNull(message = "\"Is developer\" mark must not be empty")
    @Column(name = "is_developer")
    private Boolean isDeveloper;

    @NotNull(message = "\"Use albums\" mark must not be empty")
    @Column(name = "can_take_albums")
    private Boolean canTakeAlbums;


    public Department() {

    }

    public Department(Integer id, String name, String chiefName, String description, Integer workersAmount, Boolean isDeveloper, Boolean canTakeAlbums) {
        super(id, name);
        this.chiefName = chiefName;
        this.description = description;
        this.workersAmount = workersAmount;
        this.isDeveloper = isDeveloper;
        this.canTakeAlbums = canTakeAlbums;
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

    public Boolean getIsDeveloper() {
        return isDeveloper;
    }

    public void setIsDeveloper(Boolean aDevelop) {
        this.isDeveloper = aDevelop;
    }

    public Boolean getCanTakeAlbums() {
        return canTakeAlbums;
    }

    public void setCanTakeAlbums(Boolean canTakeAlbums) {
        this.canTakeAlbums = canTakeAlbums;
    }
}
