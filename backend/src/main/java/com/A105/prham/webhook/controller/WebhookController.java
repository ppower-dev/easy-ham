package com.A105.prham.webhook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.A105.prham.webhook.service.WebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {

	private final WebhookService webhookService;

	/**
	 * Mattermost Outgoing Webhook 수신 API
	 * [POST /api/v1/webhooks/messages]
	 */
	@PostMapping("/messages")
	public ResponseEntity<Void> handleMattermostWebhook(@RequestBody MattermostPayloadDto payload) {

		// (디버깅) 수신된 페이로드 로깅

		// 1. 서비스 로직을 비동기로 호출합니다. (기다리지 않음)
		// (null 체크 등 간단한 유효성 검사 후 호출)
		if (payload == null || payload.postId() == null) {
			// 비어있어도 200 OK를 보내야 MM이 재시도를 안 합니다.
			return ResponseEntity.ok().build();
		}

		webhookService.processMessageAsync(payload);

		// 2. Mattermost에 즉시 200 OK 응답을 반환합니다.
		// (이 응답이 늦어지면 MM은 웹훅 실패로 간주합니다)
		return ResponseEntity.ok().build();
	}

}
