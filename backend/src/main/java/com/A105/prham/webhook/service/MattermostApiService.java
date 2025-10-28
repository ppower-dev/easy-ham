package com.A105.prham.webhook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class MattermostApiService {

	private final WebClient.Builder webClientBuilder;

	@Value("${mattermost.api.base-url:http://localhost:8065}")
	private String mattermostBaseUrl;

	@Value("${mattermost.api.token}")
	private String apiToken;

	//파일 정보 조회
	// GET /api/v1/posts/{post_id}/files/info
	public MattermostFileInfo getFileInfo(String fileId) {
		WebClient webClient = webClientBuilder
			.baseUrl(mattermostBaseUrl)
			.defaultHeader("Authorization", "Bearer " + apiToken)
			.build();

		MattermostFileInfoResponse response = webClient.get()
			.uri("/api/v4/files/{fileId}/info", fileId)
			.retrieve()
			.bodyToMono(MattermostFileInfoResponse.class)
			.block();

		String downloadUrl = mattermostBaseUrl + "/api/v4/files/" + fileId;

		return new MattermostFileInfo(
			response.id(),
			response.name(),
			downloadUrl,
			response.mime_type()
		);
	}

	//파일 다운로드 api
	public byte[] downloadFile(String fileId) {
		WebClient webClient = webClientBuilder
			.baseUrl(mattermostBaseUrl)
			.defaultHeader("Authorization", "Bearer " + apiToken)
			.build();

		Flux<DataBuffer> dataBufferFlux = webClient.get()
			.uri("/api/v4/files/{fileId}", fileId)
			.retrieve()
			.bodyToFlux(DataBuffer.class);

		byte[] fileBytes = dataBufferFlux
			.reduce(DataBuffer::write)
			.map(dataBuffer -> {
				byte[] bytes = new byte[dataBuffer.readableByteCount()];
				dataBuffer.read(bytes);
				return bytes;
			})
			.block();

		return fileBytes;
	}

	// 파일을 로컬에 저장
	public Path downloadAndSaveFile(String fileId, String fileName) throws IOException{
		byte[] fileBytes = downloadFile(fileId);

		//저장 경로: /uploads
		Path uploadDir = Paths.get("uploads");
		if(!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}

		Path filePath = uploadDir.resolve(fileName);
		Files.write(filePath, fileBytes);

		return filePath;
	}

	private record MattermostFileInfoResponse(
		String id,
		String name,
		String extension,
		String mime_type
	) {}

	private record MattermostFileInfo(
		String id,
		String name,
		String downloadUrl,
		String mimeType
	) {}
}
