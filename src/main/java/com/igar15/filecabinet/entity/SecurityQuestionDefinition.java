package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "security_question_definitions")
public class SecurityQuestionDefinition extends AbstractBaseEntity {

    @NotBlank
    @Column(name = "text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SecurityQuestionDefinition{" + "id=" + super.getId() + '\'' + ", text=" + text + '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SecurityQuestionDefinition other = (SecurityQuestionDefinition) obj;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

}
