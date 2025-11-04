package com.A105.prham.user_notice_like.repository;

import com.A105.prham.notice.entity.Notice;
import com.A105.prham.user.entity.User;
import com.A105.prham.user_notice.entity.UserNotice;
import com.A105.prham.user_notice_like.entity.UserNoticeLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserNoticeLikeRepository extends JpaRepository<UserNoticeLike, Long> {
    boolean existsByUserAndUserNotice(User user, UserNotice userNotice);

    UserNoticeLike findByUserAndUserNotice(User user, UserNotice userNotice);

    List<UserNoticeLike> findByUser(User user);
}
