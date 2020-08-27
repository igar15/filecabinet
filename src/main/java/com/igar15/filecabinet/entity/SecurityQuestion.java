package com.igar15.filecabinet.entity;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "security_questions")
public class SecurityQuestion extends AbstractBaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "question_definition_id")
    private SecurityQuestionDefinition questionDefinition;

    @Column(name = "answer")
    private String answer;

    public SecurityQuestion() {
        super();
    }

    public SecurityQuestion(User user, SecurityQuestionDefinition questionDefinition, String answer) {
        super();
        this.user = user;
        this.questionDefinition = questionDefinition;
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public SecurityQuestionDefinition getQuestionDefinition() {
        return questionDefinition;
    }

    public void setQuestionDefinition(final SecurityQuestionDefinition questionDefinition) {
        this.questionDefinition = questionDefinition;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "SecurityQuestionDefinition{" + "id=" + super.getId() + '\'' + "user=" + user.getId() + '\'' + "questionDefinition=" + questionDefinition.getId() + '\'' + ", answer=" + answer + '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((answer == null) ? 0 : answer.hashCode());
        result = prime * result + ((questionDefinition == null) ? 0 : questionDefinition.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        SecurityQuestion other = (SecurityQuestion) obj;
        if (answer == null) {
            if (other.answer != null)
                return false;
        } else if (!answer.equals(other.answer))
            return false;
        if (questionDefinition == null) {
            if (other.questionDefinition != null)
                return false;
        } else if (!questionDefinition.equals(other.questionDefinition))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }


}
