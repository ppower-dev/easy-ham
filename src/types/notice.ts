/**
 * 공지사항 관련 타입 정의
 */

export type Category = '학사' | '취업';

export type Subcategory = '할일' | '특강' | '정보' | '이벤트';

export interface Attachment {
  id: number;
  type: 'image' | 'file';
  name: string;
  url: string;
  size?: number;
}

export interface Notice {
  id: number;
  title: string;
  content: string;
  author: string;
  channel: string;
  category: Category;
  subcategory: Subcategory;
  dday: number | null;
  deadline?: string;
  bookmarked: boolean;
  completed: boolean;
  attachments?: Attachment[];
  mattermostUrl?: string;
  createdAt: string;
  updatedAt: string;
}
