package com.A105.prham.user_notice.repository;

import com.A105.prham.user_notice.entity.UserNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNoticeRepository extends JpaRepository<UserNotice,Long> {
}
