package com.A105.prham.webhook.service;

import com.A105.prham.webhook.dto.MattermostFileInfoDto;
import com.A105.prham.webhook.entity.File;
import com.A105.prham.webhook.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MattermostFileService {

	private final FileRepository fileRepository; // File 엔티티 저장을 위해
	private final RestTemplate restTemplate = new RestTemplate();

	// nohup 명령어에서 주입한 환경 변수를 사용합니다.
	@Value("${mattermost.api.base-url}")
	private String mattermostApiBaseUrl;

	@Value("${mattermost.api.token}")
	private String mattermostApiToken;

	// 파일을 저장할 EC2 내부 경로 (S3를 사용하기 전 임시 경로)
	private final String localFileStoragePath = "/home/ubuntu/files/";

	// 이 메서드가 핵심입니다.
	public File downloadAndSaveFile(String fileId) {
		try {
			// 1. Mattermost API 헤더 준비 (봇 토큰)
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(mattermostApiToken);
			HttpEntity<String> entity = new HttpEntity<>(headers);

			// 2. 파일 메타데이터(정보) 먼저 가져오기 (파일명, 타입 등)
			String fileInfoUrl = mattermostApiBaseUrl + "/api/v4/files/" + fileId + "/info";
			MattermostFileInfoDto fileInfo = restTemplate.exchange(
				fileInfoUrl, HttpMethod.GET, entity, MattermostFileInfoDto.class
			).getBody();

			if (fileInfo == null) {
				log.error("Failed to get file info for ID: {}", fileId);
				return null;
			}

			// 3. 실제 파일 바이너리(데이터) 가져오기
			String fileUrl = mattermostApiBaseUrl + "/api/v4/files/" + fileId;
			ResponseEntity<byte[]> response = restTemplate.exchange(
				fileUrl, HttpMethod.GET, entity, byte[].class
			);
			byte[] fileData = response.getBody();

			if (fileData == null) {
				log.error("Failed to download file data for ID: {}", fileId);
				return null;
			}

			// 4. 로컬 스토리지에 파일 저장 (S3로 변경하기 전 임시 방식)
			// (파일 이름 중복을 피하기 위해 UUID + 원본 파일명 사용)
			String newFileName = UUID.randomUUID().toString() + "_" + fileInfo.getName();
			Path targetPath = Paths.get(localFileStoragePath, newFileName);

			// 디렉터리가 없으면 생성
			Files.createDirectories(targetPath.getParent());
			Files.write(targetPath, fileData);

			// 5. File 엔티티 생성 (DB에 저장할 정보)
			File fileEntity = File.builder()
				.mmFileId(fileId)
				.fileName(fileInfo.getName()) // 원본 파일명
				.mimeType(fileInfo.getMimeType())
				.fileUrl(targetPath.toString()) // EC2 내부 경로 (나중에 S3 URL로 대체)
				.build();

			log.info("File saved to local storage: {}", targetPath);
			return fileEntity; // 이 엔티티는 Post에 연결되어 저장될 것임

		} catch (Exception e) {
			log.error("Error processing file ID {}: {}", fileId, e.getMessage(), e);
			return null;
		}
	}
}