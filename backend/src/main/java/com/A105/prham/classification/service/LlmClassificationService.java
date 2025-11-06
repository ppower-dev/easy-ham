package com.A105.prham.classification.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.A105.prham.campus.service.CampusService;
import com.A105.prham.classification.dto.ChatMessage;
import com.A105.prham.classification.dto.LlmClassificationResult;
import com.A105.prham.classification.dto.OpenAiChatRequest;
import com.A105.prham.classification.dto.OpenAiChatResponse;
import com.A105.prham.classification.dto.ResponseFormat;
import com.A105.prham.common.code.dto.reponse.MaincodeResponseDto;
import com.A105.prham.common.code.dto.reponse.SubcodeResponseDto;
import com.A105.prham.common.code.service.CodeService;
import com.A105.prham.webhook.entity.Post;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LlmClassificationService {
	private final CodeService codeService;
	private final CampusService campusService;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${llm.api.key}")
	private String llmApiKey;

	@Value("${llm.api.url}")
	private String llmApiUrl;

	public LlmClassificationResult classify(Post post){
		// db에서 카테고리 목록 가져오기
		List<MaincodeResponseDto> mainCodes = codeService.getAllMaincodes();
		String categoryListString = formatCategoriesForPrompt(mainCodes);

		List<String> campusNames = campusService.getAllCampusNames();
		String campusListString = String.join(",", campusNames);

		// 프롬프트 생성
		String prompt = createFullPrompt(post, categoryListString, campusListString);

		// llm 호출
		OpenAiChatRequest request = OpenAiChatRequest.builder()
			.model("gpt-4o")
			.messages(List.of(new ChatMessage("user", prompt)))
			.responseFormat(new ResponseFormat("json_object"))
			.build();

		// http header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(llmApiKey);

		HttpEntity<OpenAiChatRequest> entity = new  HttpEntity<>(request, headers);


		try {
			log.info("OpenAi API 호출 시작");

			OpenAiChatResponse response = restTemplate.postForObject(llmApiUrl, entity, OpenAiChatResponse.class);

			if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
				throw new RuntimeException("llm 응답이 비었음");
			}

			// llm이 반환한 json 문자열 추출
			String llmResponseJson = response.getChoices().get(0).getMessage().getContent();
			log.info("LLM 응답 json 수신: {}", llmResponseJson);

			return objectMapper.readValue(llmResponseJson, LlmClassificationResult.class);

		} catch (Exception e) {
			log.error("llm 응답 json 파싱 실패: {}", e.getMessage(), e);
			return null;
		}
	}

	private String formatCategoriesForPrompt(List<MaincodeResponseDto> mainCodes) {
		return mainCodes.stream()
			.map(main -> "-" + main.getMainCodeName() + ": " +
					main.getSubcodes().stream()
						.map(SubcodeResponseDto::getSubcodeName)
						.collect(Collectors.joining(", ")))
			.collect(Collectors.joining("\n"));
	}

	private String createFullPrompt(Post post, String categoryListString, String campusListString) {

		return """
			당신은 삼성 청년 SW AI 아카데미(SSAFY) 공지사항 분석 AI입니다.
			주어진 [채널명]과 [메시지]를 [분류 규칙]에 따라 분석하여, [출력 형식]에 맞는 JSON 객체만 반환하세요.
			
			[채널명]: %s
			
			[메시지]:
			%s
			
			[카테고리 목록]:
			%s
			
			[캠퍼스 목록]:
			%s
			
			[분류 규칙]:
			1. 채널 힌트: [채널명]에 "[취업]"이 있으면 '취업'으로, "공지사항"이 있으면 '학사'로 우선 판단합니다 (가중치 70%%).
			2. 의무/필수 액션: "제출", "설문" 등 *필수* 작업이 명시되면, 내용이 '특강'이라도 subCategory를 '할일'로 분류합니다. (예: "특강 퇴실설문" -> "취업-할일")
			3. 선택적 액션: "신청", "참여" 등 *선택* 작업은 원래 카테고리('특강', '이벤트')를 유지합니다.
			4. 캠퍼스 추출: *오직* mainCategory가 "취업"이고 subCategory가 "특강"일 *경우에만*, [캠퍼스 목록]에서 해당 캠퍼스를 리스트로 추출합니다. 그 외 모든 경우(전국, 전 캠퍼스, 학사 공지 등)는 null로 설정합니다.
			5. 제목: [메시지]의 핵심 내용을 15단어 이내로 요약합니다.
			6. 마감일: "YYYY-MM-DDTHH:MM:SS" 형식으로 추출합니다. 없으면 null입니다.
			
			[출력 형식 (JSON)]
			{
				"title": "string",
				"mainCategory": "string",
				"subCategory": "string",
				"deadline": "YYYY-MM-DDTHH:MM:SS" | null,
				"campusList": ["string"] | null
			}
			""".formatted(post.getChannelName(), post.getOriginalText(), categoryListString, campusListString);
	}

}

