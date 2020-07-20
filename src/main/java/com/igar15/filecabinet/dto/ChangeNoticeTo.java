package com.igar15.filecabinet.dto;

import com.igar15.filecabinet.util.DecNumValid;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ChangeNoticeTo {

    private Integer id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Change code must not be empty")
    @Range(min = 1, max = 18, message = "Change code must be between 1 and 18")
    private Integer changeCode;

    private String developerName;

    @DecNumValid()
    private String tempDocumentDecimalNumber;

    @NotEmpty
    private List<String> documentDecimalNumbers = new ArrayList<>();

    private String[] developerNames;

    public ChangeNoticeTo() {
    }

    public ChangeNoticeTo (String[] developerNames) {
        this.developerNames = developerNames;
    }

    public ChangeNoticeTo(Integer id, String name, Integer changeCode, String developerName, List<String> documentDecimalNumbers, String[] developerNames) {
        this.id = id;
        this.name = name;
        this.changeCode = changeCode;
        this.developerName = developerName;
        this.documentDecimalNumbers = documentDecimalNumbers;
        this.developerNames = developerNames;
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

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getTempDocumentDecimalNumber() {
        return tempDocumentDecimalNumber;
    }

    public void setTempDocumentDecimalNumber(String tempDocumentDecimalNumber) {
        this.tempDocumentDecimalNumber = tempDocumentDecimalNumber;
    }

    public List<String> getDocumentDecimalNumbers() {
        return documentDecimalNumbers;
    }

    public void setDocumentDecimalNumbers(List<String> documentDecimalNumbers) {
        this.documentDecimalNumbers = documentDecimalNumbers;
    }

    public String[] getDeveloperNames() {
        return developerNames;
    }

    public void setDeveloperNames(String[] developerNames) {
        this.developerNames = developerNames;
    }
}
