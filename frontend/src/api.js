// 백엔드 API 호출 공통 헬퍼 
// — 프론트판 "예외 처리 인프라"
// - base URL, credentials(세션 쿠키), res.ok 체크를 한 곳에서 처리
// - 실패 응답(4xx/5xx)은 throw로 던져서 호출부의 .catch로 보냄

import { API_BASE_URL } from "./config";

export async function apiFetch(path, options = {}) {
  const res = await fetch(`${API_BASE_URL}${path}`, {
    credentials: "include",
    ...options,
  });

  // 응답 본문 파싱 (401처럼 본문 없는 응답 대비해 실패 시 null)
  const data = await res.json().catch(() => null);

  if (!res.ok) {
    // 백엔드 ErrorResponse의 message 사용, 없으면 상태코드로
    throw new Error(data?.message ?? `요청 실패: (${res.status})`);
  }
  return data;
}