package com.A105.prham.webhook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.A105.prham.common.response.ApiResponseDto;
import com.A105.prham.common.response.SuccessCode;
import com.A105.prham.webhook.dto.MattermostWebhookDto;
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
	public ResponseEntity<ApiResponseDto<Void>> handleMattermostWebhook(
		@RequestBody MattermostWebhookDto payload
	) {
		webhookService.processWebhook(payload);

		return ResponseEntity.ok(ApiResponseDto.<Void>success(SuccessCode.WEBHOOK_RECEIVED));
	}

}
