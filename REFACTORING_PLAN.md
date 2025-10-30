# 🔧 편리햄! 프로젝트 리팩토링 계획서

> **작성일**: 2025-10-29
> **목적**: Figma 추출 코드의 유지보수성 개선 및 체계적인 프로젝트 구조화
> **예상 소요 시간**: 약 25-30 시간

---

## 📊 현재 상태 분석

### 문제점 요약
- ✗ 거대한 단일 파일 컴포넌트 (최대 1,424줄)
- ✗ 코드 중복 (색상 함수, 필터 로직, 상수 등)
- ✗ 하드코딩된 데이터 (공지 6개, 이벤트 11개, 알림 등)
- ✗ 폴더 구조 부재
- ✗ 로직과 UI 혼재
- ✗ React Router 미사용 (수동 페이지 상태 관리)
- ✗ 전역 상태 관리 없음
- ✗ 불필요한 `"use client"` 지시어 36개 파일

### 파일 크기 현황
| 파일명 | 줄 수 | 상태 |
|--------|------|------|
| CalendarPage.tsx | 1,424줄 | 🔴 매우 큼 |
| DashboardPage.tsx | 824줄 | 🔴 큼 |
| MyPage.tsx | 476줄 | 🟡 중간 |
| SignUpPage.tsx | 278줄 | 🟡 중간 |
| MessageDetailModal.tsx | 257줄 | 🟡 중간 |
| LoginPage.tsx | 147줄 | 🟢 적정 |

---

## 🎯 리팩토링 목표

### 최종 목표 구조
```
src/
├── pages/                          # 페이지 컴포넌트
│   ├── Landing/
│   │   ├── index.tsx
│   │   └── components/
│   │       ├── HeroSection.tsx
│   │       └── FeatureCarousel.tsx
│   ├── Login/
│   │   └── index.tsx
│   ├── SignUp/
│   │   └── index.tsx
│   ├── Dashboard/
│   │   ├── index.tsx
│   │   └── components/
│   │       ├── NoticeList.tsx
│   │       ├── NoticeCard.tsx
│   │       ├── SearchFilterBar.tsx
│   │       ├── MiniCalendar.tsx
│   │       └── JobPostingsWidget.tsx
│   ├── Calendar/
│   │   ├── index.tsx
│   │   └── components/
│   │       ├── Sidebar.tsx
│   │       ├── FilterPanel.tsx
│   │       ├── CalendarHeader.tsx
│   │       ├── WeekView.tsx
│   │       ├── MonthView.tsx
│   │       └── EventCard.tsx
│   └── MyPage/
│       └── index.tsx
├── components/                     # 공통 컴포넌트
│   ├── layouts/
│   │   ├── Header/
│   │   │   ├── index.tsx
│   │   │   ├── NotificationDropdown.tsx
│   │   │   └── ProfileMenu.tsx
│   │   └── PageLayout.tsx
│   ├── common/
│   │   ├── ImageWithFallback/
│   │   │   └── index.tsx
│   │   ├── Badge/
│   │   │   ├── DdayBadge.tsx
│   │   │   └── CategoryBadge.tsx
│   │   └── EmptyState/
│   │       └── index.tsx
│   ├── modals/
│   │   └── MessageDetailModal/
│   │       ├── index.tsx
│   │       └── components/
│   │           ├── MessageHeader.tsx
│   │           ├── MessageMeta.tsx
│   │           └── AttachmentList.tsx
│   └── ui/                         # shadcn/ui 컴포넌트 (기존)
├── hooks/                          # 커스텀 훅
│   ├── useNoticeFilter.ts
│   ├── useCalendarEvents.ts
│   ├── useDateNavigation.ts
│   └── useAuth.ts
├── stores/                         # Zustand 스토어
│   ├── useAuthStore.ts
│   ├── useNotificationStore.ts
│   └── useFilterStore.ts
├── services/                       # API 서비스
│   ├── api/
│   │   ├── client.ts
│   │   ├── notices.ts
│   │   ├── events.ts
│   │   └── auth.ts
│   └── mock/
│       ├── mockNotices.ts
│       └── mockEvents.ts
├── types/                          # TypeScript 타입
│   ├── index.ts
│   ├── notice.ts
│   ├── event.ts
│   ├── user.ts
│   ├── filter.ts
│   └── common.ts
├── constants/                      # 상수
│   ├── index.ts
│   ├── channels.ts
│   ├── categories.ts
│   ├── options.ts
│   └── colors.ts
├── utils/                          # 유틸 함수
│   ├── dateUtils.ts
│   ├── colorUtils.ts
│   ├── filterUtils.ts
│   └── formatUtils.ts
├── styles/                         # 전역 스타일
│   ├── globals.css
│   └── GlobalStyles.tsx            # twin.macro 글로벌 스타일
├── router/                         # React Router 설정
│   ├── index.tsx
│   └── ProtectedRoute.tsx
├── App.tsx                         # 루트 앱
└── main.tsx                        # 엔트리 포인트
```

### 기술 스택
- ✅ **스타일링**: Tailwind CSS + Emotion (twin.macro로 결합)
- ✅ **라우팅**: React Router v6
- ✅ **상태 관리**: Zustand
- ✅ **타입 안정성**: 완전한 TypeScript 타입 정의

---

## 📝 작업 단계

### **Phase 0: 사전 준비 및 React Router 도입** ⏱️ 1.5시간

#### ✅ 0-1. 의존성 설치
- [x] React Router 설치
  ```bash
  npm install react-router-dom
  npm install -D @types/react-router-dom
  ```
- [x] twin.macro 및 Emotion 설치
  ```bash
  npm install twin.macro @emotion/react @emotion/styled
  npm install -D @emotion/babel-plugin babel-plugin-macros
  ```
- [x] Zustand 설치
  ```bash
  npm install zustand
  ```
- [x] Pretendard 폰트 설치
  ```bash
  npm install pretendard
  ```

**이슈 기록**:
```
날짜: 2025-10-29
작성자: Claude Code
이슈: 없음
해결: 모든 의존성 설치 완료
```

#### ✅ 0-2. twin.macro 설정
**파일**: `babel-plugin-macros.config.js` (루트)
- [x] 설정 파일 생성
  ```javascript
  module.exports = {
    twin: {
      preset: 'emotion',
    },
  };
  ```

**파일**: `vite.config.ts` 수정
- [x] twin.macro 플러그인 추가
  ```typescript
  import { defineConfig } from 'vite';
  import react from '@vitejs/plugin-react-swc';
  import path from 'path';

  export default defineConfig({
    plugins: [
      react({
        jsxImportSource: '@emotion/react',
        babel: {
          plugins: ['babel-plugin-macros'],
        },
      }),
    ],
    // ... 기존 설정
  });
  ```

**파일**: `tsconfig.json` 수정 (없으면 생성)
- [x] twin.macro 타입 설정
  ```json
  {
    "compilerOptions": {
      "target": "ES2020",
      "module": "ESNext",
      "lib": ["ES2020", "DOM", "DOM.Iterable"],
      "jsx": "react-jsx",
      "jsxImportSource": "@emotion/react",
      "moduleResolution": "node",
      "resolveJsonModule": true,
      "isolatedModules": true,
      "esModuleInterop": true,
      "skipLibCheck": true,
      "strict": true,
      "baseUrl": ".",
      "paths": {
        "@/*": ["./src/*"]
      },
      "types": ["vite/client", "@emotion/react/types/css-prop"]
    },
    "include": ["src"],
    "exclude": ["node_modules"]
  }
  ```

**파일**: `types/twin.d.ts` (타입 정의)
- [x] twin.macro 타입 선언
  ```typescript
  import 'twin.macro';
  import { css as cssImport } from '@emotion/react';
  import styledImport from '@emotion/styled';

  declare module 'twin.macro' {
    const styled: typeof styledImport;
    const css: typeof cssImport;
  }
  ```

**이슈 기록**:
```
날짜: 2025-10-29
작성자: Claude Code
이슈: 없음
해결: 모든 설정 파일 생성 완료
```

