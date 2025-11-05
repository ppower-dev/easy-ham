/**
 * API Endpoints
 * Centralized endpoint management for all API calls
 */

export const API_ENDPOINTS = {
  // Auth endpoints
  auth: {
    getSsoLoginUrl: "/auth/sso/login-url",
    signup: "/auth/signup",
    logout: "/auth/logout",
    ssoCallback: "/auth/sso/callback",
  },

  // Code lookups (campus, skill, position)
  codes: {
    campuses: "/campus",
    skills: "/skill",
    positions: "/position",
  },

  // 공지사항 엔드포인트
  notices: {
    list: "/v1/notices",
    detail: (id: number) => `/v1/notices/${id}`,
  },

  // 북마크 엔드포인트
  bookmarks: {
    list: "/v1/bookmarks",
    add: (userNoticeId: number) => `/v1/bookmarks/${userNoticeId}`,
    remove: (userNoticeId: number) => `/v1/bookmarks/${userNoticeId}`,
  },
} as const;
