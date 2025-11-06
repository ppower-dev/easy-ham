/**
 * 검색 API 응답 데이터를 프론트엔드 Notice 타입으로 변환하는 유틸리티
 */

import type { Notice, Subcategory, Attachment } from '@/types/notice';
import type { SearchResultItem, SearchFileItem } from '@/types/api';

/**
 * content에서 title 추출
 *
 * 현재 백엔드에서 title 필드를 주지 않으므로,
 * content의 첫 줄을 title로 사용합니다.
 *
 * @param content - 메시지 본문
 * @returns 추출된 제목 (최대 100자)
 */
function extractTitle(content: string): string {
  // 줄바꿈으로 분리해서 첫 줄 가져오기
  const firstLine = content.split('\n')[0].trim();

  if (firstLine && firstLine.length > 0) {
    // 첫 줄이 있으면 첫 줄 사용 (100자 제한)
    return firstLine.length > 100
      ? firstLine.substring(0, 100) + '...'
      : firstLine;
  }

  // 첫 줄이 없으면 전체 content의 첫 50자 사용
  return content.length > 50
    ? content.substring(0, 50) + '...'
    : content;
}

/**
 * mainCategory 숫자 코드 → 한글 카테고리 변환
 *
 * 현재는 null이 오지만, 나중에 LLM 파이프라인 완성 시
 * 실제 숫자 코드가 올 예정입니다.
 *
 * @param code - 카테고리 코드
 * @returns "학사" | "취업"
 */
function mapMainCategory(code: number | null): "학사" | "취업" {
  if (code === null) return "학사";  // 기본값

  // TODO: 백엔드 팀원에게 실제 매핑 확인 필요
  return code === 1 ? "학사" : "취업";
}

/**
 * subCategory 숫자 코드 → 한글 서브카테고리 변환
 *
 * @param code - 서브카테고리 코드
 * @returns "할일" | "특강" | "정보" | "행사"
 */
function mapSubCategory(code: number | null): Subcategory {
  if (code === null) return "정보";  // 기본값

  // TODO: 백엔드 팀원에게 실제 매핑 확인 필요
  const mapping: Record<number, Subcategory> = {
    1: "할일",
    2: "특강",
    3: "정보",
    4: "정보",
    5: "행사",
    6: "정보",
    8: "정보",
  };

  return mapping[code] || "정보";
}

/**
 * 파일 정보 변환
 *
 * SearchFileItem[] → Attachment[]
 * 모달에서 첨부파일을 표시하기 위한 형식으로 변환
 *
 * @param files - 백엔드 파일 정보 배열
 * @returns 모달용 Attachment 배열 (없으면 undefined)
 */
function convertFiles(files: SearchFileItem[] | null): Attachment[] | undefined {
  if (!files || files.length === 0) return undefined;

  return files.map(file => ({
    id: file.id,
    name: file.name,
    type: file.hasPreviewImage ? 'image' : 'file',
    mimeType: file.mimeType,
    // Mattermost 파일 다운로드 URL
    url: `https://k13a105.p.ssafy.io/chat/api/v4/files/${file.id}`,
  }));
}

/**
 * 검색 결과 아이템 → Notice 타입 변환
 *
 * 이 함수는 백엔드 검색 API 응답을 프론트엔드에서 사용하는
 * Notice 타입으로 변환합니다.
 *
 * ⚠️ 주의사항:
 * 1. id가 null일 수 있으므로 랜덤 ID 생성
 * 2. title은 content에서 추출
 * 3. deadline, dday는 현재 없음
 * 4. bookmarked는 isLiked에서 가져오되, 없으면 false
 *
 * @param item - 검색 결과 아이템
 * @returns Notice 객체
 */
export function convertSearchItemToNotice(item: SearchResultItem): Notice {
  return {
    // ID: null이면 임시 랜덤 ID (나중에 실제 ID로 교체됨)
    id: item.id || Math.floor(Math.random() * 1000000),

    // Title: content 첫 줄 추출
    title: extractTitle(item.content),

    // 본문 및 저자
    content: item.content,
    author: item.userName,

    // 채널 (현재는 ID 문자열)
    channel: item.mmChannelId,

    // 카테고리 변환
    category: mapMainCategory(item.mainCategory),
    subcategory: mapSubCategory(item.subCategory),

    // 북마크 상태 (nullish coalescing: isLiked가 없으면 false)
    bookmarked: item.isLiked ?? false,

    // 완료 상태 (검색 결과에는 없으므로 기본값 false)
    completed: false,

    // 첨부파일 변환
    attachments: convertFiles(item.files),

    // 날짜 (타임스탬프 밀리초 → ISO 문자열)
    createdAt: new Date(item.mmCreatedAt).toISOString(),
    updatedAt: new Date(item.mmCreatedAt).toISOString(),

    // D-day, deadline (현재 없음 - 나중에 LLM 파이프라인 완성 시 추가)
    dday: null,
    deadline: undefined,

    // Mattermost 링크
    mattermostUrl: `https://mattermost.ssafy.com/message/${item.mmMessageId}`,
  };
}