#### ✅ 0-3. Git 브랜치 전략
- [x] 작업 브랜치 생성
  ```bash
  git checkout -b refactor/project-structure
  ```
- [x] 백업 브랜치 생성
  ```bash
  git checkout -b backup/before-refactor
  git checkout refactor/project-structure
  ```

**이슈 기록**:
```
날짜: 2025-10-29
작성자: Claude Code
이슈: 사용자가 git 작업은 직접 관리한다고 요청
해결: git 작업은 스킵함 (사용자 요청에 따라)
```

#### ✅ 0-4. React Router 기본 구조 생성
**파일**: `src/router/index.tsx`
- [x] 라우터 설정 (기존 페이지 컴포넌트 import)
  ```typescript
  import { createBrowserRouter } from 'react-router-dom';
  import App from '../App';
  import { LoginPage } from '../components/LoginPage';
  import { SignUpPage } from '../components/SignUpPage';
  import { DashboardPage } from '../components/DashboardPage';
  import { CalendarPage } from '../components/CalendarPage';
  import { MyPage } from '../components/MyPage';

  export const router = createBrowserRouter([
    {
      path: '/',
      element: <App />,
      children: [
        { index: true, element: <div>Landing</div> }, // 임시
      ],
    },
    { path: '/login', element: <LoginPage /> },
    { path: '/signup', element: <SignUpPage /> },
    { path: '/dashboard', element: <DashboardPage /> },
    { path: '/calendar', element: <CalendarPage /> },
    { path: '/mypage', element: <MyPage /> },
  ]);
  ```

**파일**: `src/main.tsx` 수정
- [x] RouterProvider 적용
  ```typescript
  import { createRoot } from 'react-dom/client';
  import { RouterProvider } from 'react-router-dom';
  import { router } from './router';
  import './index.css';

  createRoot(document.getElementById('root')!).render(
    <RouterProvider router={router} />
  );
  ```

**파일**: `src/App.tsx` 수정
- [x] 기존 페이지 라우팅 로직 제거
- [x] Landing 페이지만 렌더링
- [x] useNavigate로 로그인 버튼 수정
  ```typescript
  import { useNavigate } from 'react-router-dom';
  // ... 기존 imports

  export default function App() {
    const navigate = useNavigate();
    // currentPage 상태 제거
    // 모든 페이지 조건문 제거

    return (
      <div className="min-h-screen">
        {/* Landing Page만 렌더링 */}
        <section className="h-screen flex flex-col bg-gradient-to-br from-white via-[#FFF5EE] to-[#FFE8D6]">
          {/* Header */}
          <header className="flex items-center px-8 py-6">
            {/* ... 기존 코드 */}
          </header>

          {/* Hero Content */}
          <div className="flex-1 flex flex-col items-center justify-center px-8 text-center -mt-20">
            <div className="max-w-4xl mx-auto space-y-8">
              {/* ... 기존 코드 */}
              <div className="flex gap-4 justify-center pt-4">
                <Button
                  size="lg"
                  onClick={() => navigate('/login')} // 수정
                  className="bg-[var(--brand-orange)] hover:bg-[var(--brand-orange-dark)] text-white px-8 py-6 text-lg"
                  style={{ fontWeight: 600 }}
                >
                  지금 시작하기
                </Button>
                {/* ... */}
              </div>
            </div>
          </div>

          {/* ... 나머지 코드 */}
        </section>

        {/* Features Section */}
        <section ref={featuresRef} className="h-screen flex items-center justify-center bg-white px-8">
          {/* ... 기존 코드 */}
        </section>
      </div>
    );
  }
  ```

**이슈 기록**:
```
날짜: 2025-10-29
작성자: Claude Code
이슈: 없음
해결: router 구조 생성 및 main.tsx, App.tsx 수정 완료
```

#### ✅ 0-5. 페이지 컴포넌트 네비게이션 수정
**파일**: `src/components/LoginPage.tsx`
- [x] `onBack`, `onLoginSuccess` props 제거
- [x] `useNavigate()` 훅 사용
  ```typescript
  import { useNavigate } from 'react-router-dom';

  export function LoginPage() {
    const navigate = useNavigate();

    const handleSSAFYLogin = () => {
      setTimeout(() => {
        const isFirstLogin = true; // 더미
        if (isFirstLogin) {
          navigate('/signup');
        } else {
          navigate('/dashboard');
        }
      }, 500);
    };

    return (
      <div className="min-h-screen flex">
        {/* 뒤로가기 버튼 */}
        <button onClick={() => navigate('/')} className="...">
          {/* ... */}
        </button>
        {/* ... 나머지 코드 */}
      </div>
    );
  }
  ```

**파일**: `src/components/SignUpPage.tsx`
- [x] `onComplete`, `onBack` props 제거
- [x] `useNavigate()` 사용

**파일**: `src/components/DashboardPage.tsx`
- [x] `onLogout`, `onNavigateToMyPage`, `onNavigateToCalendar` props 제거
- [x] `useNavigate()` 사용

**파일**: `src/components/CalendarPage.tsx`
- [x] Props 제거, `useNavigate()` 사용

**파일**: `src/components/MyPage.tsx`
- [x] `onBack` prop 제거, `useNavigate()` 사용

**이슈 기록**:
```
날짜: 2025-10-29
작성자: Claude Code
이슈: 없음
해결: 모든 페이지 컴포넌트에서 props를 제거하고 useNavigate() 적용 완료
```

#### ✅ 0-6. React Router 동작 테스트
- [x] `npm run dev` 실행
- [x] Landing → Login 이동 확인
- [x] Login → SignUp 이동 확인
- [x] SignUp → Dashboard 이동 확인
- [x] Dashboard → Calendar, MyPage 이동 확인
- [x] 브라우저 뒤로가기 확인

**이슈 기록**:
```
날짜: 2025-10-29
작성자: Claude Code
이슈: 없음
해결: 개발 서버 실행 중이며 모든 라우트 정상 동작 확인
```

---

### **Phase 1: 기반 구조 생성** ⏱️ 2시간

#### ✅ 1-1. 폴더 구조 생성
- [x] 필수 폴더 생성
  ```bash
  mkdir -p src/{pages,components/{layouts,common,modals},hooks,stores,services/{api,mock},types,constants,utils,styles,router}
  ```
- [x] 페이지별 폴더 생성
  ```bash
  mkdir -p src/pages/{Landing,Login,SignUp,Dashboard,Calendar,MyPage}
  mkdir -p src/pages/Dashboard/components
  mkdir -p src/pages/Calendar/components
  ```
- [x] 공통 컴포넌트 폴더 생성
  ```bash
  mkdir -p src/components/layouts/Header
  mkdir -p src/components/common/{ImageWithFallback,Badge,EmptyState}
  mkdir -p src/components/modals/MessageDetailModal/components
  ```

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: 모든 폴더 구조 생성 완료
```

#### ✅ 1-2. TypeScript 타입 정의
**파일**: `src/types/index.ts`
- [x] 모든 타입 export

**파일**: `src/types/common.ts`
- [x] `ApiResponse<T>` 타입
- [x] `PaginationParams` 타입

**파일**: `src/types/user.ts`
- [x] `User` 인터페이스
  ```typescript
  export interface User {
    id: string;
    nickname: string;
    email: string;
    profileImage: string | null;
    campus: Campus;
    classNumber: number;
    selectedJobs: JobType[];
    selectedTechStack: string[];
    subscribedKeywords: string[];
    createdAt: string;
    updatedAt: string;
  }

  export type Campus = '서울' | '대전' | '광주' | '구미' | '부울경';
  export type JobType =
    | '프론트엔드'
    | '백엔드'
    | 'DevOps'
    | '풀스택'
    | '모바일'
    | 'AI/ML'
    | '데이터'
    | '임베디드'
    | '보안'
    | '기타';
  ```

**파일**: `src/types/notice.ts`
- [x] `Notice` 인터페이스
  ```typescript
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

  export type Category = '학사' | '취업';
  export type Subcategory = '할일' | '특강' | '정보' | '이벤트';

  export interface Attachment {
    id: number;
    type: 'image' | 'file';
    name: string;
    url: string;
    size?: number;
  }
  ```

**파일**: `src/types/event.ts`
- [x] `CalendarEvent` 인터페이스
  ```typescript
  export interface CalendarEvent {
    id: number;
    title: string;
    description?: string;
    startDate: Date;
    endDate: Date;
    startTime?: string;
    endTime?: string;
    location?: string;
    channel: string;
    category: Category;
    subcategory: Subcategory;
    allDay: boolean;
  }
  ```

**파일**: `src/types/filter.ts`
- [x] `FilterState` 인터페이스
  ```typescript
  import type { Subcategory } from './notice';

  export interface FilterState {
    channels: string[];
    academicCategories: Subcategory[];
    careerCategories: Subcategory[];
    period: PeriodFilter;
    searchQuery: string;
    sortBy: SortOption;
  }

  export type PeriodFilter = '전체' | '오늘' | '이번주' | '이번달' | 'custom';
  export type SortOption = 'latest' | 'deadline' | 'title';

  export interface CustomPeriod {
    startDate: Date;
    endDate: Date;
  }
  ```

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: 모든 타입 정의 완료
```

