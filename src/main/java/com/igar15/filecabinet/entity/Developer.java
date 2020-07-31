package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractNamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @Column(name = "is_developer")
    private Boolean isDeveloper;

    @NotNull
    @Column(name = "can_take_albums")
    private Boolean canTakeAlbums;


    public Developer() {

    }

    public Developer(Integer id, String name, String chiefName, String description, Integer workersAmount, Boolean isDeveloper, Boolean canTakeAlbums) {
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
