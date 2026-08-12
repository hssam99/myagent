// 앱 전역 설정값을 한 곳에 모아두는 파일
// - 각 컴포넌트가 import.meta.env.VITE_...를 직접 읽지 않고 여기서 한 번 감싸서 export
// - 이유: 환경변수 이름이 바뀌거나 읽는 방식이 바뀌어도 이 파일만 고치면 됨
//   (백엔드의 AppProperties가 하는 역할과 같음 — 설정 접근 창구의 단일화)


// 백엔드 API 기본 주소 (.env의 VITE_API_BASE_URL에서 읽어옴)
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;