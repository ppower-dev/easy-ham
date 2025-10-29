package com.A105.prham.user.service;

import com.A105.prham.campus.entity.Campus;
import com.A105.prham.campus.repository.CampusRepository;
import com.A105.prham.position.entity.Position;
import com.A105.prham.position.repository.PositionRepository;
import com.A105.prham.skill.entity.Skill;
import com.A105.prham.skill.repository.SkillRepository;
import com.A105.prham.user.dto.request.UserSignupRequest;
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

    /**
     * 신규 사용자 생성 (회원가입)
     * - 기본 정보 + 선택적 skills/positions 포함
     */
    public User createUser(UserSignupRequest request) {

        Campus campus = campusRepository.findById(request.getCampusId())
                .orElseThrow(() -> new EntityNotFoundException("캠퍼스 정보를 찾을 수 없습니다."));

        // 유저 생성
        User user = User.builder()
                .name(request.getName())
                .classroom(request.getClassroom())
                .email(request.getEmail())
                .campus(campus)
                .generation(request.getGeneration())
                .build();

        // 선택적 스킬 연결
        if (request.getSkillIds() != null && !request.getSkillIds().isEmpty()) {
            List<Skill> skills = skillRepository.findAllById(request.getSkillIds());
            for (Skill skill : skills) {
                user.getUserSkills().add(
                        UserSkill.builder()
                                .user(user)
                                .skill(skill)
                                .build()
                );
            }
        }

        // 선택적 포지션 연결
        if (request.getPositionIds() != null && !request.getPositionIds().isEmpty()) {
            List<Position> positions = positionRepository.findAllById(request.getPositionIds());
            for (Position position : positions) {
                user.getUserPositions().add(
                        UserPosition.builder()
                                .user(user)
                                .position(position)
                                .build()
                );
            }
        }

        return userRepository.save(user);
    }

    /**
     * 이메일로 사용자 조회
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }






}
