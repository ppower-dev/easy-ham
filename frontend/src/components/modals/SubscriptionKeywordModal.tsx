import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import { X } from "lucide-react";

interface SubscriptionKeywordModalProps {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
}

const availableKeywords = [
  "프로젝트",
  "취업",
  "특강",
  "공지",
  "행사",
  "MT",
  "스터디",
  "멘토링",
  "채용",
  "코딩테스트",
  "알고리즘",
  "면접",
  "포트폴리오",
  "제출",
  "발표",
];

export function SubscriptionKeywordModal({
  isOpen,
  onOpenChange,
}: SubscriptionKeywordModalProps) {
  const [subscribedKeywords, setSubscribedKeywords] = useState<string[]>([]);
  const [customKeyword, setCustomKeyword] = useState("");

  const handleAddKeyword = (keyword: string) => {
    if (
      subscribedKeywords.length < 5 &&
      !subscribedKeywords.includes(keyword)
    ) {
      setSubscribedKeywords([...subscribedKeywords, keyword]);
    }
  };

  const handleRemoveKeyword = (keyword: string) => {
    setSubscribedKeywords(subscribedKeywords.filter((k) => k !== keyword));
  };

  const handleAddCustomKeyword = () => {
    if (
      customKeyword.trim() &&
      subscribedKeywords.length < 5 &&
      !subscribedKeywords.includes(customKeyword.trim())
    ) {
      setSubscribedKeywords([...subscribedKeywords, customKeyword.trim()]);
      setCustomKeyword("");
    }
  };

  const handleSave = () => {
    // 저장 로직 (나중에 API 연동)
    onOpenChange(false);
  };

  return (
    <Dialog open={isOpen} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle>구독 키워드 관리</DialogTitle>
          <DialogDescription>
            관심 있는 키워드를 선택하면 관련 공지사항을 우선적으로
            알려드립니다.
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          {/* 카운터 */}
          <div className="text-sm text-gray-500 text-right">
            {subscribedKeywords.length} / 5
          </div>

          {/* 선택된 키워드 */}
          {subscribedKeywords.length > 0 && (
            <div>
              <Label className="mb-2 block text-xs">선택된 키워드</Label>
              <div className="flex flex-wrap gap-2">
                {subscribedKeywords.map((keyword) => (
                  <Badge
                    key={keyword}
                    className="bg-[var(--brand-orange)] text-white px-3 py-1.5 flex items-center gap-2"
                  >
                    {keyword}
                    <button
                      onClick={() => handleRemoveKeyword(keyword)}
                      className="hover:opacity-70"
                    >
                      <X className="w-3 h-3" />
                    </button>
                  </Badge>
                ))}
              </div>
            </div>
          )}

          <Separator />

          {/* 사용 가능한 키워드 */}
          <div>
            <Label className="mb-2 block text-xs">사용 가능한 키워드</Label>
            <div className="flex flex-wrap gap-2">
              {availableKeywords
                .filter((k) => !subscribedKeywords.includes(k))
                .map((keyword) => (
                  <Badge
                    key={keyword}
                    variant="outline"
                    className={`px-3 py-1.5 cursor-pointer transition-colors ${
                      subscribedKeywords.length >= 5
                        ? "opacity-50 cursor-not-allowed"
                        : "hover:bg-[var(--brand-orange-light)] hover:border-[var(--brand-orange)]"
                    }`}
                    onClick={() => handleAddKeyword(keyword)}
                  >
                    {keyword}
                  </Badge>
                ))}
            </div>
          </div>

          <Separator />

          {/* 직접 입력 */}
          <div>
            <Label className="mb-2 block text-xs">직접 입력</Label>
            <div className="flex gap-2">
              <Input
                placeholder="키워드를 입력하세요"
                value={customKeyword}
                onChange={(e) => setCustomKeyword(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    e.preventDefault();
                    handleAddCustomKeyword();
                  }
                }}
                disabled={subscribedKeywords.length >= 5}
              />
              <Button
                onClick={handleAddCustomKeyword}
                variant="outline"
                disabled={subscribedKeywords.length >= 5}
                className="border-[var(--brand-orange)] text-[var(--brand-orange)] hover:bg-[var(--brand-orange-light)]"
              >
                추가
              </Button>
            </div>
          </div>
        </div>

        {/* 저장 버튼 */}
        <div className="flex justify-end gap-2 mt-6">
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            취소
          </Button>
          <Button
            onClick={handleSave}
            className="bg-[var(--brand-orange)] hover:bg-[var(--brand-orange-dark)] text-white"
          >
            저장
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
