package com.A105.prham.user_notice_like.service;

import com.A105.prham.auth.util.JwtUtils;
import com.A105.prham.common.exception.CustomException;
import com.A105.prham.common.response.ErrorCode;
import com.A105.prham.notice.entity.Notice;
import com.A105.prham.notice.repository.NoticeRepository;
import com.A105.prham.user.domain.User;
import com.A105.prham.user.repository.UserRepository;
import com.A105.prham.user_notice_like.domain.UserNoticeLike;
import com.A105.prham.user_notice_like.dto.UserNoticeLikeCreateResponse;
import com.A105.prham.user_notice_like.repository.UserNoticeLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNoticeLikeService {

    private final UserNoticeLikeRepository userNoticeLikeRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;

    public UserNoticeLikeCreateResponse saveBookmarks(Long userId, Long noticeId){

        // 유저 유효성 검사
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 공지사항 유효성 검사
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));

        // 해당 유저와 게시물에 대한 북마크가 이미 있는 경우 예외 처리
        if(userNoticeLikeRepository.existsByUserAndNotice(user, notice)){
            throw new CustomException(ErrorCode.DUPLICATED_USER_NOTICE_LIKE);
        }

        // 북마크 객체 생성 후 저장
        UserNoticeLike userNoticeLike = UserNoticeLike.builder()
                .user(user)
                .notice(notice)
                .team(notice.getTeam())
                .code(notice.getSubcode())
                .maincode(notice.getMaincode())
                .channel(notice.getChannel())
                .post(notice.getPost())
                .isLiked(true)
                .build();

        userNoticeLikeRepository.save(userNoticeLike);

        //결과 반환
        return UserNoticeLikeCreateResponse.builder()
                .noticeId(noticeId)
                .isLiked(true)
                .build();
    }
}