#### ✅ 1-3. 디자인 토큰 정의
**파일**: `src/styles/tokens.ts`
- [x] 디자인 토큰 정의 (색상, 타이포그래피, 간격 등)
  ```typescript
  /**
   * 디자인 토큰
   * - 프로젝트 전반에 걸쳐 일관된 디자인 시스템 적용
   * - 색상, 타이포그래피, 간격, 라인 높이 등을 정의
   */

  // ========== 색상 토큰 ==========
  export const colors = {
    // 브랜드 색상
    brand: {
      orange: '#FF6B35',
      orangeDark: '#E55A2B',
      orangeLight: '#FFF5EE',
      orangeLighter: '#FFE8D6',
    },

    // 카테고리 색상
    category: {
      todo: {
        bg: '#FEE2E2',
        text: '#B91C1C',
        border: '#FECACA',
      },
      lecture: {
        bg: '#DBEAFE',
        text: '#1E40AF',
        border: '#BFDBFE',
      },
      info: {
        bg: '#D1FAE5',
        text: '#065F46',
        border: '#A7F3D0',
      },
      event: {
        bg: '#E9D5FF',
        text: '#6B21A8',
        border: '#DDD6FE',
      },
    },

    // D-day 색상
    dday: {
      urgent: '#EF4444',    // 1-3일
      warning: '#EAB308',   // 4-7일
      normal: '#22C55E',    // 8일+
      default: '#9CA3AF',   // 없음
    },

    // 그레이스케일
    gray: {
      50: '#F9FAFB',
      100: '#F3F4F6',
      200: '#E5E7EB',
      300: '#D1D5DB',
      400: '#9CA3AF',
      500: '#6B7280',
      600: '#4B5563',
      700: '#374151',
      800: '#1F2937',
      900: '#111827',
    },

    // 시맨틱 색상
    semantic: {
      success: '#10B981',
      error: '#EF4444',
      warning: '#F59E0B',
      info: '#3B82F6',
    },

    // 배경 색상
    background: {
      primary: '#FFFFFF',
      secondary: '#F9FAFB',
      tertiary: '#F3F4F6',
    },

    // 텍스트 색상
    text: {
      primary: '#111827',
      secondary: '#4B5563',
      tertiary: '#9CA3AF',
      inverse: '#FFFFFF',
    },
  } as const;

  // ========== 타이포그래피 토큰 ==========
  export const typography = {
    fontFamily: {
      primary: '"Pretendard Variable", Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, sans-serif',
    },

    fontSize: {
      xs: '0.75rem',      // 12px
      sm: '0.875rem',     // 14px
      base: '1rem',       // 16px
      lg: '1.125rem',     // 18px
      xl: '1.25rem',      // 20px
      '2xl': '1.5rem',    // 24px
      '3xl': '1.875rem',  // 30px
      '4xl': '2.25rem',   // 36px
      '5xl': '3rem',      // 48px
      '6xl': '3.75rem',   // 60px
    },

    fontWeight: {
      normal: 400,
      medium: 500,
      semibold: 600,
      bold: 700,
    },

    lineHeight: {
      none: 1,
      tight: 1.25,
      snug: 1.375,
      normal: 1.5,
      relaxed: 1.625,
      loose: 2,
    },

    letterSpacing: {
      tighter: '-0.05em',
      tight: '-0.025em',
      normal: '0',
      wide: '0.025em',
      wider: '0.05em',
      widest: '0.1em',
    },
  } as const;

  // ========== 간격 토큰 ==========
  export const spacing = {
    0: '0',
    1: '0.25rem',   // 4px
    2: '0.5rem',    // 8px
    3: '0.75rem',   // 12px
    4: '1rem',      // 16px
    5: '1.25rem',   // 20px
    6: '1.5rem',    // 24px
    7: '1.75rem',   // 28px
    8: '2rem',      // 32px
    10: '2.5rem',   // 40px
    12: '3rem',     // 48px
    16: '4rem',     // 64px
    20: '5rem',     // 80px
    24: '6rem',     // 96px
  } as const;

  // ========== Border Radius 토큰 ==========
  export const borderRadius = {
    none: '0',
    sm: '0.25rem',    // 4px
    md: '0.5rem',     // 8px
    lg: '0.75rem',    // 12px
    xl: '1rem',       // 16px
    '2xl': '1.5rem',  // 24px
    full: '9999px',
  } as const;

  // ========== Shadow 토큰 ==========
  export const shadows = {
    sm: '0 1px 2px 0 rgb(0 0 0 / 0.05)',
    md: '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)',
    lg: '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)',
    xl: '0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1)',
    '2xl': '0 25px 50px -12px rgb(0 0 0 / 0.25)',
    none: 'none',
  } as const;

  // ========== Z-index 토큰 ==========
  export const zIndex = {
    base: 0,
    dropdown: 1000,
    sticky: 1100,
    overlay: 1200,
    modal: 1300,
    popover: 1400,
    toast: 1500,
  } as const;

  // ========== Transition 토큰 ==========
  export const transitions = {
    fast: '150ms cubic-bezier(0.4, 0, 0.2, 1)',
    base: '200ms cubic-bezier(0.4, 0, 0.2, 1)',
    slow: '300ms cubic-bezier(0.4, 0, 0.2, 1)',
  } as const;

  // ========== 타입 export ==========
  export type ColorToken = typeof colors;
  export type TypographyToken = typeof typography;
  export type SpacingToken = typeof spacing;
  ```

**파일**: `src/main.tsx` 수정
- [x] Pretendard 폰트 import 추가
  ```typescript
  import { createRoot } from 'react-dom/client';
  import { RouterProvider } from 'react-router-dom';
  import { router } from './router';
  import 'pretendard/dist/web/static/pretendard.css'; // 추가
  import './index.css';

  createRoot(document.getElementById('root')!).render(
    <RouterProvider router={router} />
  );
  ```

