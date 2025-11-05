package com.A105.prham.notification.service;

import com.A105.prham.common.exception.CustomException;
import com.A105.prham.common.response.ErrorCode;
import com.A105.prham.keyword.Keyword;
import com.A105.prham.keyword.repository.KeywordRepository;
import com.A105.prham.notification.dto.request.KeywordCreateRequest;
import com.A105.prham.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public void addKeyword(@AuthenticationPrincipal User user, KeywordCreateRequest keywordCreateRequest) {
        Keyword keyword = Keyword.builder()
                .word(keywordCreateRequest.word())
                .user(user)
                .build();
        keywordRepository.save(keyword);
    }

    @Transactional
    public void deleteKeyword(@AuthenticationPrincipal User user, Long keywordId) {
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new CustomException(ErrorCode.KEYWORD_NOT_FOUND));
        keywordRepository.delete(keyword);
    }
}
