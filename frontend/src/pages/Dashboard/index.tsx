import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { PageLayout } from '@/components/layouts/PageLayout';
import { NoticeList } from './components/NoticeList';
import { SearchFilterBar } from './components/SearchFilterBar';
import { MiniCalendar } from './components/MiniCalendar';
import { JobPostingsWidget } from './components/JobPostingsWidget';
import { MessageDetailModal, type MessageDetail } from '@/components/modals/MessageDetailModal';
import type { Attachment } from '@/components/modals/MessageDetailModal/components/AttachmentList';
import { Card } from '@/components/ui/card';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { useFilterStore } from '@/stores/useFilterStore';
import { useNoticeFilter } from '@/hooks/useNoticeFilter';
import { getMockNotices, getMockJobPostings } from '@/services/mock';
import { bookmarksApi } from '@/services/api/bookmarks';
import type { Notice } from '@/types';

export default function DashboardPage() {
  const navigate = useNavigate();

  // Zustand 스토어
  const {
    selectedChannels,
    selectedAcademicCategories,
    selectedCareerCategories,
    searchQuery,
    periodFilter,
    sortBy,
    showBookmarkedOnly,
    toggleChannel,
    toggleAcademicCategory,
    toggleCareerCategory,
    setSearchQuery,
    setPeriodFilter,
    setSortBy,
    toggleBookmarkFilter,
    resetFilters,
  } = useFilterStore();

  // 로컬 상태
  const [notices, setNotices] = useState<Notice[]>(getMockNotices());
  const [selectedMessage, setSelectedMessage] = useState<MessageDetail | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // 필터링된 공지사항 (useNoticeFilter 훅 사용)
  const { filteredNotices } = useNoticeFilter(notices);

  // 북마크/완료 토글
  const toggleBookmark = async (id: number) => {
    const notice = notices.find((n) => n.id === id);
    if (!notice) return;

    const wasBookmarked = notice.bookmarked;

    // 1. 즉시 UI 업데이트 (낙관적 업데이트)
    setNotices((prev) =>
      prev.map((n) =>
        n.id === id ? { ...n, bookmarked: !n.bookmarked } : n
      )
    );

    console.log(`[북마크 토글] ID: ${id}, ${wasBookmarked ? '해제' : '추가'}`);

    try {
      // 2. API 호출
      await bookmarksApi.toggle(id, wasBookmarked);
      console.log(`[북마크 API] ${wasBookmarked ? '해제' : '추가'} 성공`);

      toast.success(
        wasBookmarked ? '북마크가 해제되었습니다.' : '북마크에 추가되었습니다.'
      );
    } catch (error) {
      // 3. 실패 시 롤백
      console.error('[북마크 API] 호출 실패:', error);
      setNotices((prev) =>
        prev.map((n) =>
          n.id === id ? { ...n, bookmarked: !n.bookmarked } : n
        )
      );
      toast.error('북마크 처리에 실패했습니다. 다시 시도해주세요.');
    }
  };

  const toggleComplete = (id: number) => {
    setNotices((prev) =>
      prev.map((notice) =>
        notice.id === id ? { ...notice, completed: !notice.completed } : notice
      )
    );
  };

  // 공지사항 클릭 핸들러 (모달 열기)
  const handleNoticeClick = (notice: Notice) => {
    const mattermostUrl = `https://mattermost.ssafy.com/ssafy/pl/message${notice.id}`;

    // 샘플 첨부파일 (실제로는 notice.attachments 사용)
    const attachments: Attachment[] =
      notice.id === 1
        ? [
            {
              id: 'att1',
              name: '발표자료_템플릿.pptx',
              url: '#',
              type: 'file',
              mimeType: 'application/vnd.ms-powerpoint',
            },
          ]
        : notice.id === 2
          ? [
              {
                id: 'att2',
                name: '유저테스트_안내.png',
                url: 'https://images.unsplash.com/photo-1676276375900-dd41f828c985?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9qZWN0JTIwcHJlc2VudGF0aW9uJTIwc2NoZWR1bGV8ZW58MXx8fHwxNzYxNjI0OTA2fDA&ixlib=rb-4.1.0&q=80&w=1080',
                type: 'image',
              },
            ]
          : [];

    const messageDetail: MessageDetail = {
      id: notice.id,
      title: notice.title,
      content: notice.content,
      author: notice.author,
      category: notice.category,
      subcategory: notice.subcategory,
      created_at: notice.createdAt,
      updated_at: notice.updatedAt,
      channel: notice.channel,
      dday: notice.dday,
      mattermostUrl,
      attachments: attachments.length > 0 ? attachments : undefined,
    };

    setSelectedMessage(messageDetail);
    setIsModalOpen(true);
  };

  const jobPostings = getMockJobPostings();

  return (
    <PageLayout>
      <div className="flex-1 flex gap-6 px-8 py-6">
        {/* 메인 콘텐츠 */}
        <div className="flex-1 space-y-6">
          {/* 검색 및 필터 섹션 */}
          <SearchFilterBar
            searchQuery={searchQuery}
            onSearchChange={setSearchQuery}
            selectedChannels={selectedChannels}
            onChannelToggle={toggleChannel}
            selectedAcademicCategories={selectedAcademicCategories}
            onAcademicCategoryToggle={toggleAcademicCategory}
            selectedCareerCategories={selectedCareerCategories}
            onCareerCategoryToggle={toggleCareerCategory}
            periodFilter={periodFilter}
            onPeriodChange={setPeriodFilter}
            showBookmarkedOnly={showBookmarkedOnly}
            onBookmarkFilterToggle={toggleBookmarkFilter}
            onReset={resetFilters}
          />

          {/* 공지사항 리스트 */}
          <Card className="shadow-md">
            {/* 리스트 헤더 */}
            <div className="h-12 px-6 flex items-center justify-between border-b">
              <h2 className="text-base" style={{ fontWeight: 700 }}>
                공지사항
              </h2>
              <Select value={sortBy} onValueChange={setSortBy as any}>
                <SelectTrigger className="w-[140px] h-8 text-sm">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="latest">정렬: 최신순</SelectItem>
                  <SelectItem value="deadline">정렬: 마감일순</SelectItem>
                  <SelectItem value="title">정렬: 제목순</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* 리스트 */}
            <NoticeList
              notices={filteredNotices}
              onBookmarkToggle={toggleBookmark}
              onCompleteToggle={toggleComplete}
              onNoticeClick={handleNoticeClick}
            />
          </Card>
        </div>

        {/* 우측 사이드바 */}
        <div className="w-80 space-y-6">
          {/* 미니 캘린더 */}
          <MiniCalendar onNavigateToCalendar={() => navigate('/calendar')} />

          {/* 채용 정보 위젯 */}
          <JobPostingsWidget
            postings={jobPostings}
            onViewAll={() => navigate('/jobs')}
          />
        </div>
      </div>

      {/* 메시지 상세 모달 */}
      {selectedMessage && (
        <MessageDetailModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          message={selectedMessage}
        />
      )}
    </PageLayout>
  );
}