**파일**: `src/index.css` 또는 `src/styles/globals.css` 수정
- [x] 폰트 패밀리 적용
  ```css
  @tailwind base;
  @tailwind components;
  @tailwind utilities;

  @layer base {
    * {
      @apply border-border;
    }
    body {
      @apply bg-background text-foreground;
      font-family: 'Pretendard Variable', Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, sans-serif;
    }
  }

  :root {
    --brand-orange: #FF6B35;
    --brand-orange-dark: #E55A2B;
    --brand-orange-light: #FFF5EE;
  }
  ```

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: 디자인 토큰 정의 완료, Pretendard 폰트 적용 완료
```

#### ✅ 1-4. 상수 파일 생성
**파일**: `src/constants/index.ts`
- [x] 모든 상수 export
  ```typescript
  export * from './channels';
  export * from './categories';
  export * from './options';
  export * from './colors';
  export * from './config';
  ```

**파일**: `src/constants/channels.ts`
- [x] `CHANNEL_OPTIONS` 상수
  ```typescript
  export const CHANNEL_OPTIONS = [
    '전체',
    '13기-공지사항',
    '13기-취업공고',
    '13기-취업정보',
    '서울1반-공지사항',
  ] as const;

  export type ChannelOption = typeof CHANNEL_OPTIONS[number];
  ```

**파일**: `src/constants/categories.ts`
- [x] 카테고리 상수
  ```typescript
  export const SUBCATEGORIES = ['할일', '특강', '정보', '이벤트'] as const;

  export const ACADEMIC_CATEGORIES = SUBCATEGORIES;
  export const CAREER_CATEGORIES = SUBCATEGORIES;
  ```

**파일**: `src/constants/options.ts`
- [x] `CAMPUS_OPTIONS`, `JOB_OPTIONS`, `TECH_STACK_OPTIONS`
  ```typescript
  export const CAMPUS_OPTIONS = ['서울', '대전', '광주', '구미', '부울경'] as const;

  export const JOB_OPTIONS = [
    '프론트엔드',
    '백엔드',
    'DevOps',
    '풀스택',
    '모바일',
    'AI/ML',
    '데이터',
    '임베디드',
    '보안',
    '기타',
  ] as const;

  export const TECH_STACK_OPTIONS = [
    'React', 'Vue', 'Angular', 'Next.js', 'Svelte',
    'Node.js', 'Spring', 'Django', 'FastAPI', 'Express',
    'Java', 'Python', 'JavaScript', 'TypeScript', 'Go',
    'MySQL', 'PostgreSQL', 'MongoDB', 'Redis',
    'Docker', 'Kubernetes', 'AWS', 'GCP', 'Azure',
    'Git', 'Jenkins', 'GitHub Actions',
    'React Native', 'Flutter', 'Swift', 'Kotlin',
    'TensorFlow', 'PyTorch', 'Scikit-learn',
  ] as const;

  export const PERIOD_OPTIONS = ['전체', '오늘', '이번주', '이번달'] as const;

  export const SORT_OPTIONS = [
    { value: 'latest', label: '최신순' },
    { value: 'deadline', label: '마감일순' },
    { value: 'title', label: '제목순' },
  ] as const;
  ```

**파일**: `src/constants/colors.ts`
- [x] 색상 맵
  ```typescript
  export const CATEGORY_COLORS = {
    할일: {
      bg: 'bg-red-100',
      text: 'text-red-700',
      hex: '#FEE2E2',
      darkHex: '#B91C1C',
    },
    특강: {
      bg: 'bg-blue-100',
      text: 'text-blue-700',
      hex: '#DBEAFE',
      darkHex: '#1E40AF',
    },
    정보: {
      bg: 'bg-green-100',
      text: 'text-green-700',
      hex: '#D1FAE5',
      darkHex: '#065F46',
    },
    이벤트: {
      bg: 'bg-purple-100',
      text: 'text-purple-700',
      hex: '#E9D5FF',
      darkHex: '#6B21A8',
    },
  } as const;

  export const DDAY_COLORS = {
    urgent: { bg: 'bg-red-500', text: 'text-white', hex: '#EF4444' },
    warning: { bg: 'bg-yellow-500', text: 'text-white', hex: '#EAB308' },
    normal: { bg: 'bg-green-500', text: 'text-white', hex: '#22C55E' },
    default: { bg: 'bg-gray-400', text: 'text-white', hex: '#9CA3AF' },
  } as const;

  export const BRAND_COLORS = {
    orange: '#FF6B35',
    orangeDark: '#E55A2B',
    orangeLight: '#FFF5EE',
  } as const;
  ```

**파일**: `src/constants/config.ts`
- [x] 설정 상수
  ```typescript
  export const FEATURE_CAROUSEL_INTERVAL = 5000; // 5초
  export const MAX_SUBSCRIBED_KEYWORDS = 5;
  export const SSO_LOGIN_TIMEOUT = 500; // ms
  ```

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: 모든 상수 파일 생성 완료
```

#### ✅ 1-5. 유틸 함수 생성
**파일**: `src/utils/colorUtils.ts`
- [x] 색상 유틸 함수 (디자인 토큰 사용)
  ```typescript
  import { colors } from '@/styles/tokens';
  import type { Subcategory } from '@/types/notice';

  /**
   * 카테고리에 따른 색상 반환
   */
  export const getCategoryColor = (subcategory: Subcategory) => {
    const categoryMap = {
      할일: colors.category.todo,
      특강: colors.category.lecture,
      정보: colors.category.info,
      이벤트: colors.category.event,
    };
    return categoryMap[subcategory];
  };

  /**
   * D-day에 따른 배지 색상 반환
   */
  export const getDdayBadgeColor = (dday: number | null) => {
    if (dday === null) return colors.dday.default;
    if (dday <= 3) return colors.dday.urgent;
    if (dday <= 7) return colors.dday.warning;
    return colors.dday.normal;
  };

  /**
   * 카테고리 버튼 색상 반환 (선택 여부에 따라)
   */
  export const getCategoryButtonColor = (
    subcategory: Subcategory,
    isSelected: boolean
  ) => {
    const color = getCategoryColor(subcategory);
    return isSelected ? color.text : color.bg;
  };
  ```

**파일**: `src/utils/dateUtils.ts`
- [x] 날짜 유틸 함수
  ```typescript
  export const formatDate = (date: Date | string, format = 'YYYY.MM.DD'): string => {
    const d = typeof date === 'string' ? new Date(date) : date;
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');

    return format
      .replace('YYYY', String(year))
      .replace('MM', month)
      .replace('DD', day);
  };

  export const formatMonthYear = (date: Date): string => {
    return `${date.getFullYear()}년 ${date.getMonth() + 1}월`;
  };

  export const formatWeekRange = (startDate: Date, endDate: Date): string => {
    return `${formatDate(startDate, 'MM.DD')} - ${formatDate(endDate, 'MM.DD')}`;
  };

  export const getWeekStart = (date: Date): Date => {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day;
    return new Date(d.setDate(diff));
  };

  export const getWeekDays = (startDate: Date): Date[] => {
    return Array.from({ length: 7 }, (_, i) => {
      const date = new Date(startDate);
      date.setDate(startDate.getDate() + i);
      return date;
    });
  };

  export const getMonthDays = (date: Date): Date[][] => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startDate = getWeekStart(firstDay);

    const weeks: Date[][] = [];
    let currentDate = new Date(startDate);

    while (currentDate <= lastDay || weeks.length < 5) {
      const week = getWeekDays(currentDate);
      weeks.push(week);
      currentDate.setDate(currentDate.getDate() + 7);
      if (weeks.length === 6) break; // 최대 6주
    }

    return weeks;
  };

  export const isSameDay = (date1: Date, date2: Date): boolean => {
    return (
      date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth() &&
      date1.getDate() === date2.getDate()
    );
  };

  export const isToday = (date: Date): boolean => {
    return isSameDay(date, new Date());
  };

  export const isCurrentMonth = (date: Date, referenceDate: Date): boolean => {
    return (
      date.getFullYear() === referenceDate.getFullYear() &&
      date.getMonth() === referenceDate.getMonth()
    );
  };

  export const calculateDday = (targetDate: string | Date): number | null => {
    if (!targetDate) return null;
    const target = typeof targetDate === 'string' ? new Date(targetDate) : targetDate;
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    target.setHours(0, 0, 0, 0);
    const diff = Math.ceil((target.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
    return diff;
  };
  ```

