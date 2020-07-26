package com.igar15.filecabinet.dto;

import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.util.validation.DecNumValid;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class ChangeNoticeTo {

    private Integer id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Change code must not be empty")
    @Range(min = 1, max = 18, message = "Change code must be between 1 and 18")
    private Integer changeCode;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    private Developer developer;

    @DecNumValid()
    private String tempDocumentDecimalNumber;

    @NotEmpty
    private Set<Document> documents;




    public ChangeNoticeTo() {

    }

    public ChangeNoticeTo(Integer id, String name, Integer changeCode, LocalDate issueDate, Developer developer,
                         Set<Document> documents) {
        this.id = id;
        this.name = name;
        this.changeCode = changeCode;
        this.issueDate = issueDate;
        this.developer = developer;
        this.documents = documents;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChangeCode() {
        return changeCode;
    }

    public void setChangeCode(Integer changeCode) {
        this.changeCode = changeCode;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public String getTempDocumentDecimalNumber() {
        return tempDocumentDecimalNumber;
    }

    public void setTempDocumentDecimalNumber(String tempDocumentDecimalNumber) {
        this.tempDocumentDecimalNumber = tempDocumentDecimalNumber;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }


}
