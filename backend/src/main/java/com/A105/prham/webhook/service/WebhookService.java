package com.A105.prham.webhook.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.A105.prham.webhook.entity.File;
import com.A105.prham.webhook.entity.Post;
import com.A105.prham.webhook.repository.FileRepository;
import com.A105.prham.webhook.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebhookService {

	private final PostRepository postRepository;
	private final FileRepository fileRepository;


}