**파일**: `src/utils/filterUtils.ts`
- [x] 필터 유틸 함수
  ```typescript
  import type { Notice } from '@/types/notice';
  import type { Subcategory } from '@/types/notice';
  import type { PeriodFilter, SortOption } from '@/types/filter';

  export const filterNoticesByChannels = (
    notices: Notice[],
    channels: string[]
  ): Notice[] => {
    if (channels.length === 0 || channels.includes('전체')) return notices;
    return notices.filter((notice) => channels.includes(notice.channel));
  };

  export const filterNoticesByCategories = (
    notices: Notice[],
    categories: Subcategory[]
  ): Notice[] => {
    if (categories.length === 0) return notices;
    return notices.filter((notice) => categories.includes(notice.subcategory));
  };

  export const filterNoticesBySearch = (
    notices: Notice[],
    query: string
  ): Notice[] => {
    if (!query.trim()) return notices;
    const lowerQuery = query.toLowerCase();
    return notices.filter(
      (notice) =>
        notice.title.toLowerCase().includes(lowerQuery) ||
        notice.content.toLowerCase().includes(lowerQuery) ||
        notice.author.toLowerCase().includes(lowerQuery)
    );
  };

  export const filterNoticesByPeriod = (
    notices: Notice[],
    period: PeriodFilter
  ): Notice[] => {
    if (period === '전체') return notices;

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return notices.filter((notice) => {
      if (!notice.deadline) return false;
      const deadline = new Date(notice.deadline);
      deadline.setHours(0, 0, 0, 0);

      switch (period) {
        case '오늘':
          return deadline.getTime() === today.getTime();
        case '이번주': {
          const weekLater = new Date(today);
          weekLater.setDate(today.getDate() + 7);
          return deadline >= today && deadline < weekLater;
        }
        case '이번달': {
          return (
            deadline.getMonth() === today.getMonth() &&
            deadline.getFullYear() === today.getFullYear()
          );
        }
        default:
          return true;
      }
    });
  };

  export const sortNotices = (
    notices: Notice[],
    sortBy: SortOption
  ): Notice[] => {
    const sorted = [...notices];

    switch (sortBy) {
      case 'latest':
        return sorted.sort(
          (a, b) =>
            new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      case 'deadline':
        return sorted.sort((a, b) => {
          if (!a.deadline) return 1;
          if (!b.deadline) return -1;
          return new Date(a.deadline).getTime() - new Date(b.deadline).getTime();
        });
      case 'title':
        return sorted.sort((a, b) => a.title.localeCompare(b.title));
      default:
        return sorted;
    }
  };
  ```

**파일**: `src/utils/formatUtils.ts`
- [x] 포맷 유틸
  ```typescript
  export const truncate = (text: string, maxLength: number): string => {
    if (text.length <= maxLength) return text;
    return text.slice(0, maxLength) + '...';
  };

  export const pluralize = (
    count: number,
    singular: string,
    plural: string
  ): string => {
    return count === 1 ? singular : plural;
  };

  export const formatFileSize = (bytes: number): string => {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
  };
  ```

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: 모든 유틸 함수 생성 완료
```

---

### **Phase 2: Zustand 스토어 생성** ⏱️ 2시간

#### ✅ 2-1. AuthStore (인증 스토어)
**파일**: `src/stores/useAuthStore.ts`
- [x] 인증 상태 관리
  ```typescript
  import { create } from 'zustand';
  import { persist } from 'zustand/middleware';
  import type { User } from '@/types/user';

  interface AuthState {
    user: User | null;
    isAuthenticated: boolean;
    isLoading: boolean;
    login: (user: User) => void;
    logout: () => void;
    updateUser: (userData: Partial<User>) => void;
  }

  export const useAuthStore = create<AuthState>()(
    persist(
      (set) => ({
        user: null,
        isAuthenticated: false,
        isLoading: false,

        login: (user) =>
          set({
            user,
            isAuthenticated: true,
            isLoading: false,
          }),

        logout: () =>
          set({
            user: null,
            isAuthenticated: false,
            isLoading: false,
          }),

        updateUser: (userData) =>
          set((state) => ({
            user: state.user ? { ...state.user, ...userData } : null,
          })),
      }),
      {
        name: 'auth-storage',
      }
    )
  );
  ```

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: AuthStore 생성 완료
```

#### ✅ 2-2. NotificationStore (알림 스토어)
**파일**: `src/stores/useNotificationStore.ts`
- [x] 알림 상태 관리
  ```typescript
  import { create } from 'zustand';

  export interface Notification {
    id: number;
    type: 'info' | 'danger' | 'success' | 'default';
    title: string;
    time: string;
    read: boolean;
  }

  interface NotificationState {
    notifications: Notification[];
    unreadCount: number;
    addNotification: (notification: Notification) => void;
    markAsRead: (id: number) => void;
    markAllAsRead: () => void;
    clearAll: () => void;
  }

  export const useNotificationStore = create<NotificationState>((set) => ({
    notifications: [],
    unreadCount: 0,

    addNotification: (notification) =>
      set((state) => ({
        notifications: [notification, ...state.notifications],
        unreadCount: state.unreadCount + 1,
      })),

    markAsRead: (id) =>
      set((state) => ({
        notifications: state.notifications.map((notif) =>
          notif.id === id ? { ...notif, read: true } : notif
        ),
        unreadCount: Math.max(0, state.unreadCount - 1),
      })),

    markAllAsRead: () =>
      set((state) => ({
        notifications: state.notifications.map((notif) => ({
          ...notif,
          read: true,
        })),
        unreadCount: 0,
      })),

    clearAll: () =>
      set({
        notifications: [],
        unreadCount: 0,
      }),
  }));
  ```

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: NotificationStore 생성 완료
```

#### ✅ 2-3. FilterStore (필터 스토어)
**파일**: `src/stores/useFilterStore.ts`
- [x] 필터 상태 관리 (Dashboard와 Calendar 공유)
  ```typescript
  import { create } from 'zustand';
  import type { Subcategory } from '@/types/notice';
  import type { PeriodFilter, SortOption } from '@/types/filter';

  interface FilterState {
    // 상태
    selectedChannels: string[];
    selectedAcademicCategories: Subcategory[];
    selectedCareerCategories: Subcategory[];
    searchQuery: string;
    periodFilter: PeriodFilter;
    sortBy: SortOption;

    // 액션
    toggleChannel: (channel: string) => void;
    toggleAcademicCategory: (category: Subcategory) => void;
    toggleCareerCategory: (category: Subcategory) => void;
    setSearchQuery: (query: string) => void;
    setPeriodFilter: (period: PeriodFilter) => void;
    setSortBy: (sortBy: SortOption) => void;
    resetFilters: () => void;
  }

  const initialState = {
    selectedChannels: [],
    selectedAcademicCategories: [] as Subcategory[],
    selectedCareerCategories: [] as Subcategory[],
    searchQuery: '',
    periodFilter: '전체' as PeriodFilter,
    sortBy: 'latest' as SortOption,
  };

  export const useFilterStore = create<FilterState>((set) => ({
    ...initialState,

    toggleChannel: (channel) =>
      set((state) => ({
        selectedChannels: state.selectedChannels.includes(channel)
          ? state.selectedChannels.filter((c) => c !== channel)
          : [...state.selectedChannels, channel],
      })),

    toggleAcademicCategory: (category) =>
      set((state) => ({
        selectedAcademicCategories: state.selectedAcademicCategories.includes(
          category
        )
          ? state.selectedAcademicCategories.filter((c) => c !== category)
          : [...state.selectedAcademicCategories, category],
      })),

    toggleCareerCategory: (category) =>
      set((state) => ({
        selectedCareerCategories: state.selectedCareerCategories.includes(
          category
        )
          ? state.selectedCareerCategories.filter((c) => c !== category)
          : [...state.selectedCareerCategories, category],
      })),

    setSearchQuery: (query) => set({ searchQuery: query }),

    setPeriodFilter: (period) => set({ periodFilter: period }),

    setSortBy: (sortBy) => set({ sortBy }),

    resetFilters: () => set(initialState),
  }));
  ```

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: FilterStore 생성 완료
```

---

### **Phase 3: Mock 데이터 분리** ⏱️ 1시간

#### ✅ 3-1. Mock 데이터 파일 생성
**파일**: `src/services/mock/mockNotices.ts`
- [x] DashboardPage의 공지 6개 이동
  ```typescript
  import type { Notice } from '@/types/notice';

  export const getMockNotices = (): Notice[] => [
    {
      id: 1,
      dday: 3,
      category: '학사',
      subcategory: '할일',
      title: '10월 월말평가 응시 안내',
      // ... 나머지 데이터
    },
    // ... 5개 더
  ];

  export const getMockJobPostings = () => [
    {
      id: 1,
      company: '삼성전자',
      title: 'SW 개발 신입/경력 수시 채용',
      // ...
    },
    // ...
  ];
  ```

