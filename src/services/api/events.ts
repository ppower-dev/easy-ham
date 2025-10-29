/**
 * 캘린더 이벤트 API 서비스
 * 현재는 Mock 데이터 반환, 추후 실제 API로 대체
 */

import type { CalendarEvent } from '@/types/event';
import { getMockEvents } from '@/services/mock/mockEvents';

export const eventsApi = {
  /**
   * 모든 이벤트 가져오기
   */
  getAll: async (): Promise<CalendarEvent[]> => {
    // TODO: 실제 API 호출로 대체
    // return apiClient.get<CalendarEvent[]>('/events').then(res => res.data);
    return Promise.resolve(getMockEvents());
  },

  /**
   * 특정 날짜 범위의 이벤트 가져오기
   */
  getByDateRange: async (
    startDate: Date,
    endDate: Date
  ): Promise<CalendarEvent[]> => {
    // TODO: 실제 API 호출로 대체
    // return apiClient.get<CalendarEvent[]>(`/events?start=${startDate.toISOString()}&end=${endDate.toISOString()}`).then(res => res.data);
    const events = getMockEvents();
    return Promise.resolve(
      events.filter(
        (event) => event.startDate >= startDate && event.endDate <= endDate
      )
    );
  },

  /**
   * 특정 이벤트 가져오기
   */
  getById: async (id: number): Promise<CalendarEvent | null> => {
    // TODO: 실제 API 호출로 대체
    // return apiClient.get<CalendarEvent>(`/events/${id}`).then(res => res.data);
    const events = getMockEvents();
    return Promise.resolve(events.find((e) => e.id === id) || null);
  },
};
