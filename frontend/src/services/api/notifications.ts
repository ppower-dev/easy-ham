/**
 * 알림 설정 관련 API
 */

import { apiClient } from './client';
import { API_ENDPOINTS } from '@/constants/api';
import type { ApiResponse } from '@/types/common';

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
