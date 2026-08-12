import { useState, useEffect } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/react/daygrid";
import classicThemePlugin from "@fullcalendar/react/themes/classic";

import "@fullcalendar/react/skeleton.css";
import "@fullcalendar/react/themes/classic/theme.css";
import "@fullcalendar/react/themes/classic/palette.css";

import { apiFetch } from "../api";

function CalendarPage() {
    const [calendars, setCalendars] = useState([]);
    const [selectedCalendar, setSelectedCalendar] = useState(null);
    const [events, setEvents] = useState([]);
    const [showCreate, setShowCreate] = useState(false);
    const [newName, setNewName] = useState("");

    // 캘린더 목록 조회
    useEffect(() => {
        apiFetch("/api/calendars")
            .then((data) => {
                setCalendars(data);

                if (data.length > 0) {
                    setSelectedCalendar(data[0]);
                }
            })
            .catch((err) => console.error(err));
    }, []);

    // 선택된 캘린더의 일정 조회
    useEffect(() => {
        if (!selectedCalendar) return;

        apiFetch(`/api/calendars/${selectedCalendar.id}/events`)
            .then((data) => {
                setEvents(data);
            })
            .catch((err) => console.error(err));
    }, [selectedCalendar]);

    // 캘린더 생성
    const handleCreateCalendar = () => {
        apiFetch("/api/calendars", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: newName,
            }),
        })
            .then((created) => {
                setCalendars((prev) => [...prev, created]);
                setNewName("");
                setShowCreate(false);
            })
            .catch((err) => console.error(err));
    };

    // EventResponse → FullCalendar
    const calendarEvents = events.map((e) => ({
        id: String(e.id),
        title: e.title,
        start: e.startsAt,
        end: e.endsAt ?? undefined,
        allDay: e.allDay,
    }));

    return (
        <div className="calendar-page">

            {/* 상단 */}
            <div className="calendar-topbar">

                <select
                    className="calendar-select"
                    value={selectedCalendar?.id ?? ""}
                    onChange={(e) => {
                        const cal = calendars.find(
                            (c) => c.id === Number(e.target.value)
                        );

                        setSelectedCalendar(cal);
                    }}
                >
                    {calendars.map((cal) => (
                        <option key={cal.id} value={cal.id}>
                            {cal.name}
                        </option>
                    ))}
                </select>

                {/* TODO: 모달로 빼기 */}
                <button
                    className="chip-btn"
                    onClick={() => setShowCreate((prev) => !prev)}
                >
                    + 캘린더 만들기
                </button>

                <div className="topbar-right">
                    <button
                        className="icon-btn"
                        onClick={() => alert("검색은 준비 중입니다")}
                    >
                        🔍
                    </button>

                    <button
                        className="icon-btn"
                        onClick={() => alert("필터는 준비 중입니다")}
                    >
                        ⚙️
                    </button>
                </div>
            </div>

            {/* 캘린더 생성 */}
            {showCreate && (
                <div className="calendar-create-form">
                    <input
                        value={newName}
                        onChange={(e) => setNewName(e.target.value)}
                        placeholder="새 캘린더 이름"
                    />

                    <button onClick={handleCreateCalendar}>
                        만들기
                    </button>
                </div>
            )}

            {/* FullCalendar */}
            <FullCalendar
                plugins={[
                    dayGridPlugin,
                    classicThemePlugin,
                ]}
                initialView="dayGridMonth"
                locale="ko"
                height="auto"
                headerToolbar={{
                    left: "title",
                    center: "",
                    right: "prev,next",
                }}
                events={calendarEvents}
            />

            {/* 하단 */}
            <div className="calendar-bottombar">

                <button
                    className="round-btn"
                    onClick={() => alert("메뉴는 준비 중입니다")}
                >
                    ≡
                </button>

                <div>
                    <button
                        className="round-btn"
                        onClick={() => alert("일정 등록은 2단계!")}
                    >
                        ＋
                    </button>

                    <button
                        className="round-btn"
                        onClick={() => alert("inbox는 준비 중입니다")}
                    >
                        📥
                    </button>
                </div>

            </div>
        </div>
    );
}

export default CalendarPage;