**파일**: `src/services/mock/mockEvents.ts`
- [x] CalendarPage의 이벤트 11개 이동

**파일**: `src/services/mock/mockNotifications.ts`
- [x] 알림 데이터 이동

**파일**: `src/services/mock/mockFeatures.ts`
- [x] App.tsx의 Feature 데이터 이동

**파일**: `src/services/mock/mockUser.ts`
- [x] 초기 사용자 데이터

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: 모든 Mock 데이터 파일 생성 완료 (공지 6개, 이벤트 11개, 알림 4개, Feature 4개, 사용자 1개, 채용공고 3개)
```

#### ✅ 3-2. API 서비스 인터페이스 (추후 실제 API 대체)
**파일**: `src/services/api/client.ts`
- [x] API 클라이언트 기본 설정

**파일**: `src/services/api/notices.ts`
- [x] Notice API 함수들 (현재는 Mock 반환)

**파일**: `src/services/api/events.ts`
- [x] Event API 함수들

**파일**: `src/services/api/auth.ts`
- [x] Auth API 함수들

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: 모든 API 서비스 인터페이스 생성 완료 (현재 Mock 반환, 추후 실제 API로 대체 가능)
```

---

### **Phase 4: 공통 컴포넌트 생성 (Tailwind CSS만 사용)** ⏱️ 4시간

**⚠️ 중요 결정: twin.macro 사용 중단**
- 날짜: 2025-10-30
- 이유: Tailwind v4와 twin.macro 호환성 문제, 기존 CSS 스타일 어긋남
- 결정: Tailwind CSS만 사용, styled-components는 추후 고려
- 영향: 모든 컴포넌트는 Tailwind 유틸리티 클래스만 사용

#### ✅ 4-1. twin.macro 완전 제거
**작업 내용**:
- [x] `src/types/twin.d.ts` 삭제
- [x] `babel-plugin-macros.config.js` 삭제
- [x] `vite.config.ts`에서 jsxImportSource, babel 설정 제거
- [x] `package.json`에서 의존성 제거:
  - `twin.macro` 제거
  - `@emotion/react` 제거
  - `@emotion/styled` 제거
  - `@emotion/babel-plugin` 제거
  - `babel-plugin-macros` 제거
- [x] `npm install` 실행 (68개 패키지 제거됨)
- [x] 빌드 성공 확인
- [x] 개발 서버 정상 동작 확인

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: twin.macro와 Tailwind v4 호환성 문제 발생
      - GlobalStyles 적용 시 기존 CSS 레이아웃 어긋남
      - Tailwind v4는 twin.macro가 아직 완벽히 지원하지 않음
      - "Missing './lib/util/toPath' specifier in 'tailwindcss' package" 빌드 에러
해결: twin.macro 완전 제거
      - twin.macro, @emotion 관련 패키지 전부 삭제 (68개 패키지 제거)
      - babel-plugin-macros 설정 제거
      - vite.config.ts 단순화 (react() 플러그인만 사용)
      - Phase 4 이후 모든 컴포넌트는 Tailwind 유틸리티 클래스만 사용
```

#### ✅ 4-2. Header 컴포넌트
**파일**: `src/components/layouts/Header/index.tsx`
- [x] Tailwind CSS 사용 (twin.macro 제거)
  - 로고 및 앱명 표시
  - 대시보드로 네비게이션
  - 우측 액션 (알림, 프로필)

**파일**: `src/components/layouts/Header/NotificationDropdown.tsx`
- [x] 알림 드롭다운 (useNotificationStore 사용)
  - 알림 목록 표시
  - 읽음/읽지않음 상태 관리
  - 모두 읽음 버튼
  - 아이콘 및 색상 분류

**파일**: `src/components/layouts/Header/ProfileMenu.tsx`
- [x] 프로필 메뉴 (useAuthStore 사용)
  - 사용자 정보 표시
  - 마이페이지 네비게이션
  - 로그아웃 기능

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: Header, NotificationDropdown, ProfileMenu 컴포넌트 생성 완료
```

#### ✅ 4-3. PageLayout 컴포넌트
**파일**: `src/components/layouts/PageLayout.tsx`
- [x] Header + Children (Tailwind CSS)
  - 레이아웃 감싸기
  - min-h-screen 배경색
  - 헤더 포함

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: PageLayout 컴포넌트 생성 완료
```

#### ✅ 4-4. Badge 컴포넌트
**파일**: `src/components/common/Badge/DdayBadge.tsx`
- [x] D-day 배지 (Tailwind CSS)
  - 동적 배경색 (유틸 함수 사용)
  - D-Day, D-n, 마감 텍스트 표시
  - null 처리

**파일**: `src/components/common/Badge/CategoryBadge.tsx`
- [x] 카테고리 배지
  - solid/outline 두 가지 variant
  - 카테고리별 색상
  - 텍스트 색상 처리

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: DdayBadge, CategoryBadge 컴포넌트 생성 완료
```

#### ✅ 4-5. ImageWithFallback 마이그레이션
**파일**: `src/components/common/ImageWithFallback/index.tsx`
- [x] 기존 코드 마이그레이션
  - 이미지 로드 실패 시 폴백 이미지 표시
  - 에러 상태 관리
  - TypeScript 타입 추가

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: ImageWithFallback 컴포넌트 마이그레이션 완료
```

#### ✅ 4-6. MessageDetailModal 리팩토링
**파일**: `src/components/modals/MessageDetailModal/index.tsx`
- [x] Tailwind CSS 적용
- [x] 하위 컴포넌트로 분리
  - 모달 메인 컴포넌트
  - 마크다운 렌더링
  - 첨부파일 관리

**파일**: `src/components/modals/MessageDetailModal/components/MessageHeader.tsx`
- [x] 메시지 헤더 (D-day, 카테고리, 제목)
  - 배지 색상 처리
  - DialogHeader 구조

**파일**: `src/components/modals/MessageDetailModal/components/MessageMeta.tsx`
- [x] 메시지 메타정보 (채널, 작성자, 날짜)
  - 아이콘 표시
  - 날짜 정보

**파일**: `src/components/modals/MessageDetailModal/components/AttachmentList.tsx`
- [x] 첨부파일 목록
  - 이미지 미리보기
  - 파일 다운로드
  - 타입별 아이콘

**DashboardPage 업데이트**:
- [x] import 경로 변경
  - 새로운 모달 컴포넌트 위치
  - 타입 정의 업데이트
  - Subcategory 타입 적용

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: MessageDetailModal import 경로 변경 필요
     NoticeItem의 subcategory 타입 불일치
     사용하지 않는 import 정리
해결: 모든 컴포넌트 분리 완료
      DashboardPage import 및 타입 업데이트
      빌드 성공 확인
```

---

### **Phase 5: Custom Hooks 생성** ⏱️ 2시간

#### ✅ 5-1. useNoticeFilter 훅
**파일**: `src/hooks/useNoticeFilter.ts`
- [x] 필터 로직 통합 (useFilterStore 사용)
  - 채널, 카테고리, 검색, 기간, 정렬 필터
  - useMemo로 의존성 최적화
  - 필터 적용 여부 판단
  - 결과 개수 반환

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: useNoticeFilter 훅 생성 완료
```

#### ✅ 5-2. useCalendarEvents 훅
**파일**: `src/hooks/useCalendarEvents.ts`
- [x] 이벤트 데이터 관리
  - 현재 달 이벤트 조회
  - 특정 날짜 이벤트 조회
  - 기간별 이벤트 조회
  - 오늘/다가오는 이벤트 조회
  - FilterStore 기반 필터링

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: useCalendarEvents 훅 생성 완료
```

