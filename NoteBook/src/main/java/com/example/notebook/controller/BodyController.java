package com.example.notebook.controller;

import com.example.notebook.exception.ResourceNotFoundException;
import com.example.notebook.models.Body;
import com.example.notebook.repository.BodyRepository;
import com.example.notebook.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class BodyController {

	@Autowired
	private BodyRepository bodyRepository;

	@Autowired
	private TopicRepository topicRepository;

	@GetMapping("/topics/{topicId}/bodies")
	public List<Body> getBodiesByTopicId(@PathVariable Long topicId) {
		return bodyRepository.findByTopicId(topicId);
	}

	@PostMapping("/topics/{topicId}/bodies")
	public Body addBody(@PathVariable Long topicId,
	                        @Valid @RequestBody Body body) {
		return topicRepository.findById(topicId)
				.map(topic -> {
					body.setTopic(topic);
					return bodyRepository.save(body);
				}).orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + topicId));
	}

	@PutMapping("/topics/{topicId}/bodies{bodyId}")
	public Body updateBody(@PathVariable Long topicId,
	                       @PathVariable Long bodyId,
	                       @Valid @RequestBody Body bodyRequest) {
		if(!topicRepository.existsById(topicId)) {
			throw new ResourceNotFoundException("Topic not found with id " + topicId);
		}

		return bodyRepository.findById(bodyId)
				.map(body -> {
					body.setText(bodyRequest.getText());
					return bodyRepository.save(body);
				}).orElseThrow(() -> new ResourceNotFoundException("Body not found with id " + bodyId));
	}

	@DeleteMapping("/topics/{topicId}/bodies{bodyId}")
	public ResponseEntity<?> deleteBody(@PathVariable Long topicId,
	                                    @PathVariable Long bodyId) {
		if(!topicRepository.existsById(topicId)) {
			throw new ResourceNotFoundException("Topic not found with id " + topicId);
		}

		return bodyRepository.findById(bodyId)
				.map(body -> {
					bodyRepository.delete(body);
					return ResponseEntity.ok().build();
				}).orElseThrow(() -> new ResourceNotFoundException("Body not found with id " + bodyId));

	}
}