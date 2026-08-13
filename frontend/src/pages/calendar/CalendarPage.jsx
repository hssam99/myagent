import { useState, useEffect } from "react";
import { useNavigate } from "react-router";
import MonthCalendar from "../../components/MonthCalendar";
import EventCreateModal from "./EventCreateModal";
import { calendarColor } from "../../utils/calendarColor";
import { apiFetch } from "../../api";

import "./CalendarPage.css";

const VISIBLE_KEY = "visibleCalendarIds"; // 노출할 캘린더

function CalendarPage() {
    const navigate = useNavigate();
    const [calendars, setCalendars] = useState([]); // 사용자의 캘린더
    const [visibleCalendarIds, setVisibleCalendarIds] = useState([]); // 보이는 캘린더 id
    const [events, setEvents] = useState([]);
    const [modalDate, setModalDate] = useState(null);

    // 캘린더 목록 조회
    useEffect(() => {
        apiFetch("/api/calendars")
            .then((data) => {
                setCalendars(data);
                const saved = JSON.parse(localStorage.getItem(VISIBLE_KEY) ?? "[]");
                setVisibleCalendarIds(saved.filter((id) => data.some((c) => c.id === id)));
            })
            .catch((err) => console.error(err));
    }, []);

    // visible 캘린더들의 일정 조회
    useEffect(() => {
        Promise.all(
            visibleCalendarIds.map((calendarId) =>
                apiFetch(`/api/calendars/${calendarId}/events`).then((list) =>
                    // 어느 캘린더 일정인지 + 색 (백엔드 응답엔 없는 정보)
                    list.map((e) => ({ ...e, calendarId, color: calendarColor(calendarId) }))
                )
            )
        )
            .then((results) => setEvents(results.flat()))
            .catch((err) => console.error(err));
    }, [visibleCalendarIds]);

    const toggleVisible = (calendarId) => {
        setVisibleCalendarIds((prev) => {
            const next = prev.includes(calendarId)
                ? prev.filter((id) => id !== calendarId)
                : [...prev, calendarId];
            localStorage.setItem(VISIBLE_KEY, JSON.stringify(next));
            return next;
        });
    };
    return (
        <div className="calendar-page">
            {/* 상단 — 캘린더 토글 칩 */}
            <div className="calendar-topbar">
                <div className="calendar-chips">
                    {calendars.map((cal) => {
                        const on = visibleCalendarIds.includes(cal.id);
                        return (
                            <div key={cal.id} className={on ? "cal-chip cal-chip--on" : "cal-chip"}>
                                {/* 캘린더 토글 */}
                                <button className="cal-chip-main" onClick={() => toggleVisible(cal.id)}>
                                    <span className="cal-dot" style={{ background: calendarColor(cal.id) }} />
                                    {cal.name}
                                </button>
                                {/* 캘린더별 설정 */}
                                <button
                                    className="cal-chip-gear"
                                    onClick={() => navigate(`/calendars/${cal.id}/settings`)}
                                >
                                    ⚙
                                </button>
                            </div>
                        );
                    })}
                </div>

                <div className="topbar-right">
                    <button className="icon-btn" onClick={() => alert("검색은 준비 중입니다")}>🔍</button>
                    <button className="icon-btn" onClick={() => alert("필터는 준비 중입니다")}>⚙️</button>
                </div>
            </div>

            <div className="calendar-actions">
                <button className="chip-btn" onClick={() => navigate("/calendars/new")}>
                    + 캘린더 만들기
                </button>
            </div>

            <MonthCalendar
                events={events}
                onDateClick={(dateStr) => {
                    if (visibleCalendarIds.length === 0) return;   // 켜진 캘린더 없으면 무시
                    setModalDate(dateStr);
                }}
            />

            {/* 하단 */}
            <div className="calendar-bottombar">
                <button className="round-btn" onClick={() => alert("메뉴는 준비 중입니다")}>≡</button>

                <div className="topbar-right">
                    <button
                        className="round-btn"
                        disabled={visibleCalendarIds.length === 0}
                        onClick={() => {
                            const d = new Date();
                            const today = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`;
                            setModalDate(today); // 로컬(내 시간대) 기준 오늘
                        }}
                    >
                        ＋
                    </button>

                    <button className="round-btn" onClick={() => alert("inbox는 준비 중입니다")}>📥</button>
                </div>
            </div>

            {/* 일정 생성 모달 */}
            {modalDate && visibleCalendarIds.length > 0 && (
                <EventCreateModal
                    calendars={calendars.filter((c) => visibleCalendarIds.includes(c.id))}
                    defaultDate={modalDate}
                    onCreated={(created, calendarId) => {
                        setEvents((prev) => [
                            ...prev,
                            { ...created, calendarId, color: calendarColor(calendarId) },
                        ]);
                        setModalDate(null);
                    }}
                    onClose={() => setModalDate(null)}
                />
            )}
        </div>
    );
}

export default CalendarPage;