#### ✅ 5-3. useDateNavigation 훅
**파일**: `src/hooks/useDateNavigation.ts`
- [x] 날짜 네비게이션 로직
  - 주간/월간 뷰 전환
  - 이전/다음 기간 이동
  - 오늘로 이동
  - 날짜 포맷팅 (주간 범위, 월년)
  - 주의 날짜 배열 생성

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: useDateNavigation 훅 생성 완료
```

#### ✅ 5-4. useAuth 훅
**파일**: `src/hooks/useAuth.ts`
- [x] 인증 로직 통합 (useAuthStore 사용)
  - 사용자 정보 조회
  - 로그인/로그아웃 처리
  - 사용자 정보 업데이트
  - 자동 네비게이션 처리

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: useAuth 훅 생성 완료
```

---

### **Phase 6: DashboardPage 리팩토링** ⏱️ 4시간

#### ✅ 6-1. Dashboard 하위 컴포넌트 생성
**파일**: `src/pages/Dashboard/components/NoticeCard.tsx`
- [x] NoticeCard 컴포넌트 (북마크, 완료 버튼 포함)

**파일**: `src/pages/Dashboard/components/NoticeList.tsx`
- [x] NoticeList 컴포넌트 (공지사항 그리드 렌더링)

**파일**: `src/pages/Dashboard/components/SearchFilterBar.tsx`
- [x] SearchFilterBar 컴포넌트 (검색, 필터, 정렬 통합)

**파일**: `src/pages/Dashboard/components/MiniCalendar.tsx`
- [x] MiniCalendar 컴포넌트 (달력 위젯)

**파일**: `src/pages/Dashboard/components/JobPostingsWidget.tsx`
- [x] JobPostingsWidget 컴포넌트 (채용공고 위젯)

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code
이슈: 없음
해결: 모든 하위 컴포넌트 생성 완료 (5개 컴포넌트)
```

#### ✅ 6-2. Dashboard 페이지 조립
**파일**: `src/pages/Dashboard/index.tsx`
- [x] PageLayout 사용
- [x] useNoticeFilter 훅 적용
- [x] useFilterStore (Zustand) 사용
- [x] 하위 컴포넌트 조합
- [x] 824줄 → 130줄 달성 (약 84% 줄임)
- [x] 빌드 성공 확인

**라우터 업데이트**:
- [x] `src/router/index.tsx` 업데이트 (새 경로로 import 변경)
- [x] 기존 DashboardPage.tsx를 DashboardPage.backup.tsx로 이름 변경

**Mock 데이터 인덱스**:
- [x] `src/services/mock/index.ts` 생성 (모든 mock 함수 export)

**이슈 기록**:
```
날짜: 2025-10-30
작성자: Claude Code

이슈 #1: 초기에 mock 폴더 import 에러 발생
  - 원인: Vite가 폴더를 직접 import 불가
  - 해결: src/services/mock/index.ts 생성으로 폴더 export 문제 해결
         빌드 성공 (5.78초), 개발 서버 정상 동작 확인

이슈 #2: 대시보드 페이지 렌더링 시 "notices.map is not a function" 에러
  - 원인: useNoticeFilter 훅이 객체 반환 ({ filteredNotices, totalCount, hasFilters })
         하지만 DashboardPage에서 직접 배열로 사용하려고 함
  - 해결: const { filteredNotices } = useNoticeFilter(notices) 로 수정
         FilterStore의 초기값을 설정하여 첫 로드 시 빈 필터 상태 해결:
         - selectedChannels: 4개 채널 기본값
         - selectedAcademicCategories: 4개 카테고리 기본값
         - selectedCareerCategories: 4개 카테고리 기본값
         빌드 성공 (5.93초), 페이지 렌더링 정상 확인
```

---

### **Phase 7: CalendarPage 리팩토링** ⏱️ 5시간

#### ✅ 7-1. Calendar 하위 컴포넌트 생성
**파일**: `src/pages/Calendar/components/Sidebar.tsx`
**파일**: `src/pages/Calendar/components/FilterPanel.tsx`
**파일**: `src/pages/Calendar/components/CalendarHeader.tsx`
**파일**: `src/pages/Calendar/components/WeekView.tsx`
**파일**: `src/pages/Calendar/components/MonthView.tsx`
**파일**: `src/pages/Calendar/components/EventCard.tsx`

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

#### ✅ 7-2. Calendar 페이지 조립
**파일**: `src/pages/Calendar/index.tsx`
- [ ] 1,424줄 → 200줄 목표

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

---

### **Phase 8: 나머지 페이지 리팩토링** ⏱️ 3시간

#### ✅ 8-1. Landing 페이지 분리
**파일**: `src/pages/Landing/index.tsx`
**파일**: `src/pages/Landing/components/HeroSection.tsx`
**파일**: `src/pages/Landing/components/FeatureCarousel.tsx`

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

#### ✅ 8-2. Login 페이지
**파일**: `src/pages/Login/index.tsx`
- [ ] twin.macro 적용
- [ ] useAuthStore 사용

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

#### ✅ 8-3. SignUp 페이지
**파일**: `src/pages/SignUp/index.tsx`
- [ ] 상수 import
- [ ] twin.macro 적용

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

#### ✅ 8-4. MyPage 페이지
**파일**: `src/pages/MyPage/index.tsx`
- [ ] useAuthStore 사용

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

---

### **Phase 9: ProtectedRoute 및 라우터 최종 정리** ⏱️ 1시간

#### ✅ 9-1. ProtectedRoute 컴포넌트
**파일**: `src/router/ProtectedRoute.tsx`
- [ ] 인증 확인
  ```typescript
  import { Navigate } from 'react-router-dom';
  import { useAuthStore } from '@/stores/useAuthStore';

  interface ProtectedRouteProps {
    children: React.ReactNode;
  }

  export const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
    const { isAuthenticated } = useAuthStore();

    if (!isAuthenticated) {
      return <Navigate to="/login" replace />;
    }

    return <>{children}</>;
  };
  ```

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

#### ✅ 9-2. Router 최종 업데이트
**파일**: `src/router/index.tsx`
- [ ] 모든 페이지를 새 경로로 업데이트
- [ ] ProtectedRoute 적용
  ```typescript
  import { createBrowserRouter } from 'react-router-dom';
  import { ProtectedRoute } from './ProtectedRoute';
  import LandingPage from '@/pages/Landing';
  import LoginPage from '@/pages/Login';
  import SignUpPage from '@/pages/SignUp';
  import DashboardPage from '@/pages/Dashboard';
  import CalendarPage from '@/pages/Calendar';
  import MyPage from '@/pages/MyPage';

  export const router = createBrowserRouter([
    { path: '/', element: <LandingPage /> },
    { path: '/login', element: <LoginPage /> },
    { path: '/signup', element: <SignUpPage /> },
    {
      path: '/dashboard',
      element: (
        <ProtectedRoute>
          <DashboardPage />
        </ProtectedRoute>
      ),
    },
    {
      path: '/calendar',
      element: (
        <ProtectedRoute>
          <CalendarPage />
        </ProtectedRoute>
      ),
    },
    {
      path: '/mypage',
      element: (
        <ProtectedRoute>
          <MyPage />
        </ProtectedRoute>
      ),
    },
  ]);
  ```

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

---

### **Phase 10: "use client" 제거** ⏱️ 30분

#### ✅ 10-1. 일괄 제거
- [ ] UI 컴포넌트 36개 파일에서 제거
- [ ] Git Bash에서 실행:
  ```bash
  find src/components/ui -type f -name "*.tsx" -exec sed -i "1{/^['\"]use client['\"]/d;}" {} +
  ```
- [ ] 수동 확인

**제거 대상** (36개):
- [ ] accordion.tsx
- [ ] alert-dialog.tsx
- [ ] (... 나머지 34개)

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

---

### **Phase 11: 테스트 및 검증** ⏱️ 2시간

#### ✅ 11-1. 빌드 테스트
- [ ] `npm run build`
- [ ] TypeScript 에러 해결
- [ ] 빌드 성공 확인

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

#### ✅ 11-2. 개발 서버 테스트
- [ ] `npm run dev`
- [ ] 모든 라우트 동작 확인

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

#### ✅ 11-3. 기능 검증
- [ ] Dashboard: 필터링, 검색, 정렬, 북마크, 완료
- [ ] Calendar: 주/월 뷰, 이벤트 필터링
- [ ] MyPage: 정보 수정
- [ ] Header: 알림, 프로필
- [ ] 로그인/로그아웃

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

---

### **Phase 12: 최종 정리** ⏱️ 1시간

#### ✅ 12-1. 코드 정리
- [ ] 사용하지 않는 import 제거
- [ ] console.log 제거
- [ ] Prettier 포맷팅

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

#### ✅ 12-2. 문서 업데이트
- [ ] `CLAUDE.md` 업데이트
- [ ] `README.md` 업데이트

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

#### ✅ 12-3. Git 커밋
- [ ] 변경사항 커밋
  ```bash
  git add .
  git commit -m "refactor: 프로젝트 전체 구조 리팩토링

  - React Router v6 도입
  - twin.macro (Tailwind + Emotion) 적용
  - Zustand 상태 관리
  - 페이지별 폴더 구조화
  - 공통 컴포넌트 모듈화
  - 로직 훅으로 분리
  - 타입 정의 및 상수 분리
  - \"use client\" 지시어 제거"
  ```

**이슈 기록**:
```
날짜:
작성자:
이슈:

