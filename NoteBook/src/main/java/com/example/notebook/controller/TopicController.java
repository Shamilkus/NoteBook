package com.example.notebook.controller;

import com.example.notebook.exception.ResourceNotFoundException;
import com.example.notebook.models.Topic;
import com.example.notebook.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class TopicController {

	@Autowired
	private TopicRepository topicRepository;

	@GetMapping("/topics")
	public Page<Topic> getTopics(Pageable pageable) {
		return topicRepository.findAll(pageable);
	}


	@PostMapping("/topics")
	public Topic createTopic(@Valid @RequestBody Topic topic) {
		return topicRepository.save(topic);
	}

	@PutMapping("/topics/{topicId}")
	public Topic updateTopic(@PathVariable Long topicId,
	                               @Valid @RequestBody Topic topicRequest) {
		return topicRepository.findById(topicId)
				.map(topic -> {
					topic.setTitle(topicRequest.getTitle());
					topic.setDescription(topicRequest.getDescription());
					return topicRepository.save(topic);
				}).orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + topicId));
	}


	@DeleteMapping("/topics/{topicId}")
	public ResponseEntity<?> deleteTopic(@PathVariable Long topicId) {
		return topicRepository.findById(topicId)
				.map(topic -> {
					topicRepository.delete(topic);
					return ResponseEntity.ok().build();
				}).orElseThrow(() -> new ResourceNotFoundException("Topics not found with id " + topicId));
	}
}