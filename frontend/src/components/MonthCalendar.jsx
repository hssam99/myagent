// 월간 달력 어댑터 — FullCalendar를 이 파일 안에만 가둔다
// - 바깥(CalendarPage)은 이 컴포넌트의 props(events, onDateClick)만 알면 됨
// - 나중에 직접 만든 달력으로 교체할 때 이 파일 내부만 갈아끼우면 끝
// - 콜백으로 라이브러리 고유 객체(info)를 흘리지 않고 필요한 값만 번역해서 전달
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/react/daygrid";
import interactionPlugin from "@fullcalendar/react/interaction"; // dateClick에 필요
import classicThemePlugin from "@fullcalendar/react/themes/classic";
import "@fullcalendar/react/skeleton.css";
import "@fullcalendar/react/themes/classic/theme.css";
import "@fullcalendar/react/themes/classic/palette.css";

function MonthCalendar({ events, onDateClick }) {
  // EventResponse → FullCalendar 형식 변환
  const calendarEvents = events.map((e) => ({
    id: String(e.id),
    title: e.title,
    start: e.startsAt, // UTC ISO 문자열 → 로컬 시간으로 표시됨
    end: e.endsAt ?? undefined,
    allDay: e.allDay,
    color: e.color,
  }));

  return (
    <FullCalendar
      plugins={[dayGridPlugin, interactionPlugin, classicThemePlugin]}
      initialView="dayGridMonth" // FullCalendar는 월간/주간/일간 뷰가 있는데, 시작을 월간(dayGridMonth)으로.
      locale="ko" // 언어. "8월", "일 월 화..." 같은 표기를 한국어
      height="auto"
      headerToolbar={{ left: "title", center: "", right: "prev,next" }} //TODO: 추후 커스터마이징
      events={calendarEvents}
      dateClick={(info) => onDateClick(info.dateStr)} // 라이브러리 객체 대신 "2026-08-24" 문자열만
    />
  );
}

export default MonthCalendar;