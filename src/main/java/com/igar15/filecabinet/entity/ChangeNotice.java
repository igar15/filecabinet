package com.igar15.filecabinet.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="change_notices")
public class ChangeNotice extends AbstractNamedEntity {

    @Column(name = "change_code")
    private Integer changeCode;

    @NotNull
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private Developer developer;


    @ManyToMany()
    @JoinTable(name = "document_change_notices",
            joinColumns = @JoinColumn(name = "change_notice_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> documents;

    public ChangeNotice() {

    }

    public ChangeNotice(Integer id, String name, Integer changeCode, LocalDate issueDate, Developer developer, Set<Document> documents) {
        super(id, name);
        this.changeCode = changeCode;
        this.issueDate = issueDate;
        this.developer = developer;
        this.documents = documents;
    }

    public Integer getChangeCode() {
        return changeCode;
    }

    public void setChangeCode(Integer changeCode) {
        this.changeCode = changeCode;
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

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
