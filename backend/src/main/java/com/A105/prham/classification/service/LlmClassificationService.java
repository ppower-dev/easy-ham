// package com.A105.prham.classification.service;
//
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
//
// import com.A105.prham.classification.dto.LlmClassificationResult;
// import com.A105.prham.common.code.dto.reponse.MaincodeResponseDto;
// import com.A105.prham.common.code.service.CodeService;
// import com.A105.prham.webhook.entity.Post;
// import com.fasterxml.jackson.databind.ObjectMapper;
//
// import lombok.RequiredArgsConstructor;
// import lombok.Value;
// import lombok.extern.slf4j.Slf4j;
//
// @Service
// @Slf4j
// @RequiredArgsConstructor
// public class LlmClassificationService {
// 	private final CodeService codeService;
// 	private final RestTemplate restTemplate = new RestTemplate();
// 	private final ObjectMapper objectMapper = new ObjectMapper();
//
// 	@Value("${llm.api.key}")
// 	private String llmApiKey;
//
// 	@Value("${llm.api.url")
// 	private String llmApiUrl;
//
// 	public LlmClassificationResult classify(Post post){
// 		List<MaincodeResponseDto>
// 	}
//
//
//
// }