해결:
```

---

## 📚 컨벤션 및 룰

### 1. 파일명 규칙
- **컴포넌트**: PascalCase (예: `NoticeCard.tsx`)
- **훅**: camelCase, `use` 접두사 (예: `useNoticeFilter.ts`)
- **유틸**: camelCase (예: `dateUtils.ts`)
- **스토어**: camelCase, `use` 접두사 (예: `useAuthStore.ts`)

### 2. 디자인 토큰 사용법
```typescript
import { colors, typography, spacing, borderRadius } from '@/styles/tokens';
import tw, { styled } from 'twin.macro';

// 방법 1: 디자인 토큰 직접 사용
const Container = styled.div`
  color: ${colors.text.primary};
  font-size: ${typography.fontSize.lg};
  line-height: ${typography.lineHeight.normal};
  padding: ${spacing[4]};
  border-radius: ${borderRadius.lg};
  background-color: ${colors.background.secondary};
`;

// 방법 2: twin.macro와 함께 사용
const Card = styled.div`
  ${tw`flex flex-col`}
  color: ${colors.text.secondary};
  gap: ${spacing[4]};
`;

// 방법 3: 인라인 스타일 (비권장, 특수한 경우만)
<div style={{ color: colors.brand.orange }}>

// 카테고리 색상 사용 예시
import { getCategoryColor } from '@/utils/colorUtils';

const CategoryBadge = ({ subcategory }) => {
  const color = getCategoryColor(subcategory);

  return (
    <Badge style={{
      backgroundColor: color.bg,
      color: color.text
    }}>
      {subcategory}
    </Badge>
  );
};
```

**디자인 토큰 사용 원칙**:
- ✅ **DO**: 항상 `tokens.ts`에서 색상, 간격, 타이포그래피 값 가져오기
- ✅ **DO**: 라인 높이(lineHeight)는 반드시 토큰 사용
- ✅ **DO**: 색상은 시맨틱하게 사용 (`colors.text.primary` > `colors.gray[900]`)
- ❌ **DON'T**: 하드코딩된 hex 값 사용 (`#FF6B35` 대신 `colors.brand.orange`)
- ❌ **DON'T**: 픽셀 단위 하드코딩 (`16px` 대신 `spacing[4]`)

### 3. twin.macro 사용법
```typescript
import tw, { styled, css } from 'twin.macro';

// 방법 1: tw prop (간단한 스타일)
<div tw="flex items-center gap-4">

// 방법 2: styled components (재사용)
const Container = styled.div`
  ${tw`flex flex-col gap-4 p-6`}
  background: linear-gradient(to right, #fff, #f0f0f0);
`;

// 방법 3: css prop (동적 스타일)
<div css={[tw`p-4`, isActive && tw`bg-blue-500`]}>

// 방법 4: 디자인 토큰 + twin.macro 결합
import { colors, spacing } from '@/styles/tokens';

const StyledCard = styled.div`
  ${tw`rounded-lg shadow-md`}
  padding: ${spacing[6]};
  background-color: ${colors.background.primary};
  border: 1px solid ${colors.gray[200]};
`;
```

### 4. Zustand 사용법
```typescript
// 스토어 사용
const { user, login, logout } = useAuthStore();

// 특정 값만 구독 (성능 최적화)
const user = useAuthStore((state) => state.user);
```

### 5. Import 순서
```typescript
// 1. React 및 외부 라이브러리
import { useState } from 'react';
import tw from 'twin.macro';
import { useNavigate } from 'react-router-dom';

// 2. 내부 컴포넌트
import { Header } from '@/components/layouts/Header';

// 3. 훅 및 스토어
import { useAuthStore } from '@/stores/useAuthStore';
import { useNoticeFilter } from '@/hooks/useNoticeFilter';

// 4. 유틸/상수/타입
import { getCategoryColor } from '@/utils/colorUtils';
import { CHANNEL_OPTIONS } from '@/constants/channels';
import type { Notice } from '@/types/notice';
```

---

## 🐛 이슈 트래킹

### 작업 중 발견된 이슈
*(템플릿 복사해서 사용)*

---

**이슈 #1**
- **날짜**:
- **작성자**:
- **Phase**:
- **설명**:
- **재현 방법**:
- **해결 방법**:
- **상태**: [ ] 미해결 / [ ] 해결됨

---

## 📈 진행 상황 요약

### 전체 진행도
- [x] Phase 0: 사전 준비 및 React Router 도입 (100%)
- [x] Phase 1: 기반 구조 생성 (100%)
- [x] Phase 2: Zustand 스토어 생성 (100%)
- [x] Phase 3: Mock 데이터 분리 (100%)
- [x] Phase 4: 공통 컴포넌트 생성 (100%)
  - [x] 4-1. twin.macro 완전 제거
  - [x] 4-2. Header 컴포넌트 (Header, NotificationDropdown, ProfileMenu)
  - [x] 4-3. PageLayout 컴포넌트
  - [x] 4-4. Badge 컴포넌트 (DdayBadge, CategoryBadge)
  - [x] 4-5. ImageWithFallback 마이그레이션
  - [x] 4-6. MessageDetailModal 리팩토링 (3개 하위 컴포넌트)
- [x] Phase 5: Custom Hooks 생성 (100%)
  - [x] 5-1. useNoticeFilter 훅
  - [x] 5-2. useCalendarEvents 훅
  - [x] 5-3. useDateNavigation 훅
  - [x] 5-4. useAuth 훅
- [x] Phase 6: DashboardPage 리팩토링 (100%)
  - [x] 6-1. NoticeCard, NoticeList, SearchFilterBar, MiniCalendar, JobPostingsWidget (5개 컴포넌트)
  - [x] 6-2. DashboardPage 조립 (824줄 → 130줄, 84% 감소)
  - [x] 라우터 업데이트 및 빌드 성공
- [ ] Phase 7: CalendarPage 리팩토링 (0%)
- [ ] Phase 8: 나머지 페이지 리팩토링 (0%)
- [ ] Phase 9: ProtectedRoute 및 라우터 최종 정리 (0%)
- [ ] Phase 10: "use client" 제거 (0%)
- [ ] Phase 11: 테스트 및 검증 (0%)
- [ ] Phase 12: 최종 정리 (0%)

**전체 완료율**: 54.6% (Phase 0-6 완료)

---

## ✅ 작업 완료 체크리스트

- [x] React Router 네비게이션 동작
- [x] twin.macro 스타일링 적용 (설정 완료)
- [x] Zustand 스토어 생성 완료
- [ ] 필터링/검색 기능
- [x] 모든 페이지 렌더링
- [x] TypeScript 에러 없음
- [ ] 빌드 성공
- [ ] 코드 중복 제거
- [ ] "use client" 제거
- [ ] 문서 업데이트
- [ ] Git 커밋 완료

---

**마지막 업데이트**: 2025-10-30 (Phase 0-6 완료)
**작성자**: Claude Code
**버전**: 3.2
