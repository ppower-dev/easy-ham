package com.A105.prham.notification.service;

import com.A105.prham.keyword.Keyword;
import com.A105.prham.keyword.repository.KeywordRepository;
import com.A105.prham.notification.dto.request.KeywordCreateRequest;
import com.A105.prham.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final KeywordRepository keywordRepository;

    public void addKeyword(@AuthenticationPrincipal User user, KeywordCreateRequest keywordCreateRequest) {
        Keyword keyword = Keyword.builder()
                .word(keywordCreateRequest.word())
                .user(user)
                .build();
        keywordRepository.save(keyword);
    }
}
