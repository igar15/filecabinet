package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Integer> {

    SecurityQuestion findByQuestionDefinitionIdAndUserIdAndAnswer(Integer questionDefinitionId, Integer userId, String answer);

}
