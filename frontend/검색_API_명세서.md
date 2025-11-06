# 통합 검색
Method: GET
controller: /api/v1/search
endpoint: /posts
## Input data
Header: sso_token, String, Bearer token
Query Parameters:
keyword: String, 검색 키워드
channel_id: Long, 채널
sub_category: Long, 별도 조회로 id 받아서 요청해야됨
start_date: date, 시작 날짜(공지 작성일)
end_date: date, 종료 날짜(공지 작성일)
page: Long, default: 1
size: Long, defaul: 20

Input Sample 파라미터 없음
https://k13a105.p.ssafy.io/api/v1/search/posts

Output Sample1 다 나옴
```json
{
  "status": 200,
  "message": "요청 성공",
  "data": {
    "items": [
      {
        "id": null,
        "mmMessageId": "id6yeg53q781memcswjfhsa89y",
        "mmChannelId": "yr489sfsxbrjfyuwyqmeakw63o",
        "userName": "jznsqx4u97ngmbmri36h97kfee",
        "content": "ㅁㅇㄴㄹ",
        "highlightedContent": "ㅁㅇㄴㄹ",
        "mmCreatedAt": 1762392249879,
        "mainCategory": null,
        "subCategory": null,
        "files": null
      },
      {
        "id": null,
        "mmMessageId": "ihjigr1wh7bn9qy9bt65frfhac",
        "mmChannelId": "yr489sfsxbrjfyuwyqmeakw63o",
        "userName": "gqadzfanmpgqmmbok6z9t69oah",
        "content": "공지 : 참고하세요. 참고하라고 ㅋㅋ",
        "highlightedContent": "공지 : 참고하세요. 참고하라고 ㅋㅋ",
        "mmCreatedAt": 1762390428939,
        "mainCategory": null,
        "subCategory": null,
        "files": [
          {
            "id": "uny4wdcuzt8bpnf5ppfx33u8qy",
            "name": "[현대오토에버] 2025년 하반기 현대오토에버 신입사원 채용 상세 모집요강_2.pdf",
            "extension": "pdf",
            "size": 163831,
            "mimeType": "application/pdf",
            "width": 0,
            "height": 0,
            "hasPreviewImage": false
          }
        ]
      },
      {
        "id": null,
        "mmMessageId": "i3hfnkibxprumxt8eqcs8hzjte",
        "mmChannelId": "yr489sfsxbrjfyuwyqmeakw63o",
        "userName": "gqadzfanmpgqmmbok6z9t69oah",
        "content": "이것은 붕어빵들입니다.",
        "highlightedContent": "이것은 붕어빵들입니다.",
        "mmCreatedAt": 1762390294985,
        "mainCategory": null,
        "subCategory": null,
        "files": [
          {
            "id": "uwdx9ojqyp8opgreattyk3qrzc",
            "name": "honeybutterbung.png",
            "extension": "png",
            "size": 1436,
            "mimeType": "image/png",
            "width": 98,
            "height": 67,
            "hasPreviewImage": true
          },
          {
            "id": "gc9uf5du7pbzuyhb7x4p711ppr",
            "name": "shubung.png",
            "extension": "png",
            "size": 2256,
            "mimeType": "image/png",
            "width": 187,
            "height": 118,
            "hasPreviewImage": true
          }
        ]
      },
      {
        "id": null,
        "mmMessageId": "w4ucczzdy3yc3e6ctfqig5e9th",
        "mmChannelId": "yr489sfsxbrjfyuwyqmeakw63o",
        "userName": "gqadzfanmpgqmmbok6z9t69oah",
        "content": "이것은 붕어빵입니다.",
        "highlightedContent": "이것은 붕어빵입니다.",
        "mmCreatedAt": 1762390172985,
        "mainCategory": null,
        "subCategory": null,
        "files": [
          {
            "id": "4e8ddy1k8tnpdgq4w6pedqqfzh",
            "name": "honeybutterbung.png",
            "extension": "png",
            "size": 1436,
            "mimeType": "image/png",
            "width": 98,
            "height": 67,
            "hasPreviewImage": true
          }
        ]
      },
      {
        "id": null,
        "mmMessageId": "fw7kgwffhidsdgug4kriqw7fda",
        "mmChannelId": "yr489sfsxbrjfyuwyqmeakw63o",
        "userName": "gqadzfanmpgqmmbok6z9t69oah",
        "content": "이것은 죠스바입니다.",
        "highlightedContent": "이것은 죠스바입니다.",
        "mmCreatedAt": 1762390165656,
        "mainCategory": null,
        "subCategory": null,
        "files": [
          {
            "id": "oohiuuujkbfsufeyhnmheds63h",
            "name": "jawsbar.png",
            "extension": "png",
            "size": 16486,
            "mimeType": "image/png",
            "width": 252,
            "height": 541,
            "hasPreviewImage": true
          }
        ]
      },
      {
        "id": null,
        "mmMessageId": "opsc5km9ztbj7kn6ahe39568zc",
        "mmChannelId": "yr489sfsxbrjfyuwyqmeakw63o",
        "userName": "gqadzfanmpgqmmbok6z9t69oah",
        "content": "보안 서약서 제출해주세요",
        "highlightedContent": "보안 서약서 제출해주세요",
        "mmCreatedAt": 1762390157847,
        "mainCategory": null,
        "subCategory": null,
        "files": null
      },
      {
        "id": null,
        "mmMessageId": "oy95xmurj3ny9fzymbwkcz89ac",
        "mmChannelId": "yr489sfsxbrjfyuwyqmeakw63o",
        "userName": "gqadzfanmpgqmmbok6z9t69oah",
        "content": "오늘 점심 메뉴는 무엇인가요?",
        "highlightedContent": "오늘 점심 메뉴는 무엇인가요?",
        "mmCreatedAt": 1762390155071,
        "mainCategory": null,
        "subCategory": null,
        "files": null
      },
      {
        "id": null,
        "mmMessageId": "7oywb6473fy57moxim5g9kz4hh",
        "mmChannelId": "yr489sfsxbrjfyuwyqmeakw63o",
        "userName": "gqadzfanmpgqmmbok6z9t69oah",
        "content": "안녕하세요?",
        "highlightedContent": "안녕하세요?",
        "mmCreatedAt": 1762390148666,
        "mainCategory": null,
        "subCategory": null,
        "files": null
      }
    ],
    "metadata": {
      "query": "",
      "totalHits": 8,
      "page": 0,
      "size": 20,
      "totalPages": 1,
      "processingTimeMs": 0,
      "appliedFilters": {
        "channelIds": null,
        "categoryIds": null,
        "startDate": null,
        "endDate": null,
        "isLiked": null
      }
    }
  }
}
```

