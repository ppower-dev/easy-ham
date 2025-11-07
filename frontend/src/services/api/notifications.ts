/**
 * 알림 설정 관련 API
 */

import { apiClient } from './client';
import { API_ENDPOINTS } from '@/constants/api';
import type { ApiResponse, NotificationSettings, NotificationSettingsResponse } from '@/types/api';

// ===== 타입 정의 =====

export interface InitializeNotificationSettingsResponse {
  status: number;
  message: string;
}

// ===== API 함수 =====

/**
 * 알림 설정 초기화
 * 회원가입 완료 후 사용자의 알림 설정을 초기화하는 API
 * @returns 성공 메시지
 */
export const initializeNotificationSettings = async (): Promise<ApiResponse<InitializeNotificationSettingsResponse>> => {
  return apiClient.post<InitializeNotificationSettingsResponse>(
    API_ENDPOINTS.notifications.initializeSettings,
    {}
  );
};

/**
 * 알림 설정 조회
 * 사용자의 현재 알림 설정을 조회하는 API
 * @returns 알림 설정 데이터 (deadlineAlertHours, jobAlertEnabled, keywordAlertEnabled)
 */
export const getNotificationSettings = async (): Promise<ApiResponse<NotificationSettingsResponse>> => {
  return apiClient.get<NotificationSettingsResponse>(
    API_ENDPOINTS.notifications.getSettings
  );
};

/**
 * 알림 설정 수정
 * 사용자의 알림 설정을 수정하는 API
 * @param settings - 변경할 알림 설정 (deadlineAlertHours, jobAlertEnabled, keywordAlertEnabled)
 * @returns 성공 메시지
 */
export const updateNotificationSettings = async (
  settings: NotificationSettings
): Promise<ApiResponse<{ message: string }>> => {
  return apiClient.patch<{ message: string }>(
    API_ENDPOINTS.notifications.updateSettings,
    settings
  );
};
