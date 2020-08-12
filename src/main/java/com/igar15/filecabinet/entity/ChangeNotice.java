package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractNamedEntity;
import com.igar15.filecabinet.util.validation.ChangeNoticeNameNotDuplicate;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

@ChangeNoticeNameNotDuplicate
@Entity
@Table(name="change_notices")
public class ChangeNotice extends AbstractNamedEntity {

    @NotNull(message = "Change code must not be empty")
    @Range(min = 1, max = 18, message = "Change code must be between 1 and 18")
    @Column(name = "change_code")
    private Integer changeCode;

    @NotNull(message = "Issue date must not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @NotNull(message = "Developer must not be empty")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "document_change_notices", joinColumns = @JoinColumn(name = "change_notice_id"))
    @MapKeyJoinColumn(name = "document_id")
    @Column(name = "change")
    private Map<Document, Integer> documents;

    public ChangeNotice() {
    }

    public ChangeNotice(Integer id, String name, Integer changeCode, LocalDate issueDate, Department department) {
        super(id, name);
        this.changeCode = changeCode;
        this.issueDate = issueDate;
        this.department = department;
    }

    public ChangeNotice(Integer id, String name, Integer changeCode, LocalDate issueDate, Department department, Map<Document, Integer> documents) {
        super(id, name);
        this.changeCode = changeCode;
        this.issueDate = issueDate;
        this.department = department;
        this.documents = documents;
    }


    public Integer getChangeCode() {
        return changeCode;
    }

    public void setChangeCode(Integer changeCode) {
        this.changeCode = changeCode;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

}
