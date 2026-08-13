// 캘린더별 색상 — id로 팔레트에서 자동 배정 (색 고르기 UI는 나중에)
const PALETTE = ["#3b82f6", "#ef4444", "#10b981", "#f59e0b", "#8b5cf6", "#ec4899"];

export function calendarColor(calendarId) {
  return PALETTE[calendarId % PALETTE.length];
}