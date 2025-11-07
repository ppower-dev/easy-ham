/**
 * 알림 설정 상태 관리 스토어
 * Zustand 기반 알림 설정 (deadline, job alert, keyword alert) 관리
 */

import { create } from 'zustand';
import { getNotificationSettings, updateNotificationSettings } from '@/services/api/notifications';
import type { NotificationSettings } from '@/types/api';

interface NotificationSettingsState extends NotificationSettings {
  isLoading: boolean;
  error: string | null;

  // 액션
  fetchSettings: () => Promise<void>;
  updateDeadlineAlertHours: (hours: number) => Promise<void>;
  updateJobAlertEnabled: (enabled: boolean) => Promise<void>;
  updateKeywordAlertEnabled: (enabled: boolean) => Promise<void>;
  clearError: () => void;
}

export const useNotificationSettingsStore = create<NotificationSettingsState>((set, get) => ({
  deadlineAlertHours: 6,
  jobAlertEnabled: true,
  keywordAlertEnabled: true,
  isLoading: false,
  error: null,

  /**
   * 알림 설정 조회
   * 서버에서 최신 알림 설정을 가져와 스토어에 저장
   */
  fetchSettings: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await getNotificationSettings();
      set({
        deadlineAlertHours: response.data.deadlineAlertHours,
        jobAlertEnabled: response.data.jobAlertEnabled,
        keywordAlertEnabled: response.data.keywordAlertEnabled,
        isLoading: false,
      });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '알림 설정 조회 실패';
      set({ error: errorMessage, isLoading: false });
      throw error;
    }
  },

  /**
   * 마감 기한 알림 시간 업데이트
   * @param hours 마감 기한 전 몇 시간에 알림을 받을지
   */
  updateDeadlineAlertHours: async (hours: number) => {
    const currentState = get();
    set({ isLoading: true, error: null });

    try {
      await updateNotificationSettings({
        deadlineAlertHours: hours,
        jobAlertEnabled: currentState.jobAlertEnabled,
        keywordAlertEnabled: currentState.keywordAlertEnabled,
      });

      set({
        deadlineAlertHours: hours,
        isLoading: false,
      });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '설정 저장 실패';
      set({ error: errorMessage, isLoading: false });
      throw error;
    }
  },

  /**
   * 채용정보 알림 활성화 상태 업데이트
   * @param enabled 채용정보 알림 활성화 여부
   */
  updateJobAlertEnabled: async (enabled: boolean) => {
    const currentState = get();
    set({ isLoading: true, error: null });

    try {
      await updateNotificationSettings({
        deadlineAlertHours: currentState.deadlineAlertHours,
        jobAlertEnabled: enabled,
        keywordAlertEnabled: currentState.keywordAlertEnabled,
      });

      set({
        jobAlertEnabled: enabled,
        isLoading: false,
      });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '설정 저장 실패';
      set({ error: errorMessage, isLoading: false });
      throw error;
    }
  },

  /**
   * 키워드 알림 활성화 상태 업데이트
   * @param enabled 키워드 알림 활성화 여부
   */
  updateKeywordAlertEnabled: async (enabled: boolean) => {
    const currentState = get();
    set({ isLoading: true, error: null });

    try {
      await updateNotificationSettings({
        deadlineAlertHours: currentState.deadlineAlertHours,
        jobAlertEnabled: currentState.jobAlertEnabled,
        keywordAlertEnabled: enabled,
      });

      set({
        keywordAlertEnabled: enabled,
        isLoading: false,
      });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '설정 저장 실패';
      set({ error: errorMessage, isLoading: false });
      throw error;
    }
  },

  /**
   * 에러 메시지 초기화
   */
  clearError: () => {
    set({ error: null });
  },
}));
