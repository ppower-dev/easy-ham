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
} as const;
