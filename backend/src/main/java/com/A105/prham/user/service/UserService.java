package com.A105.prham.user.service;

import com.A105.prham.campus.entity.Campus;
import com.A105.prham.campus.repository.CampusRepository;
import com.A105.prham.position.entity.Position;
import com.A105.prham.position.repository.PositionRepository;
import com.A105.prham.skill.entity.Skill;
import com.A105.prham.skill.repository.SkillRepository;
import com.A105.prham.user.dto.request.UserSignupRequest;
import com.A105.prham.user.dto.request.UserUpdateRequest;
import com.A105.prham.user.entity.User;
import com.A105.prham.user.entity.UserPosition;
import com.A105.prham.user.entity.UserSkill;
import com.A105.prham.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CampusRepository campusRepository;
    private final SkillRepository skillRepository;
    private final PositionRepository positionRepository;

    public User createUser(UserSignupRequest request, String ssoSubId) {

        Campus campus = campusRepository.findById(request.getCampusId())
                .orElseThrow(() -> new EntityNotFoundException("캠퍼스 정보를 찾을 수 없습니다."));

        if (ssoSubId == null) throw new IllegalStateException("헤더가 없습니다.");
        userRepository.findBySsoSubId(ssoSubId)
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 등록된 사용자입니다.");
                });

        User user = User.builder()
                .name(request.getName())
                .classroom(request.getClassroom())
                .email(request.getEmail())
                .campus(campus)
                .generation(request.getGeneration())
                .ssoSubId(ssoSubId)
                .build();

        if (request.getSkillIds() != null && !request.getSkillIds().isEmpty()) {
            List<Skill> skills = skillRepository.findAllById(request.getSkillIds());
            for (Skill skill : skills) {
                user.getUserSkills().add(UserSkill.builder().user(user).skill(skill).build());
            }
        }

        if (request.getPositionIds() != null && !request.getPositionIds().isEmpty()) {
            List<Position> positions = positionRepository.findAllById(request.getPositionIds());
            for (Position position : positions) {
                user.getUserPositions().add(UserPosition.builder().user(user).position(position).build());
            }
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public User findBySsoSubId(String ssoSubId) {
        return userRepository.findBySsoSubId(ssoSubId)
                .orElseThrow(() -> new EntityNotFoundException("해당 SSO UUID로 사용자를 찾을 수 없습니다."));
    }

    /**
     *  회원 정보 수정
     */
    public User updateUserInfo(String ssoSubId, UserUpdateRequest request) {
        User user = userRepository.findBySsoSubId(ssoSubId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getClassroom() != null) user.setClassroom(request.getClassroom());
        if (request.getGeneration() != null) user.setGeneration(request.getGeneration());
        if (request.getProfileImage() != null) user.setProfileImage(request.getProfileImage());

        if (request.getCampusId() != null) {
            Campus campus = campusRepository.findById(request.getCampusId())
                    .orElseThrow(() -> new EntityNotFoundException("캠퍼스를 찾을 수 없습니다."));
            user.setCampus(campus);
        }

        //  기존 스킬/포지션 초기화 후 재등록
        if (request.getSkillIds() != null) {
            user.getUserSkills().clear();
            List<Skill> skills = skillRepository.findAllById(request.getSkillIds());
            for (Skill skill : skills) {
                user.getUserSkills().add(UserSkill.builder().user(user).skill(skill).build());
            }
        }

        if (request.getPositionIds() != null) {
            user.getUserPositions().clear();
            List<Position> positions = positionRepository.findAllById(request.getPositionIds());
            for (Position position : positions) {
                user.getUserPositions().add(UserPosition.builder().user(user).position(position).build());
            }
        }

        return userRepository.save(user);
    }
}
