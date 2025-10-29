import { useState } from 'react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Badge } from './ui/badge';
import { Card } from './ui/card';
import { Checkbox } from './ui/checkbox';
import { 
  DropdownMenu, 
  DropdownMenuContent, 
  DropdownMenuItem, 
  DropdownMenuTrigger 
} from './ui/dropdown-menu';
import { 
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from './ui/select';
import { 
  Bell, 
  User, 
  Search, 
  Bookmark, 
  Check,
  Calendar as CalendarIcon,
  ChevronDown,
  Plus,
  RotateCcw,
  Briefcase,
  Info,
  Hash,
  Tag,
  GraduationCap,
  Target,
  LogOut,
  Settings
} from 'lucide-react';
import { MessageDetailModal, MessageDetail, MessageAttachment } from './MessageDetailModal';

// 타입 정의
interface NotificationItem {
  id: number;
  type: 'info' | 'danger' | 'success' | 'default';
  title: string;
  time: string;
}

interface NoticeItem {
  id: number;
  dday: number | null;
  category: string;
  subcategory: string;
  title: string;
  date: string;
  bookmarked: boolean;
  completed: boolean;
  author: string;
  content: string;
  channel: string;
  created_at: string;
  updated_at: string;
}

interface JobPosting {
  id: number;
  company: string;
  position: string;
  dday: number;
  logo: string;
}

interface DashboardPageProps {
  onLogout?: () => void;
  onNavigateToMyPage?: () => void;
  onNavigateToCalendar?: () => void;
}

export function DashboardPage({ onLogout, onNavigateToMyPage, onNavigateToCalendar }: DashboardPageProps) {
  // 알림 드롭다운 상태
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);

  // 모달 상태
  const [selectedMessage, setSelectedMessage] = useState<MessageDetail | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // 필터 상태
  const [searchQuery, setSearchQuery] = useState('');
  const [periodFilter, setPeriodFilter] = useState('전체');
  const [selectedChannels, setSelectedChannels] = useState<string[]>([
    '13기-공지사항',
    '13기-취업공고'
  ]);
  const [selectedAcademicCategories, setSelectedAcademicCategories] = useState<string[]>([
    '할일', '특강', '정보', '이벤트'
  ]);
  const [selectedCareerCategories, setSelectedCareerCategories] = useState<string[]>([
    '할일', '특강', '정보', '이벤트'
  ]);
  const [sortBy, setSortBy] = useState('관련도순');

  // 알림 데이터
  const notifications: NotificationItem[] = [
    { id: 1, type: 'info', title: '새 공지: 프로젝트 제출 일정 변경', time: '10분 전' },
    { id: 2, type: 'danger', title: '마감 임박: 내일까지 서류 제출', time: '1시간 전' },
    { id: 3, type: 'success', title: '관심 직무: 네이버 백엔드 채용', time: '3시간 전' },
    { id: 4, type: 'info', title: '구독 키워드: \'특강\' 관련 새 공지', time: '5시간 전' },
    { id: 5, type: 'default', title: '새 공지: MT 장소 투표 시작', time: '어제' },
  ];

  // 공지사항 데이터
  const [notices, setNotices] = useState<NoticeItem[]>([
    { 
      id: 1, 
      dday: 1, 
      category: '학사', 
      subcategory: '할일', 
      title: '프로젝트 중간발표 PPT 제출', 
      date: '2024.10.27', 
      bookmarked: false, 
      completed: false,
      author: '김교육 (교육 매니저)',
      content: '안녕하세요, 교육생 여러분!\n\n다음 주 월요일까지 프로젝트 중간발표 PPT를 제출해주세요.\n\n제출 기한: 2024년 10월 28일 18:00까지\n제출 방법: 프로젝트 제출 게시판에 업로드\n\n발표 자료에는 다음 내용이 포함되어야 합니다:\n1. 프로젝트 개요 및 목표\n2. 현재까지 진행 상황\n3. 기술 스택 및 아키텍처\n4. 향후 개발 계획\n5. Q&A\n\n문의사항이 있으시면 언제든지 연락주세요.\n감사합니다.',
      channel: '13기-공지사항',
      created_at: '2024.10.27 09:30',
      updated_at: '2024.10.27 09:30'
    },
    { 
      id: 2, 
      dday: 3, 
      category: '학사', 
      subcategory: '할일', 
      title: '[13기 자율 프로젝트 유저 테스트 시행 안내]', 
      date: '2024.10.28', 
      bookmarked: true, 
      completed: false,
      author: 'SSAFY 사무국',
      content: '삼성전자 SW 직무 채용설명회를 개최합니다!\n\n일시: 2024년 10월 30일 14:00 - 16:00\n장소: 대강당\n신청 기한: 2024년 10월 28일까지\n\n이번 설명회에서는:\n- 삼성전자 SW 직군 소개\n- 채용 프로세스 안내\n- 현직자 Q&A 세션\n- 코딩테스트 준비 팁\n\n참석을 원하시는 분들은 구글 폼을 통해 신청해주시기 바랍니다.\n선착순 100명까지 신청 가능합니다.\n\n많은 참여 부탁드립니다!',
      channel: '13기-취업공고',
      created_at: '2024.10.25 10:00',
      updated_at: '2024.10.25 15:30'
    },
    { 
      id: 3, 
      dday: 7, 
      category: '학사', 
      subcategory: '할일', 
      title: '관통프로젝트 1차 제출', 
      date: '2024.10.22', 
      bookmarked: false, 
      completed: false,
      author: '박프로 (프로젝트 담당)',
      content: '관통프로젝트 1차 제출 안내입니다.\n\n제출 기한: 2024년 11월 3일 23:59까지\n\n제출 내용:\n- 프로젝트 기획서 (필수)\n- ERD 다이어그램 (필수)\n- 와이어프레임 (필수)\n- API 명세서 (선택)\n\n제출 형식: PDF 파일로 통합하여 제출\n제출 위치: GitLab Repository README에 링크 추가\n\n평가 기준:\n- 기획의 완성도 (30%)\n- 기술적 타당성 (30%)\n- 창의성 (20%)\n- 문서 품질 (20%)\n\n문의사항은 Mattermost로 연락주세요.',
      channel: '13기-공지사항',
      created_at: '2024.10.22 14:20',
      updated_at: '2024.10.22 14:20'
    },
    { 
      id: 4, 
      dday: null, 
      category: '취업', 
      subcategory: '정보', 
      title: '2025 상반기 공채 일정 총정리', 
      date: '2024.10.20', 
      bookmarked: false, 
      completed: false,
      author: '최취업 (취업 지원팀)',
      content: '2025년 상반기 주요 기업 공채 일정을 정리했습니다.\n\n[대기업]\n- 삼성: 2025년 1월 접수 시작\n- LG: 2025년 2월 예정\n- SK: 2024년 12월 하반기 공채\n- 현대자동차: 2025년 1월 예정\n\n[IT 기업]\n- 네이버: 수시 채용 진행 중\n- 카카오: 2024년 12월 공채\n- 쿠팡: 상시 채용\n- 배민: 2025년 1월 예정\n\n[공공기관]\n- NHN: 2025년 2월\n- 우아한형제들: 2024년 12월\n\n각 회사별 상세 일정은 별도 공지 예정입니다.\n관심 있는 기업의 채용 공고를 꼭 확인하세요!',
      channel: '13기-취업정보',
      created_at: '2024.10.20 11:00',
      updated_at: '2024.10.20 11:00'
    },
    { 
      id: 5, 
      dday: null, 
      category: '학사', 
      subcategory: '이벤트', 
      title: '13기 MT 장소 투표', 
      date: '2024.10.19', 
      bookmarked: false, 
      completed: false,
      author: '정이벤 (학생회)',
      content: '13기 MT 장소 투표를 시작합니다!\n\n투표 기간: 2024.10.19 - 10.25\n\n후보지:\n1. 강원도 평창 리조트\n2. 경기도 가평 펜션\n3. 충청도 보령 해수욕장\n4. 전라도 순천만 자연휴양림\n\n각 장소별 특징:\n- 평창: 겨울 스포츠 가능, 넓은 공간\n- 가평: 접근성 좋음, 다양한 액티비티\n- 보령: 해변 산책, 신선한 해산물\n- 순천: 자연 경관, 힐링\n\n투표 링크: [구글 폼 링크]\n\n여러분의 소중한 의견을 들려주세요!',
      channel: '13기-공지사항',
      created_at: '2024.10.19 16:45',
      updated_at: '2024.10.19 16:45'
    },
    { 
      id: 6, 
      dday: null, 
      category: '취업', 
      subcategory: '정보', 
      title: '코딩테스트 준비 가이드', 
      date: '2024.10.18', 
      bookmarked: true, 
      completed: true,
      author: '한코딩 (알고리즘 강사)',
      content: '효과적인 코딩테스트 준비 방법을 공유합니다.\n\n[기초 다지기 (1-2개월)]\n1. 자료구조 완벽 이해\n   - 배열, 연결리스트, 스택, 큐\n   - 트리, 그래프, 해시\n\n2. 기본 알고리즘\n   - 정렬, 탐색\n   - DFS, BFS\n   - 동적 프로그래밍 기초\n\n[실전 준비 (2-3개월)]\n1. 문제 풀이 (매일 2-3문제)\n   - 백준, 프로그래머스 활용\n   - 난이도: 실버 → 골드 → 플래티넘\n\n2. 기업별 기출 문제 풀이\n   - 삼성, 카카오, 네이버 등\n\n[추천 학습 자료]\n- 책: "이것이 코딩 테스트다"\n- 사이트: 백준, 프로그래머스, LeetCode\n- 유튜브: 동빈나, 엔지니어 대한민국\n\n꾸준함이 가장 중요합니다. 화이팅!',
      channel: '13기-취업정보',
      created_at: '2024.10.18 09:15',
      updated_at: '2024.10.18 13:20'
    },
  ]);

  // 채용공고 데이터
  const jobPostings: JobPosting[] = [
    { id: 1, company: '삼성전자', position: 'SW 개발', dday: 5, logo: '💼' },
    { id: 2, company: '네이버', position: '백엔드 개발자', dday: 12, logo: '💼' },
    { id: 3, company: '카카오', position: '프론트엔드 개발자', dday: 8, logo: '💼' },
  ];

  // 채널 옵션
  const channelOptions = ['전체', '13기-공지사항', '13기-취업공고', '13기-취업정보', '서울1반-공지사항'];

  // D-day 배지 색상
  const getDdayBadgeColor = (dday: number | null): string => {
    if (dday === null) return 'bg-gray-400 text-white';
    if (dday >= 1 && dday <= 3) return 'bg-red-500 text-white';
    if (dday >= 4 && dday <= 7) return 'bg-yellow-500 text-white';
    return 'bg-green-500 text-white';
  };

  // 카테고리 색상 (모달과 동일하게)
  const getCategoryColor = (subcategory: string): string => {
    const colors: Record<string, string> = {
      '할일': 'bg-red-100 text-red-700 border-red-300',
      '특강': 'bg-blue-100 text-blue-700 border-blue-300',
      '정보': 'bg-green-100 text-green-700 border-green-300',
      '이벤트': 'bg-purple-100 text-purple-700 border-purple-300',
    };
    return colors[subcategory] || 'bg-gray-100 text-gray-700 border-gray-300';
  };

  // 알림 아이콘 및 색상
  const getNotificationIcon = (type: string) => {
    const config: Record<string, { icon: React.ReactNode; color: string }> = {
      info: { icon: <Info className="w-5 h-5" />, color: 'text-blue-500' },
      danger: { icon: <Info className="w-5 h-5" />, color: 'text-red-500' },
      success: { icon: <Briefcase className="w-5 h-5" />, color: 'text-green-500' },
      default: { icon: <Info className="w-5 h-5" />, color: 'text-gray-400' },
    };
    return config[type] || config.default;
  };

  // 필터 토글
  const toggleChannel = (channel: string) => {
    if (channel === '전체') {
      setSelectedChannels(selectedChannels.length === channelOptions.length - 1 ? [] : channelOptions.filter(c => c !== '전체'));
    } else {
      setSelectedChannels(prev =>
        prev.includes(channel) ? prev.filter(c => c !== channel) : [...prev, channel]
      );
    }
  };

  const toggleAcademicCategory = (category: string) => {
    setSelectedAcademicCategories(prev =>
      prev.includes(category) ? prev.filter(c => c !== category) : [...prev, category]
    );
  };

  const toggleCareerCategory = (category: string) => {
    setSelectedCareerCategories(prev =>
      prev.includes(category) ? prev.filter(c => c !== category) : [...prev, category]
    );
  };

  const resetFilters = () => {
    setPeriodFilter('전체');
    setSelectedChannels(['13기-공지사항', '13기-취업공고']);
    setSelectedAcademicCategories(['할일', '특강', '정보', '이벤트']);
    setSelectedCareerCategories(['할일', '특강', '정보', '이벤트']);
  };

  const toggleBookmark = (id: number) => {
    setNotices(prev => prev.map(n => n.id === id ? { ...n, bookmarked: !n.bookmarked } : n));
  };

  const toggleComplete = (id: number) => {
    setNotices(prev => prev.map(n => n.id === id ? { ...n, completed: !n.completed } : n));
  };

  const handleNoticeClick = (notice: NoticeItem) => {
    // 더미 데이터: Mattermost URL과 첨부파일
    const mattermostUrl = `https://mattermost.ssafy.com/ssafy/pl/message${notice.id}`;
    
    const attachments: MessageAttachment[] = notice.id === 1 ? [
      {
        id: 'att1',
        name: '발표자료_템플릿.pptx',
        url: '#',
        type: 'file',
        mimeType: 'application/vnd.ms-powerpoint'
      }
    ] : notice.id === 2 ? [
      {
        id: 'att2',
        name: '유저테스트_안내.png',
        url: 'https://images.unsplash.com/photo-1676276375900-dd41f828c985?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9qZWN0JTIwcHJlc2VudGF0aW9uJTIwc2NoZWR1bGV8ZW58MXx8fHwxNzYxNjI0OTA2fDA&ixlib=rb-4.1.0&q=80&w=1080',
        type: 'image'
      }
    ] : [];

    const messageDetail: MessageDetail = {
      id: notice.id,
      title: notice.title,
      content: notice.content,
      author: notice.author,
      category: notice.category,
      subcategory: notice.subcategory,
      created_at: notice.created_at,
      updated_at: notice.updated_at,
      channel: notice.channel,
      dday: notice.dday,
      mattermostUrl,
      attachments: attachments.length > 0 ? attachments : undefined,
    };
    setSelectedMessage(messageDetail);
    setIsModalOpen(true);
  };

  const handleNotificationClick = (notif: NotificationItem) => {
    // 알림에서 해당하는 공지사항 찾기
    const relatedNotice = notices.find(n => notif.title.includes(n.title.slice(0, 10)));
    if (relatedNotice) {
      handleNoticeClick(relatedNotice);
    }
    setNotificationOpen(false);
  };

  // 미니 캘린더 생성 (10월 2024)
  const generateCalendar = () => {
    const daysInMonth = 31;
    const firstDay = 2; // 2024년 10월 1일은 화요일
    const calendar = [];
    let day = 1;

    for (let week = 0; week < 5; week++) {
      const weekDays = [];
      for (let weekday = 0; weekday < 7; weekday++) {
        if ((week === 0 && weekday < firstDay) || day > daysInMonth) {
          weekDays.push(null);
        } else {
          weekDays.push(day);
          day++;
        }
      }
      calendar.push(weekDays);
    }
    return calendar;
  };

  const calendar = generateCalendar();
  const today = 27;
  const daysWithDeadlines = [27, 29, 3]; // 예시

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 헤더 */}
      <header className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-8">
        {/* 로고 */}
        <div className="flex items-center gap-2">
          <div className="w-10 h-10 rounded-xl bg-[var(--brand-orange)] flex items-center justify-center">
            <span className="text-xl">🐹</span>
          </div>
          <span className="text-xl" style={{ fontWeight: 700 }}>편리햄!</span>
        </div>

        {/* 우측 액션 */}
        <div className="flex items-center gap-4">
          {/* 알림 드롭다운 */}
          <DropdownMenu open={notificationOpen} onOpenChange={setNotificationOpen}>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" size="icon" className="relative">
                <Bell className="w-5 h-5" />
                <span className="absolute -top-1 -right-1 w-5 h-5 bg-red-500 text-white text-xs rounded-full flex items-center justify-center">
                  {notifications.length}
                </span>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-[360px] max-h-[400px] overflow-y-auto p-0">
              <div className="p-4 border-b">
                <h3 className="text-base" style={{ fontWeight: 700 }}>알림</h3>
              </div>
              <div>
                {notifications.map((notif) => {
                  const iconConfig = getNotificationIcon(notif.type);
                  const borderColor = 
                    notif.type === 'info' ? 'border-l-blue-500' :
                    notif.type === 'danger' ? 'border-l-red-500' :
                    notif.type === 'success' ? 'border-l-green-500' :
                    'border-l-gray-500';
                  
                  return (
                    <div
                      key={notif.id}
                      onClick={() => handleNotificationClick(notif)}
                      className={`flex items-center gap-3 p-4 hover:bg-gray-50 transition-colors border-b border-l-4 last:border-b-0 cursor-pointer ${borderColor}`}
                    >
                      <div className={`flex items-center justify-center flex-shrink-0 ${iconConfig.color}`}>
                        {iconConfig.icon}
                      </div>
                      <div className="flex-1 min-w-0">
                        <p className="text-sm" style={{ fontWeight: 500 }}>{notif.title}</p>
                        <p className="text-xs text-gray-500 mt-1">{notif.time}</p>
                      </div>
                    </div>
                  );
                })}
              </div>
            </DropdownMenuContent>
          </DropdownMenu>

          {/* 프로필 드롭다운 */}
          <DropdownMenu open={profileOpen} onOpenChange={setProfileOpen}>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" size="icon" className="rounded-full">
                <div className="w-8 h-8 rounded-full bg-[var(--brand-orange)] flex items-center justify-center">
                  <User className="w-5 h-5 text-white" />
                </div>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-48">
              <DropdownMenuItem 
                onClick={() => {
                  setProfileOpen(false);
                  onNavigateToMyPage?.();
                }}
                className="cursor-pointer"
              >
                <Settings className="w-4 h-4 mr-2" />
                마이페이지
              </DropdownMenuItem>
              <DropdownMenuItem 
                onClick={() => {
                  setProfileOpen(false);
                  onLogout?.();
                }}
                className="cursor-pointer text-red-600"
              >
                <LogOut className="w-4 h-4 mr-2" />
                로그아웃
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </header>

      {/* 메인 컨테이너 */}
      <div className="flex gap-6 p-8 max-w-[1800px] mx-auto">
        {/* 메인 콘텐츠 영역 (70%) */}
        <div className="flex-[7]">
          {/* 검색 및 필터 섹션 */}
          <Card className="p-5 mb-6 shadow-md">
            {/* 검색바 */}
            <div className="flex gap-2 mb-4">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <Input
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  placeholder="공지사항, 채널, 키워드 검색..."
                  className="pl-10 h-12 border-0 bg-gray-50 rounded-lg"
                />
              </div>
              <Button className="h-12 px-6 bg-[var(--brand-orange)] hover:bg-[var(--brand-orange-dark)] text-white">
                <Search className="w-4 h-4 mr-2" />
                검색
              </Button>
            </div>

            {/* 기간 필터 */}
            <div className="flex items-center gap-4 mb-2 min-h-[40px]">
              <span className="text-xs whitespace-nowrap flex items-center gap-1.5" style={{ fontWeight: 700 }}>
                <CalendarIcon className="w-3.5 h-3.5" />
                기간
              </span>
              <div className="flex gap-2">
                {['전체', '오늘', '이번주', '이번달'].map((period) => (
                  <Button
                    key={period}
                    variant={periodFilter === period ? 'default' : 'outline'}
                    size="sm"
                    onClick={() => setPeriodFilter(period)}
                    className={`h-8 px-4 rounded-md ${
                      periodFilter === period
                        ? 'bg-[var(--brand-orange)] text-white hover:bg-[var(--brand-orange-dark)]'
                        : 'bg-white hover:bg-gray-50'
                    }`}
                    style={{ fontWeight: 500 }}
                  >
                    {period}
                  </Button>
                ))}
                <Select value={periodFilter} onValueChange={setPeriodFilter}>
                  <SelectTrigger className="h-8 w-[120px] text-sm">
                    <SelectValue placeholder="기간 설정" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="custom">기간 설정</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>

            {/* 채널 필터 */}
            <div className="flex items-center gap-4 mb-2 min-h-[40px]">
              <span className="text-xs whitespace-nowrap flex items-center gap-1.5" style={{ fontWeight: 700 }}>
                <Hash className="w-3.5 h-3.5" />
                채널
              </span>
              <div className="flex gap-2 flex-wrap">
                {channelOptions.map((channel) => {
                  const isSelected = channel === '전체' 
                    ? selectedChannels.length === channelOptions.length - 1
                    : selectedChannels.includes(channel);
                  return (
                    <Button
                      key={channel}
                      variant="outline"
                      size="sm"
                      onClick={() => toggleChannel(channel)}
                      className={`h-8 px-4 rounded-md ${
                        isSelected
                          ? 'bg-[var(--brand-orange)] text-white border-[var(--brand-orange)] hover:bg-[var(--brand-orange-dark)]'
                          : 'bg-white hover:bg-gray-50'
                      }`}
                      style={{ fontWeight: 500 }}
                    >
                      {isSelected && <Check className="w-3 h-3 mr-1" />}
                      {channel}
                    </Button>
                  );
                })}
              </div>
            </div>

            {/* 카테고리 필터 */}
            <div className="flex items-center gap-4 min-h-[40px]">
              <span className="text-xs whitespace-nowrap flex items-center gap-1.5" style={{ fontWeight: 700 }}>
                <Tag className="w-3.5 h-3.5" />
                카테고리
              </span>
              <div className="flex items-center gap-2 flex-1">
                {/* 학사 */}
                <span className="text-xs text-gray-600 flex items-center gap-1" style={{ fontWeight: 500 }}>
                  <GraduationCap className="w-3.5 h-3.5" />
                  학사:
                </span>
                <div className="flex gap-2">
                  {['할일', '특강', '정보', '이벤트'].map((cat) => {
                    const isSelected = selectedAcademicCategories.includes(cat);
                    return (
                      <Button
                        key={`academic-${cat}`}
                        variant="outline"
                        size="sm"
                        onClick={() => toggleAcademicCategory(cat)}
                        className={`h-8 px-3 rounded-md border ${
                          isSelected
                            ? getCategoryColor(cat)
                            : 'bg-white hover:bg-gray-50 border-gray-200 text-gray-600'
                        }`}
                        style={{ fontWeight: 500 }}
                      >
                        {isSelected && <Check className="w-3 h-3 mr-1" />}
                        {cat}
                      </Button>
                    );
                  })}
                </div>

                {/* 구분선 */}
                <div className="h-6 w-px bg-gray-300 mx-2" />

                {/* 취업 */}
                <span className="text-xs text-gray-600 flex items-center gap-1" style={{ fontWeight: 500 }}>
                  <Briefcase className="w-3.5 h-3.5" />
                  취업:
                </span>
                <div className="flex gap-2">
                  {['할일', '특강', '정보', '이벤트'].map((cat) => {
                    const isSelected = selectedCareerCategories.includes(cat);
                    return (
                      <Button
                        key={`career-${cat}`}
                        variant="outline"
                        size="sm"
                        onClick={() => toggleCareerCategory(cat)}
                        className={`h-8 px-3 rounded-md border ${
                          isSelected
                            ? getCategoryColor(cat)
                            : 'bg-white hover:bg-gray-50 border-gray-200 text-gray-600'
                        }`}
                        style={{ fontWeight: 500 }}
                      >
                        {isSelected && <Check className="w-3 h-3 mr-1" />}
                        {cat}
                      </Button>
                    );
                  })}
                </div>
              </div>

              {/* 필터 초기화 */}
              <Button
                variant="outline"
                size="sm"
                onClick={resetFilters}
                className="h-8 px-4 rounded-md ml-auto whitespace-nowrap"
                style={{ fontWeight: 500 }}
              >
                <RotateCcw className="w-3 h-3 mr-2" />
                필터 초기화
              </Button>
            </div>
          </Card>

          {/* 공지사항 리스트 */}
          <Card className="shadow-md">
            {/* 리스트 헤더 */}
            <div className="h-12 px-6 flex items-center justify-between border-b">
              <h2 className="text-base" style={{ fontWeight: 700 }}>공지사항</h2>
              <Select value={sortBy} onValueChange={setSortBy}>
                <SelectTrigger className="w-[140px] h-8 text-sm">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="관련도순">정렬: 관련도순</SelectItem>
                  <SelectItem value="최신순">정렬: 최신순</SelectItem>
                  <SelectItem value="오래된순">정렬: 오래된순</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* 리스트 아이템 */}
            <div>
              {notices.map((notice) => (
                <div
                  key={notice.id}
                  className={`px-6 py-4 border-b last:border-b-0 hover:bg-gray-50 transition-colors ${
                    notice.completed ? 'opacity-60' : ''
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <div 
                      className="flex items-center gap-3 flex-1 min-w-0 cursor-pointer"
                      onClick={() => handleNoticeClick(notice)}
                    >
                      {/* D-day 배지 */}
                      {notice.dday !== null && (
                        <Badge className={`${getDdayBadgeColor(notice.dday)} px-2 py-0.5 text-xs shrink-0`}>
                          D-{notice.dday}
                        </Badge>
                      )}
                      
                      {/* 카테고리 라벨 */}
                      <Badge variant="secondary" className="bg-gray-100 text-gray-700 px-2 py-0.5 text-xs rounded shrink-0">
                        {notice.category}-{notice.subcategory}
                      </Badge>

                      {/* 제목 */}
                      <span className="text-sm truncate" style={{ fontWeight: 500 }}>
                        {notice.title}
                      </span>
                    </div>

                    {/* 우측 액션 */}
                    <div className="flex items-center gap-3 ml-4">
                      <Button
                        variant="ghost"
                        size="icon"
                        className="w-8 h-8"
                        onClick={(e) => {
                          e.stopPropagation();
                          toggleBookmark(notice.id);
                        }}
                      >
                        <Bookmark
                          className={`w-4 h-4 ${notice.bookmarked ? 'fill-yellow-400 text-yellow-400' : 'text-gray-400'}`}
                        />
                      </Button>
                      <div onClick={(e) => e.stopPropagation()}>
                        <Checkbox
                          checked={notice.completed}
                          onCheckedChange={() => toggleComplete(notice.id)}
                          className="w-5 h-5"
                        />
                      </div>
                    </div>
                  </div>

                  {/* 날짜 */}
                  <div className="mt-2 ml-0">
                    <span className="text-xs text-gray-500">{notice.date}</span>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </div>

        {/* 우측 사이드바 (30%) */}
        <div className="flex-[3] space-y-4">
          {/* 마감 캘린더 위젯 */}
          <Card className="p-5 shadow-md">
            {/* 헤더 */}
            <div className="flex items-center justify-between mb-3 pb-3 border-b">
              <h3 className="text-sm flex items-center gap-2" style={{ fontWeight: 700 }}>
                <CalendarIcon className="w-4 h-4" />
                마감 캘린더
              </h3>
              <Button 
                variant="ghost" 
                size="icon" 
                className="w-6 h-6"
                onClick={onNavigateToCalendar}
              >
                <Plus className="w-4 h-4" />
              </Button>
            </div>

            {/* 미니 캘린더 */}
            <div className="mb-4">
              <div className="text-center text-xs mb-2 text-gray-600" style={{ fontWeight: 600 }}>
                2024년 10월
              </div>
              <div className="grid grid-cols-7 gap-1">
                {/* 요일 헤더 */}
                {['일', '월', '화', '수', '목', '금', '토'].map((day) => (
                  <div key={day} className="text-center text-[10px] text-gray-500 h-6 flex items-center justify-center">
                    {day}
                  </div>
                ))}
                
                {/* 날짜 */}
                {calendar.map((week, weekIdx) => (
                  week.map((day, dayIdx) => (
                    <div
                      key={`${weekIdx}-${dayIdx}`}
                      className={`h-8 flex flex-col items-center justify-center text-xs relative ${
                        day === today
                          ? 'bg-[var(--brand-orange)] text-white rounded-full'
                          : day
                          ? 'text-gray-700'
                          : 'text-transparent'
                      }`}
                    >
                      {day}
                      {day && daysWithDeadlines.includes(day) && day !== today && (
                        <div className="absolute bottom-0.5 w-1 h-1 bg-red-500 rounded-full" />
                      )}
                    </div>
                  ))
                ))}
              </div>
            </div>

            {/* D-day 미니 리스트 */}
            <div className="pt-4 border-t space-y-2">
              {notices.slice(0, 3).map((notice) => (
                notice.dday !== null && (
                  <div key={notice.id} className="flex items-center gap-2">
                    <Badge className={`${getDdayBadgeColor(notice.dday)} px-1.5 py-0 text-[10px] shrink-0`}>
                      D-{notice.dday}
                    </Badge>
                    <span className="text-xs text-gray-700 truncate" style={{ fontWeight: 500 }}>
                      {notice.title}
                    </span>
                  </div>
                )
              ))}
            </div>
          </Card>

          {/* 맞춤 채용공고 섹션 */}
          <Card className="p-5 shadow-md">
            <h3 className="text-sm mb-3 flex items-center gap-2" style={{ fontWeight: 700 }}>
              <Target className="w-4 h-4" />
              맞춤 채용공고
            </h3>

            <div className="space-y-3">
              {jobPostings.map((job) => (
                <Card key={job.id} className="p-3 hover:shadow-md transition-shadow cursor-pointer border">
                  <div className="flex items-center gap-3">
                    {/* 회사 로고 */}
                    <div className="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center text-2xl shrink-0">
                      {job.logo}
                    </div>

                    {/* 정보 */}
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2">
                        <div className="min-w-0 flex-1">
                          <h4 className="text-sm truncate" style={{ fontWeight: 700 }}>
                            {job.company}
                          </h4>
                          <p className="text-xs text-gray-600 truncate">{job.position}</p>
                        </div>
                        <Badge className={`${getDdayBadgeColor(job.dday)} px-1.5 py-0 text-[10px] shrink-0`}>
                          D-{job.dday}
                        </Badge>
                      </div>
                    </div>
                  </div>
                </Card>
              ))}
            </div>
          </Card>
        </div>
      </div>

      {/* 메시지 상세보기 모달 */}
      <MessageDetailModal
        message={selectedMessage}
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </div>
  );
}