Input Sample 2 "예산" 키워드 검색
https://k13a105.p.ssafy.io/api/v1/search/posts?keyword=%EC%B5%9C%EC%88%98%EC%A7%84
```json
{
    "status": 200,
    "message": "요청 성공",
    "data": {
        "items": [
            {
                "id": 20,
                "mmMessageId": "msg_004",
                "mmChannelId": "재무팀",
                "userName": "이과장",
                "content": "예산 집행 가이드라인 업데이트. 모든 지출은 사전 승인 필요",
                "highlightedContent": "<em>예산</em> 집행 가이드라인 업데이트. 모든 지출은 사전 승인 필요",
                "mmCreatedAt": 1731542400000,
                "mainCategory": 2,
                "subCategory": 4
            },
            {
                "id": 18,
                "mmMessageId": "msg_002",
                "mmChannelId": "기획팀",
                "userName": "최서연",
                "content": "Q4 예산 집행 현황 보고드립니다. 현재 집행률 75%입니다.",
                "highlightedContent": "Q4 <em>예산</em> 집행 현황 보고드립니다. 현재 집행률 75%입니다.",
                "mmCreatedAt": 1731024000000,
                "mainCategory": 2,
                "subCategory": 8
            },
            {
                "id": 21,
                "mmMessageId": "msg_005",
                "mmChannelId": "마케팅팀",
                "userName": "박대리",
                "content": "광고 캠페인 예산 조정 건. 기존 3천만원에서 4천만원으로 증액 요청",
                "highlightedContent": "광고 캠페인 <em>예산</em> 조정 건. 기존 3천만원에서 4천만원으로 증액 요청",
                "mmCreatedAt": 1731628800000,
                "mainCategory": 2,
                "subCategory": 6
            },
            {
                "id": 17,
                "mmMessageId": "msg_001",
                "mmChannelId": "공지사항",
                "userName": "김철수",
                "content": "2024년 11월 정기 회의록입니다. 주요 안건: 예산 편성, 인력 충원 계획",
                "highlightedContent": "2024년 11월 정기 회의록입니다. 주요 <em>안건</em>: <em>예산</em> 편성, 인력 충원 계획",
                "mmCreatedAt": 1730764800000,
                "mainCategory": 1,
                "subCategory": 1
            },
            {
                "id": 14,
                "mmMessageId": "bulk_002",
                "mmChannelId": "인사팀",
                "userName": "한예진",
                "content": "2024년 연말 워크샵 안내입니다. 12월 15일 예정",
                "highlightedContent": "2024년 연말 워크샵 <em>안내</em>입니다. 12월 15일 예정",
                "mmCreatedAt": 1731369600000,
                "mainCategory": 2,
                "subCategory": 5
            }
        ],
        "metadata": {
            "query": "예산",
            "totalHits": 5,
            "page": 0,
            "size": 20,
            "totalPages": 1,
            "processingTimeMs": 1,
            "appliedFilters": {
                "channelIds": null,
                "categoryIds": null,
                "startDate": null,
                "endDate": null,
                "isLiked": null
            }
        }
    }
}
```
Input Sample 3: 다중 채널 검색
http://localhost:8080/api/v1/search/posts?channelIds=%EA%B8%B0%ED%9A%8D%ED%8C%80&channelIds=%EA%B0%9C%EB%B0%9C%ED%8C%80
```json
{
    "status": 200,
    "message": "요청 성공",
    "data": {
        "items": [
            {
                "id": 19,
                "mmMessageId": "msg_003",
                "mmChannelId": "개발팀",
                "userName": "강동원",
                "content": "Meilisearch 검색 엔진 도입 완료. 성능 테스트 진행 중",
                "highlightedContent": "Meilisearch 검색 엔진 도입 완료. 성능 테스트 진행 중",
                "mmCreatedAt": 1731110400000,
                "mainCategory": 1,
                "subCategory": 1
            },
            {
                "id": 18,
                "mmMessageId": "msg_002",
                "mmChannelId": "기획팀",
                "userName": "최서연",
                "content": "Q4 예산 집행 현황 보고드립니다. 현재 집행률 75%입니다.",
                "highlightedContent": "Q4 예산 집행 현황 보고드립니다. 현재 집행률 75%입니다.",
                "mmCreatedAt": 1731024000000,
                "mainCategory": 2,
                "subCategory": 8
            }
        ],
        "metadata": {
            "query": "",
            "totalHits": 2,
            "page": 0,
            "size": 20,
            "totalPages": 1,
            "processingTimeMs": 0,
            "appliedFilters": {
                "channelIds": [
                    "기획팀",
                    "개발팀"
                ],
                "categoryIds": null,
                "startDate": null,
                "endDate": null,
                "isLiked": null
            }
        }
    }
}
```

# 검색 엔진 상태조회
Method: GET
controller: /api/admin/search
endpoint: /status
```json
{
    "stats": {
        "numberOfDocuments": 13,
        "fieldDistribution": {
            "content": 13,
            "id": 13,
            "mainCategory": 13,
            "mmChannelId": 13,
            "mmCreatedAt": 13,
            "mmMessageId": 13,
            "subCategory": 13,
            "userName": 13
        },
        "indexing": false
    },
    "success": true
}
```

# 검색엔진 리인덱싱
Method: POST
controller: /api/admin/search
endpoint: /reindex

요청 바디 없음
https://k13a105.p.ssafy.io/api/admin/search/reindex

응답 예)
```json
{
    "indexedCount": 4,
    "success": true,
    "message": "Successfully reindexed all posts"
}
```