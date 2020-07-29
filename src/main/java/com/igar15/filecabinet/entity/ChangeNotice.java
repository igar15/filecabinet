package com.igar15.filecabinet.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Entity
@Table(name="change_notices")
public class ChangeNotice extends AbstractNamedEntity {

    @Column(name = "change_code")
    @NotNull
    private Integer changeCode;

    @NotNull
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private Developer developer;

    @ElementCollection
    @CollectionTable(name = "document_change_notices", joinColumns = @JoinColumn(name = "change_notice_id"))
    @MapKeyJoinColumn(name = "document_id")
    @Column(name = "change")
    private Map<Document, Integer> documents;

    public ChangeNotice() {

    }

    public ChangeNotice(Integer id, String name, Integer changeCode, LocalDate issueDate, Developer developer) {
        super(id, name);
        this.changeCode = changeCode;
        this.issueDate = issueDate;
        this.developer = developer;
    }

    public ChangeNotice(Integer id, String name, Integer changeCode, LocalDate issueDate, Developer developer, Map<Document, Integer> documents) {
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

    public Map<Document, Integer> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<Document, Integer> documents) {
        this.documents = documents;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
