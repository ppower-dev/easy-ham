package com.A105.prham.user_notice_like.repository;

import com.A105.prham.notice.entity.Notice;
import com.A105.prham.user.domain.User;
import com.A105.prham.user_notice_like.domain.UserNoticeLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNoticeLikeRepository extends JpaRepository<UserNoticeLike, Long> {
    boolean existsByUserAndNotice(User user, Notice notice);
}
