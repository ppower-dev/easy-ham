package com.A105.prham.user_notice_like.service;

import com.A105.prham.common.exception.CustomException;
import com.A105.prham.common.response.ErrorCode;
import com.A105.prham.notice.entity.Notice;
import com.A105.prham.notice.repository.NoticeRepository;
import com.A105.prham.user.entity.User;
import com.A105.prham.user.repository.UserRepository;
import com.A105.prham.user_notice.entity.UserNotice;
import com.A105.prham.user_notice.repository.UserNoticeRepository;
import com.A105.prham.user_notice_like.entity.UserNoticeLike;
import com.A105.prham.user_notice_like.dto.UserNoticeLikeCreateResponse;
import com.A105.prham.user_notice_like.dto.UserNoticeLikeDeleteResponse;
import com.A105.prham.user_notice_like.repository.UserNoticeLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserNoticeLikeService {

    private final UserNoticeLikeRepository userNoticeLikeRepository;
    private final UserRepository userRepository;
    private final UserNoticeRepository userNoticeRepository;

    @Transactional
    public UserNoticeLikeCreateResponse saveBookmarks(Long userId, Long userNoticeId){

        // 유저 유효성 검사
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 공지사항 유효성 검사
        UserNotice userNotice =userNoticeRepository.findById(userNoticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));

        // 해당 유저와 게시물에 대한 북마크가 이미 있는 경우 예외 처리
        if(userNoticeLikeRepository.existsByUserAndUserNotice(user, userNotice)){
            throw new CustomException(ErrorCode.DUPLICATED_USER_NOTICE_LIKE);
        }

        // 북마크 객체 생성 후 저장
        UserNoticeLike userNoticeLike = UserNoticeLike.builder()
                .user(user)
                .notice(userNotice.getNotice())
                .team(userNotice.getTeam())
                .subcode(userNotice.getSubcode())
                .maincode(userNotice.getMaincode())
                .channel(userNotice.getChannel())
                .post(userNotice.getPost())
                .isLiked(true)
                .build();

        userNoticeLikeRepository.save(userNoticeLike);

        //결과 반환
        return UserNoticeLikeCreateResponse.builder()
                .userNoticeId(userNoticeId)
                .isLiked(true)
                .build();
    }

    @Transactional
    public UserNoticeLikeDeleteResponse deleteBookmarks(Long userId, Long userNoticeId){

        // 유저 유효성 검사
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 공지 유효성 검사
        UserNotice userNotice =userNoticeRepository.findById(userNoticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));

        // 저장되어 있는 북마크가 맞는지 검사
        if(!userNoticeLikeRepository.existsByUserAndUserNotice(user, userNotice)){
            throw new CustomException(ErrorCode.INVALID_USER_NOTICE_LIKE);
        }

        // 해당 북마크 찾은 후 삭제
        UserNoticeLike userNoticeLike = userNoticeLikeRepository.findByUserAndUserNotice(user,userNotice);
        userNoticeLikeRepository.delete(userNoticeLike);

        return UserNoticeLikeDeleteResponse.builder()
                .isLiked(false)
                .build();
    }
}
