package com.example.notebook.repository;

import com.example.notebook.models.Body;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BodyRepository extends JpaRepository<Body, Long> {
	List<Body> findByTopicId(Long topicId);
}
