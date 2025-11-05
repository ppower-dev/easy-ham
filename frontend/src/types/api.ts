/**
 * API 요청/응답 관련 타입 정의
 */

import type { Category, Subcategory } from "./notice";

/**
 * API 공통 응답 형식
 */
export interface ApiResponse<T> {
  status: number;
  message: string;
  data: T;
}

/**
 * 백엔드 공지사항 API 응답 (Notice 목록 조회)
 */
export interface NoticeApiResponse {
  noticeId: number;
  title: string;
  contentPreview: string;
  mainCategory: string; // "공지사항" 등
  subCategory: string; // "일반공지" 등
  authorId: string;
  authorName: string;
  channelName: string;
  createdAt: string;
  deadline: string | null;
  isLiked: boolean; // 프론트엔드의 bookmarked
  isCompleted: boolean; // 프론트엔드의 completed
}

/**
 * 북마크 목록 조회 응답 데이터
 */
export interface BookmarksListData {
  notices: NoticeApiResponse[];
}

/**
 * 북마크 추가/삭제 응답 데이터
 */
export interface BookmarkToggleData {
  userNoticeId?: number;
  isLiked: boolean;
}

/**
 * 공지사항 목록 조회 Query Parameters
 */
export interface NoticeListParams {
  page?: number;
  limit?: number;
  search?: string;
  bookmarked?: boolean;
  completed?: boolean;
  category?: Category;
  subcategory?: Subcategory;
  channel?: string;
  deadline_from?: string;
  deadline_to?: string;
  sort_by?: "latest" | "deadline" | "title";
  sort_order?: "asc" | "desc";
}

/**
 * 북마크 목록 조회 Query Parameters
 */
export interface BookmarksListParams {
  type?: "channel" | "dm";
  sort?: "recent" | "oldest";
}
