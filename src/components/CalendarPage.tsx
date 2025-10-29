import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Card } from "./ui/card";
import { Checkbox } from "./ui/checkbox";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";
import {
  Bell,
  User,
  ChevronLeft,
  ChevronRight,
  ChevronDown,
  Calendar as CalendarIcon,
  Settings,
  LogOut,
  X,
  Hash,
  GraduationCap,
  Briefcase,
  ExternalLink,
} from "lucide-react";
import {
  MessageDetailModal,
  MessageDetail,
} from "./MessageDetailModal";

// 타입 정의
interface CalendarEvent {
  id: number;
  title: string;
  startDate: Date;
  endDate: Date;
  startTime?: string;
  endTime?: string;
  description?: string;
  location?: string;
  channel: string;
  category: string; // '학사' or '취업'
  subcategory: string; // '할일', '특강', '정보', '이벤트'
}

export function CalendarPage() {
  const navigate = useNavigate();
  // 알림 드롭다운 상태
  const [notificationOpen, setNotificationOpen] =
    useState(false);
  const [profileOpen, setProfileOpen] = useState(false);

  // 채널 필터 접기/펴기 상태
  const [channelExpanded, setChannelExpanded] = useState(true);

  // 모달 상태
  const [selectedMessage, setSelectedMessage] =
    useState<MessageDetail | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // 뷰 상태
  const [viewMode, setViewMode] = useState<"week" | "month">(
    "week",
  );

  // 날짜 상태
  const [currentDate, setCurrentDate] = useState(new Date());

  // 필터 상태
  const [selectedChannels, setSelectedChannels] = useState<
    string[]
  >([
    "전체",
    "13기-공지사항",
    "13기-취업공지",
    "13기-취업정보",
    "서울1반-공지사항",
  ]);
  const [
    selectedAcademicCategories,
    setSelectedAcademicCategories,
  ] = useState<string[]>(["할일", "특강", "정보", "이벤트"]);
  const [
    selectedCareerCategories,
    setSelectedCareerCategories,
  ] = useState<string[]>(["할일", "특강", "정보", "이벤트"]);

  // 예제 이벤트 데이터
  const events: CalendarEvent[] = [
    // 10월 21일 이벤트 (4개)
    {
      id: 1,
      title: "알고리즘 스터디",
      startDate: new Date(2025, 9, 21),
      endDate: new Date(2025, 9, 21),
      startTime: "19:00",
      endTime: "21:00",
      description: "백준 문제 풀이",
      channel: "13기-공지사항",
      category: "학사",
      subcategory: "정보",
    },
    {
      id: 2,
      title: "프로젝트 기획 발표",
      startDate: new Date(2025, 9, 21),
      endDate: new Date(2025, 9, 21),
      startTime: "14:00",
      endTime: "16:00",
      description: "팀별 아이디어 발표",
      channel: "13기-공지사항",
      category: "학사",
      subcategory: "할일",
    },
    {
      id: 3,
      title: "삼성 채용설명회",
      startDate: new Date(2025, 9, 21),
      endDate: new Date(2025, 9, 21),
      startTime: "13:00",
      endTime: "15:00",
      description: "DS부문 신입 채용",
      channel: "13기-취업정보",
      category: "취업",
      subcategory: "이벤트",
    },
    {
      id: 11,
      title: "코딩테스트 대비 특강",
      startDate: new Date(2025, 9, 21),
      endDate: new Date(2025, 9, 21),
      startTime: "16:00",
      endTime: "18:00",
      description: "자료구조 심화",
      channel: "13기-공지사항",
      category: "학사",
      subcategory: "특강",
    },
    // 10월 27일 이벤트 (2개)
    {
      id: 4,
      title: "Vue.js 심화 특강",
      startDate: new Date(2025, 9, 27),
      endDate: new Date(2025, 9, 27),
      startTime: "10:00",
      endTime: "12:00",
      description: "Composition API 실습",
      channel: "13기-공지사항",
      category: "학사",
      subcategory: "특강",
    },
    {
      id: 5,
      title: "네트워킹 데이",
      startDate: new Date(2025, 9, 27),
      endDate: new Date(2025, 9, 27),
      startTime: "18:00",
      endTime: "20:00",
      description: "선배 개발자와의 만남",
      channel: "서울1반-공지사항",
      category: "학사",
      subcategory: "이벤트",
    },
    // 10월 28일 이벤트 (3개)
    {
      id: 6,
      title: "AI 실습특강 III",
      startDate: new Date(2025, 9, 28),
      endDate: new Date(2025, 9, 28),
      startTime: "13:00",
      endTime: "17:00",
      description: "서전 환경 셋팅 필요",
      channel: "13기-공지사항",
      category: "학사",
      subcategory: "특강",
    },
    {
      id: 7,
      title: "코드 리뷰 세션",
      startDate: new Date(2025, 9, 28),
      endDate: new Date(2025, 9, 28),
      startTime: "15:00",
      endTime: "17:00",
      description: "프로젝트 코드 리뷰",
      channel: "13기-공지사항",
      category: "학사",
      subcategory: "정보",
    },
    {
      id: 8,
      title: "이력서 작성법 특강",
      startDate: new Date(2025, 9, 28),
      endDate: new Date(2025, 9, 28),
      startTime: "14:00",
      endTime: "16:00",
      description: "IT 이력서 작성 팁",
      channel: "13기-취업공지",
      category: "취업",
      subcategory: "특강",
    },
    // 10월 30일 이벤트 (2개)
    {
      id: 9,
      title: "프로젝트 중간 발표",
      startDate: new Date(2025, 9, 30),
      endDate: new Date(2025, 9, 30),
      startTime: "14:00",
      endTime: "18:00",
      description: "팀별 진행상황 발표",
      channel: "13기-공지사항",
      category: "학사",
      subcategory: "할일",
    },
    {
      id: 10,
      title: "포트폴리오 제출",
      startDate: new Date(2025, 9, 30),
      endDate: new Date(2025, 9, 30),
      description: "온라인 제출 마감",
      channel: "13기-취업공지",
      category: "취업",
      subcategory: "할일",
    },
  ];

  // 알림 데이터
  const notifications = [
    {
      id: 1,
      type: "info" as const,
      title: "[중요] AI 실습특강 III 오늘 13시",
      time: "1시간 전",
    },
    {
      id: 2,
      type: "danger" as const,
      title: "프로젝트 중간발표 내일 마감",
      time: "2시간 전",
    },
    {
      id: 3,
      type: "success" as const,
      title: "이력서 피드백 완료",
      time: "3시간 전",
    },
    {
      id: 4,
      type: "default" as const,
      title: "새로운 채용공고 2건",
      time: "5시간 전",
    },
  ];

  // 채널 옵션
  const channelOptions = [
    "전체",
    "13기-공지사항",
    "13기-취업공지",
    "13기-취업정보",
    "서울1반-공지사항",
  ];

  // 카테고리 색상 (대시보드와 통일)
  const getCategoryColor = (
    category: string,
    subcategory: string,
  ) => {
    // 학사/취업 구분 없이 서브카테고리로만 구분 (대시보드와 동일)
    switch (subcategory) {
      case "할일":
        return "bg-red-100 text-red-700 border-red-300";
      case "특강":
        return "bg-blue-100 text-blue-700 border-blue-300";
      case "정보":
        return "bg-green-100 text-green-700 border-green-300";
      case "이벤트":
        return "bg-purple-100 text-purple-700 border-purple-300";
      default:
        return "bg-gray-100 text-gray-700 border-gray-200";
    }
  };

  // 카테고리 버튼 색상 (대시보드와 통일)
  const getCategoryButtonColor = (
    subcategory: string,
    isSelected: boolean,
  ) => {
    if (!isSelected)
      return "bg-white text-gray-600 border-gray-200";

    // 학사/취업 구분 없이 동일한 색상 적용 (대시보드와 동일)
    switch (subcategory) {
      case "할일":
        return "bg-red-100 text-red-700 border-red-300";
      case "특강":
        return "bg-blue-100 text-blue-700 border-blue-300";
      case "정보":
        return "bg-green-100 text-green-700 border-green-300";
      case "이벤트":
        return "bg-purple-100 text-purple-700 border-purple-300";
      default:
        return "bg-gray-100 text-gray-700 border-gray-200";
    }
  };

  // 주의 시작일 구하기 (일요일)
  const getWeekStart = (date: Date): Date => {
    const d = new Date(date);
    const day = d.getDay();
    const diff = day; // 일요일이 0이므로
    d.setDate(d.getDate() - diff);
    d.setHours(0, 0, 0, 0);
    return d;
  };

  // 주의 날짜들 구하기 (일요일 ~ 토요일)
  const getWeekDays = (date: Date): Date[] => {
    const weekStart = getWeekStart(date);
    const days: Date[] = [];
    for (let i = 0; i < 7; i++) {
      const d = new Date(weekStart);
      d.setDate(weekStart.getDate() + i);
      days.push(d);
    }
    return days;
  };

  // 월의 날짜들 구하기
  const getMonthDays = (date: Date): Date[][] => {
    const year = date.getFullYear();
    const month = date.getMonth();

    // 월의 첫 날
    const firstDay = new Date(year, month, 1);
    // 월의 마지막 날
    const lastDay = new Date(year, month + 1, 0);

    // 첫 주의 시작 (일요일)
    const startDate = getWeekStart(firstDay);

    // 마지막 주 끝 (토요일)
    const endDate = new Date(lastDay);
    endDate.setDate(lastDay.getDate() + (6 - lastDay.getDay()));

    const weeks: Date[][] = [];
    let currentDate = new Date(startDate);

    while (currentDate <= endDate) {
      const week: Date[] = [];
      for (let i = 0; i < 7; i++) {
        week.push(new Date(currentDate));
        currentDate.setDate(currentDate.getDate() + 1);
      }
      weeks.push(week);
    }

    return weeks;
  };

  // 날짜가 같은지 확인
  const isSameDay = (date1: Date, date2: Date): boolean => {
    return (
      date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth() &&
      date1.getDate() === date2.getDate()
    );
  };

  // 날짜가 오늘인지 확인
  const isToday = (date: Date): boolean => {
    return isSameDay(date, new Date());
  };

  // 날짜가 현재 월인지 확인
  const isCurrentMonth = (
    date: Date,
    currentDate: Date,
  ): boolean => {
    return date.getMonth() === currentDate.getMonth();
  };

  // 날짜의 이벤트 필터링
  const getEventsForDate = (date: Date): CalendarEvent[] => {
    return events.filter((event) => {
      // 채널 필터 - '전체'가 선택되어 있으면 모든 채널 통과, 아니면 선택된 채널만
      if (
        selectedChannels.length > 0 &&
        !selectedChannels.includes("전체")
      ) {
        if (!selectedChannels.includes(event.channel)) {
          return false;
        }
      }

      // 카테고리 필터
      const categoryList =
        event.category === "학사"
          ? selectedAcademicCategories
          : selectedCareerCategories;
      if (!categoryList.includes(event.subcategory)) {
        return false;
      }

      // 날짜 필터 (시작일과 종료일 사이)
      const eventStart = new Date(event.startDate);
      eventStart.setHours(0, 0, 0, 0);
      const eventEnd = new Date(event.endDate);
      eventEnd.setHours(23, 59, 59, 999);
      const checkDate = new Date(date);
      checkDate.setHours(12, 0, 0, 0);

      return checkDate >= eventStart && checkDate <= eventEnd;
    });
  };

  // 채널 토글
  const toggleChannel = (channel: string) => {
    const allChannels = channelOptions.filter(
      (c) => c !== "전체",
    );

    if (channel === "전체") {
      if (selectedChannels.includes("전체")) {
        // 전체가 선택되어 있으면 모두 해제
        setSelectedChannels([]);
      } else {
        // 전체가 선택되어 있지 않으면 모두 선택
        setSelectedChannels(["전체", ...allChannels]);
      }
    } else {
      // 개별 채널 토글
      if (selectedChannels.includes(channel)) {
        // 채널 해제 - '전체'도 함께 해제
        setSelectedChannels(
          selectedChannels.filter(
            (c) => c !== channel && c !== "전체",
          ),
        );
      } else {
        // 채널 선택
        const newChannels = [
          ...selectedChannels.filter((c) => c !== "전체"),
          channel,
        ];
        // 모든 개별 채널이 선택되었는지 확인
        const allSelected = allChannels.every((c) =>
          newChannels.includes(c),
        );
        if (allSelected) {
          // 모든 채널이 선택되면 '전체'도 추가
          setSelectedChannels(["전체", ...newChannels]);
        } else {
          setSelectedChannels(newChannels);
        }
      }
    }
  };

  // 카테고리 토글
  const toggleCategory = (
    category: string,
    isAcademic: boolean,
  ) => {
    if (isAcademic) {
      if (selectedAcademicCategories.includes(category)) {
        setSelectedAcademicCategories(
          selectedAcademicCategories.filter(
            (c) => c !== category,
          ),
        );
      } else {
        setSelectedAcademicCategories([
          ...selectedAcademicCategories,
          category,
        ]);
      }
    } else {
      if (selectedCareerCategories.includes(category)) {
        setSelectedCareerCategories(
          selectedCareerCategories.filter(
            (c) => c !== category,
          ),
        );
      } else {
        setSelectedCareerCategories([
          ...selectedCareerCategories,
          category,
        ]);
      }
    }
  };

  // 필터 초기화
  const resetFilters = () => {
    setSelectedChannels([
      "전체",
      "13기-공지사항",
      "13기-취업공지",
      "13기-취업정보",
      "서울1반-공지사항",
    ]);
    setSelectedAcademicCategories([
      "할일",
      "특강",
      "정보",
      "이벤트",
    ]);
    setSelectedCareerCategories([
      "할일",
      "특강",
      "정보",
      "이벤트",
    ]);
  };

  // 이전/다음 이동
  const goToPrevious = () => {
    const newDate = new Date(currentDate);
    if (viewMode === "week") {
      newDate.setDate(newDate.getDate() - 7);
    } else {
      newDate.setMonth(newDate.getMonth() - 1);
    }
    setCurrentDate(newDate);
  };

  const goToNext = () => {
    const newDate = new Date(currentDate);
    if (viewMode === "week") {
      newDate.setDate(newDate.getDate() + 7);
    } else {
      newDate.setMonth(newDate.getMonth() + 1);
    }
    setCurrentDate(newDate);
  };

  const goToToday = () => {
    setCurrentDate(new Date());
  };

  // 미니 달력용 날짜 생성
  const getMiniCalendarDays = (): Date[][] => {
    return getMonthDays(currentDate);
  };

  // 미니 달력에서 주 선택
  const [selectedWeek, setSelectedWeek] = useState<Date[]>(
    getWeekDays(new Date()),
  );

  const handleMiniCalendarWeekClick = (week: Date[]) => {
    if (viewMode === "week") {
      setSelectedWeek(week);
      setCurrentDate(week[0]); // 주의 시작일로 설정
    }
  };

  const handleMiniCalendarDateClick = (date: Date) => {
    if (viewMode === "month") {
      // 월간 뷰에서는 해당 날짜 포함 주간 뷰로 전환
      setViewMode("week");
      setCurrentDate(date);
      setSelectedWeek(getWeekDays(date));
    }
  };

  // 이벤트 클릭 핸들러
  const handleEventClick = (event: CalendarEvent) => {
    const messageDetail: MessageDetail = {
      id: event.id,
      title: event.title,
      author: "관리자",
      channel: event.channel,
      created_at: event.startDate.toISOString(),
      updated_at: event.startDate.toISOString(),
      content: event.description || "",
      category: event.category,
      subcategory: event.subcategory,
      reactions: [],
      attachments: [],
    };
    setSelectedMessage(messageDetail);
    setIsModalOpen(true);
  };

  // 현재 뷰 데이터
  const weekDays =
    viewMode === "week" ? getWeekDays(currentDate) : [];
  const monthWeeks =
    viewMode === "month" ? getMonthDays(currentDate) : [];
  const miniCalendarWeeks = getMiniCalendarDays();

  // 날짜 포맷팅
  const formatDate = (date: Date): string => {
    return `${date.getMonth() + 1}/${date.getDate()}`;
  };

  const formatMonthYear = (date: Date): string => {
    return `${date.getFullYear()}년 ${date.getMonth() + 1}월`;
  };

  const formatWeekRange = (days: Date[]): string => {
    if (days.length === 0) return "";
    const start = days[0];
    const end = days[6];
    return `${formatDate(start)} ${["일", "월", "화", "수", "목", "금", "토"][start.getDay()]} ~ ${formatDate(end)} ${["일", "월", "화", "수", "목", "금", "토"][end.getDay()]}`;
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 헤더 */}
      <header className="bg-white border-b h-16 px-8 flex items-center justify-between sticky top-0 z-10">
        <div className="flex items-center gap-3">
          <div
            className="text-2xl cursor-pointer"
            style={{ fontWeight: 800 }}
            onClick={() => navigate('/dashboard')}
          >
            편리
            <span className="text-[var(--brand-orange)]">
              햄!
            </span>
          </div>
        </div>

        <div className="flex items-center gap-4">
          {/* 알림 드롭다운 */}
          <DropdownMenu
            open={notificationOpen}
            onOpenChange={setNotificationOpen}
          >
            <DropdownMenuTrigger asChild>
              <Button
                variant="ghost"
                size="icon"
                className="relative"
              >
                <Bell className="w-5 h-5" />
                <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-80">
              <div className="p-3 border-b">
                <div className="flex items-center justify-between">
                  <h3
                    className="text-sm"
                    style={{ fontWeight: 700 }}
                  >
                    알림
                  </h3>
                  <Badge className="bg-red-500 text-white">
                    {notifications.length}
                  </Badge>
                </div>
              </div>
              <div className="max-h-96 overflow-y-auto">
                {notifications.map((notif) => {
                  const bgColor =
                    notif.type === "info"
                      ? "bg-blue-50 border-l-blue-500"
                      : notif.type === "danger"
                        ? "bg-red-50 border-l-red-500"
                        : notif.type === "success"
                          ? "bg-green-50 border-l-green-500"
                          : "bg-gray-50 border-l-gray-500";

                  return (
                    <div
                      key={notif.id}
                      className={`p-3 border-l-4 border-b hover:bg-gray-50 cursor-pointer transition-colors ${bgColor}`}
                    >
                      <p
                        className="text-sm mb-1"
                        style={{ fontWeight: 600 }}
                      >
                        {notif.title}
                      </p>
                      <p className="text-xs text-gray-500">
                        {notif.time}
                      </p>
                    </div>
                  );
                })}
              </div>
            </DropdownMenuContent>
          </DropdownMenu>

          {/* 프로필 드롭다운 */}
          <DropdownMenu
            open={profileOpen}
            onOpenChange={setProfileOpen}
          >
            <DropdownMenuTrigger asChild>
              <Button
                variant="ghost"
                size="icon"
                className="rounded-full"
              >
                <div className="w-8 h-8 rounded-full bg-[var(--brand-orange)] flex items-center justify-center">
                  <User className="w-5 h-5 text-white" />
                </div>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-48">
              <DropdownMenuItem
                onClick={() => {
                  setProfileOpen(false);
                  navigate('/mypage');
                }}
                className="cursor-pointer"
              >
                <Settings className="w-4 h-4 mr-2" />
                마이페이지
              </DropdownMenuItem>
              <DropdownMenuItem
                onClick={() => {
                  setProfileOpen(false);
                  navigate('/');
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
      <div className="flex h-[calc(100vh-64px)]">
        {/* 좌측 사이드바 (25%) */}
        <div className="w-1/4 bg-white border-r p-5 overflow-y-auto">
          {/* 미니 달력 */}
          <Card className="p-4 mb-6 shadow-sm">
            {/* 헤더 */}
            <div className="flex items-center justify-between mb-2">
              <Button
                variant="ghost"
                size="icon"
                className="w-6 h-6"
                onClick={() => {
                  const newDate = new Date(currentDate);
                  newDate.setMonth(newDate.getMonth() - 1);
                  setCurrentDate(newDate);
                }}
              >
                <ChevronLeft className="w-4 h-4" />
              </Button>
              <div
                className="text-sm"
                style={{ fontWeight: 700 }}
              >
                {formatMonthYear(currentDate)}
              </div>
              <Button
                variant="ghost"
                size="icon"
                className="w-6 h-6"
                onClick={() => {
                  const newDate = new Date(currentDate);
                  newDate.setMonth(newDate.getMonth() + 1);
                  setCurrentDate(newDate);
                }}
              >
                <ChevronRight className="w-4 h-4" />
              </Button>
            </div>

            <div className="border-b mb-2" />

            {/* 달력 그리드 */}
            <div
              className={viewMode === "month" ? "rounded-md" : ""}
              style={
                viewMode === "month"
                  ? {
                      backgroundColor: "rgba(255, 138, 61, 0.08)",
                    }
                  : {}
              }
            >
              {/* 요일 헤더 */}
              <div className="grid grid-cols-7 gap-1 mb-1">
                {["일", "월", "화", "수", "목", "금", "토"].map(
                  (day, idx) => (
                    <div
                      key={day}
                      className="text-center text-[10px] text-gray-500 h-6 flex items-center justify-center"
                      style={{
                        color:
                          idx === 0
                            ? "#ef4444"
                            : idx === 6
                              ? "#3b82f6"
                              : undefined,
                      }}
                    >
                      {day}
                    </div>
                  ),
                )}
              </div>

              {/* 날짜 */}
              {miniCalendarWeeks.map((week, weekIdx) => {
                const isSelectedWeek =
                  viewMode === "week" &&
                  week.some((d) =>
                    selectedWeek.some((sd) => isSameDay(d, sd)),
                  );

                return (
                  <div
                    key={weekIdx}
                    className={`grid grid-cols-7 gap-1 mb-1 rounded-md ${
                      isSelectedWeek ? "p-0.5" : ""
                    }`}
                    style={
                      isSelectedWeek
                        ? {
                            backgroundColor:
                              "rgba(255, 138, 61, 0.08)",
                          }
                        : {}
                    }
                  >
                    {week.map((date, dayIdx) => {
                      const hasEvents =
                        getEventsForDate(date).length > 0;
                      const today = isToday(date);
                      const currentMonth = isCurrentMonth(
                        date,
                        currentDate,
                      );

                      return (
                        <div
                          key={dayIdx}
                          className={`h-8 flex flex-col items-center justify-center text-[11px] relative cursor-pointer rounded ${
                            today
                              ? "bg-[var(--brand-orange)] text-white"
                              : currentMonth
                                ? "text-gray-700 hover:bg-gray-100"
                                : "text-gray-300"
                          }`}
                          style={{
                            color:
                              !today &&
                              currentMonth &&
                              dayIdx === 0
                                ? "#ef4444"
                                : !today &&
                                    currentMonth &&
                                    dayIdx === 6
                                  ? "#3b82f6"
                                  : undefined,
                          }}
                          onClick={() => {
                            if (viewMode === "week") {
                              handleMiniCalendarWeekClick(week);
                            } else {
                              handleMiniCalendarDateClick(date);
                            }
                          }}
                        >
                          {date.getDate()}
                          {hasEvents && !today && (
                            <div className="absolute bottom-0.5 w-1 h-1 bg-[var(--brand-orange)] rounded-full" />
                          )}
                        </div>
                      );
                    })}
                  </div>
                );
              })}
            </div>
          </Card>

          {/* 필터 섹션 */}
          <div className="space-y-4">
            {/* 채널 필터 */}
            <div>
              <button
                onClick={() =>
                  setChannelExpanded(!channelExpanded)
                }
                className="flex items-center gap-1.5 mb-2 w-full hover:opacity-70 transition-opacity"
              >
                <Hash className="w-3.5 h-3.5 text-gray-500" />
                <h3
                  className="text-xs text-gray-600 flex-1 text-left"
                  style={{ fontWeight: 700 }}
                >
                  채널
                </h3>
                <ChevronDown
                  className={`w-3.5 h-3.5 text-gray-500 transition-transform ${
                    channelExpanded ? "rotate-180" : ""
                  }`}
                />
              </button>
              {channelExpanded && (
                <div className="space-y-1.5">
                  {channelOptions.map((channel) => (
                    <button
                      key={channel}
                      onClick={() => toggleChannel(channel)}
                      className="w-full h-8 px-3 rounded-md text-xs text-left flex items-center gap-2 transition-colors hover:bg-gray-100"
                      style={{
                        fontWeight: selectedChannels.includes(
                          channel,
                        )
                          ? 700
                          : 500,
                      }}
                    >
                      <div
                        className={`w-3 h-3 rounded-full border-2 flex items-center justify-center ${
                          selectedChannels.includes(channel)
                            ? "border-[var(--brand-orange)]"
                            : "border-gray-300"
                        }`}
                      >
                        {selectedChannels.includes(channel) && (
                          <div className="w-1.5 h-1.5 rounded-full bg-[var(--brand-orange)]" />
                        )}
                      </div>
                      <span
                        className={
                          selectedChannels.includes(channel)
                            ? "text-gray-800"
                            : "text-gray-600"
                        }
                      >
                        {channel}
                      </span>
                    </button>
                  ))}
                </div>
              )}
            </div>

            <div className="border-t" />

            {/* 학사 카테고리 */}
            <div>
              <div className="flex items-center gap-1.5 mb-2">
                <GraduationCap className="w-3.5 h-3.5 text-gray-500" />
                <span
                  className="text-xs text-gray-600"
                  style={{ fontWeight: 700 }}
                >
                  학사
                </span>
              </div>
              <div className="grid grid-cols-2 gap-1.5">
                {["할일", "특강", "정보", "이벤트"].map(
                  (category) => (
                    <button
                      key={category}
                      onClick={() =>
                        toggleCategory(category, true)
                      }
                      className={`h-8 rounded-md text-xs flex items-center justify-center gap-1 border transition-colors ${getCategoryButtonColor(
                        category,
                        selectedAcademicCategories.includes(
                          category,
                        ),
                      )}`}
                      style={{ fontWeight: 500 }}
                    >
                      {selectedAcademicCategories.includes(
                        category,
                      ) && (
                        <span className="text-[10px]">✓</span>
                      )}
                      {category}
                    </button>
                  ),
                )}
              </div>
            </div>

            {/* 취업 카테고리 */}
            <div>
              <div className="flex items-center gap-1.5 mb-2">
                <Briefcase className="w-3.5 h-3.5 text-gray-500" />
                <span
                  className="text-xs text-gray-600"
                  style={{ fontWeight: 700 }}
                >
                  취업
                </span>
              </div>
              <div className="grid grid-cols-2 gap-1.5">
                {["할일", "특강", "정보", "이벤트"].map(
                  (category) => (
                    <button
                      key={category}
                      onClick={() =>
                        toggleCategory(category, false)
                      }
                      className={`h-8 rounded-md text-xs flex items-center justify-center gap-1 border transition-colors ${getCategoryButtonColor(
                        category,
                        selectedCareerCategories.includes(
                          category,
                        ),
                      )}`}
                      style={{ fontWeight: 500 }}
                    >
                      {selectedCareerCategories.includes(
                        category,
                      ) && (
                        <span className="text-[10px]">✓</span>
                      )}
                      {category}
                    </button>
                  ),
                )}
              </div>
            </div>

            {/* 필터 초기화 */}
            <div className="pt-2">
              <button
                onClick={resetFilters}
                className="w-full text-xs text-gray-500 hover:text-gray-700 py-2"
                style={{ fontWeight: 500 }}
              >
                필터 초기화
              </button>
            </div>
          </div>
        </div>

        {/* 메인 캘린더 영역 (75%) */}
        <div className="flex-1 flex flex-col overflow-hidden">
          {/* 상단 컨트롤 바 */}
          <div className="bg-white border-b px-6 py-5">
            <div className="flex items-center justify-between">
              {/* 좌측 타이틀 */}
              <div>
                <h1
                  className="text-xl mb-1"
                  style={{ fontWeight: 700 }}
                >
                  {viewMode === "week"
                    ? `이번 주 일정 (${formatWeekRange(weekDays)})`
                    : formatMonthYear(currentDate)}
                </h1>
                <p className="text-xs text-gray-500">
                  날짜별 일정을 한눈에 확인하세요
                </p>
              </div>

              {/* 우측 컨트롤 */}
              <div className="flex items-center gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={goToPrevious}
                  className="h-9"
                >
                  <ChevronLeft className="w-4 h-4 mr-1" />
                  {viewMode === "week" ? "이전 주" : "이전 달"}
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={goToToday}
                  className="h-9"
                >
                  오늘
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={goToNext}
                  className="h-9"
                >
                  {viewMode === "week" ? "다음 주" : "다음 달"}
                  <ChevronRight className="w-4 h-4 ml-1" />
                </Button>

                <div className="ml-2 flex rounded-lg border overflow-hidden">
                  <button
                    onClick={() => setViewMode("week")}
                    className={`px-4 py-2 text-sm transition-colors ${
                      viewMode === "week"
                        ? "bg-[var(--brand-orange)] text-white"
                        : "bg-white text-gray-600 hover:bg-gray-50"
                    }`}
                    style={{ fontWeight: 500 }}
                  >
                    주간
                  </button>
                  <button
                    onClick={() => setViewMode("month")}
                    className={`px-4 py-2 text-sm transition-colors border-l ${
                      viewMode === "month"
                        ? "bg-[var(--brand-orange)] text-white"
                        : "bg-white text-gray-600 hover:bg-gray-50"
                    }`}
                    style={{ fontWeight: 500 }}
                  >
                    월간
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* 캘린더 그리드 */}
          <div className="flex-1 overflow-auto bg-gray-50">
            {viewMode === "week" ? (
              // 주간 뷰
              <div className="h-full flex flex-col">
                {/* 요일 헤더 */}
                <div className="grid grid-cols-7 bg-white border-b sticky top-0 z-10">
                  {weekDays.map((day, idx) => {
                    const today = isToday(day);
                    return (
                      <div
                        key={idx}
                        className={`text-center py-3 border-r last:border-r-0 ${
                          today ? "bg-blue-50" : ""
                        }`}
                      >
                        <div
                          className="text-sm mb-1"
                          style={{
                            fontWeight: 500,
                            color:
                              idx === 0
                                ? "#ef4444"
                                : idx === 6
                                  ? "#3b82f6"
                                  : undefined,
                          }}
                        >
                          {
                            [
                              "일",
                              "월",
                              "화",
                              "수",
                              "목",
                              "금",
                              "토",
                            ][day.getDay()]
                          }
                        </div>
                        <div
                          className={`text-base ${today ? "text-[var(--brand-orange)]" : ""}`}
                          style={{ fontWeight: 700 }}
                        >
                          {formatDate(day)}
                        </div>
                      </div>
                    );
                  })}
                </div>

                {/* 날짜 컬럼 */}
                <div className="grid grid-cols-7 flex-1 bg-white">
                  {weekDays.map((day, idx) => {
                    const dayEvents = getEventsForDate(day);
                    const today = isToday(day);

                    return (
                      <div
                        key={idx}
                        className={`border-r last:border-r-0 p-3 overflow-y-auto ${
                          today
                            ? "bg-blue-50 bg-opacity-30"
                            : ""
                        }`}
                      >
                        {dayEvents.length === 0 ? (
                          <div className="flex items-center justify-center h-32 text-xs text-gray-400">
                            일정 없음
                          </div>
                        ) : (
                          <div className="space-y-2">
                            {dayEvents.map((event) => (
                              <Card
                                key={event.id}
                                className="p-2.5 cursor-pointer hover:shadow-md transition-shadow"
                                onClick={() =>
                                  handleEventClick(event)
                                }
                              >
                                {/* 카테고리 라벨 */}
                                <div className="flex items-center justify-between gap-2">
                                  <div className="flex items-center gap-1">
                                    <Badge
                                      className={`text-[10px] px-1.5 py-0 border ${getCategoryColor(event.category, event.subcategory)}`}
                                    >
                                      {event.category}/
                                      {event.subcategory}
                                    </Badge>
                                    {!isSameDay(
                                      event.startDate,
                                      event.endDate,
                                    ) && (
                                      <span
                                        className="text-[10px] text-gray-400"
                                        style={{
                                          fontWeight: 500,
                                        }}
                                      >
                                        {formatDate(
                                          event.startDate,
                                        )}
                                        ~
                                      </span>
                                    )}
                                  </div>
                                  <ExternalLink className="w-3.5 h-3.5 text-gray-400" />
                                </div>

                                {/* 시간 */}
                                {event.startTime &&
                                  event.endTime && (
                                    <div
                                      className="text-sm"
                                      style={{
                                        fontWeight: 700,
                                      }}
                                    >
                                      {event.startTime}~
                                      {event.endTime}
                                    </div>
                                  )}

                                {/* 제목 */}
                                <div
                                  className="text-sm truncate"
                                  style={{ fontWeight: 500 }}
                                >
                                  {event.title}
                                </div>

                                {/* 설명 */}
                                {event.description && (
                                  <div className="text-xs text-gray-500 truncate">
                                    {event.description}
                                  </div>
                                )}
                              </Card>
                            ))}
                          </div>
                        )}
                      </div>
                    );
                  })}
                </div>
              </div>
            ) : (
              // 월간 뷰
              <div className="h-full p-4">
                <div className="bg-white rounded-lg shadow-sm h-full flex flex-col overflow-hidden">
                  {/* 요일 헤더 */}
                  <div className="grid grid-cols-7 border-b">
                    {[
                      "일",
                      "월",
                      "화",
                      "수",
                      "목",
                      "금",
                      "토",
                    ].map((day, idx) => (
                      <div
                        key={day}
                        className="text-center py-3 text-sm border-r last:border-r-0"
                        style={{
                          fontWeight: 600,
                          color:
                            idx === 0
                              ? "#ef4444"
                              : idx === 6
                                ? "#3b82f6"
                                : "#374151",
                        }}
                      >
                        {day}
                      </div>
                    ))}
                  </div>

                  {/* 날짜 셀 */}
                  <div className="flex-1 flex flex-col">
                    {monthWeeks.map((week, weekIdx) => (
                      <div
                        key={weekIdx}
                        className="grid grid-cols-7 flex-1 border-b last:border-b-0"
                      >
                        {week.map((date, dayIdx) => {
                          const dayEvents =
                            getEventsForDate(date);
                          const today = isToday(date);
                          const currentMonth = isCurrentMonth(
                            date,
                            currentDate,
                          );

                          return (
                            <div
                              key={dayIdx}
                              className={`border-r last:border-r-0 p-2 cursor-pointer hover:bg-gray-50 transition-colors ${
                                !currentMonth
                                  ? "bg-gray-50"
                                  : ""
                              }`}
                              onClick={() => {
                                setViewMode("week");
                                setCurrentDate(date);
                                setSelectedWeek(
                                  getWeekDays(date),
                                );
                              }}
                            >
                              {/* 날짜 */}
                              <div className="flex items-start justify-between mb-1">
                                <span
                                  className={`inline-flex items-center justify-center w-6 h-6 text-sm rounded-full ${
                                    today
                                      ? "bg-[var(--brand-orange)] text-white"
                                      : currentMonth
                                        ? "text-gray-700"
                                        : "text-gray-400"
                                  }`}
                                  style={{
                                    fontWeight: today
                                      ? 700
                                      : 500,
                                  }}
                                >
                                  {date.getDate()}
                                </span>
                                {dayEvents.length > 3 && (
                                  <span
                                    className="text-[10px] text-gray-500"
                                    style={{ fontWeight: 500 }}
                                  >
                                    +{dayEvents.length - 3}
                                  </span>
                                )}
                              </div>

                              {/* 이벤트 제목 표시 */}
                              {dayEvents.length > 0 && (
                                <div className="space-y-0.5">
                                  {dayEvents
                                    .slice(0, 3)
                                    .map((event) => {
                                      // 서브카테고리별 세로선 색상
                                      const getBorderColor =
                                        () => {
                                          switch (
                                            event.subcategory
                                          ) {
                                            case "할일":
                                              return "border-red-500";
                                            case "특강":
                                              return "border-blue-500";
                                            case "정보":
                                              return "border-green-500";
                                            case "이벤트":
                                              return "border-purple-500";
                                            default:
                                              return "border-gray-400";
                                          }
                                        };

                                      return (
                                        <div
                                          key={event.id}
                                          className={`text-sm truncate pl-2 border-l-[3px] ${getBorderColor()}`}
                                          style={{
                                            fontWeight: 500,
                                          }}
                                        >
                                          {event.title}
                                        </div>
                                      );
                                    })}
                                </div>
                              )}
                            </div>
                          );
                        })}
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            )}
          </div>
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
