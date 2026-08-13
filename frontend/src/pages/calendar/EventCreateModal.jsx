// 일정 생성 모달 — 클릭한 날짜가 기본값으로 들어온 폼
// props:
//   calendars : 내 캘린더 목록
//   defaultDate: 클릭한 날짜 ("2026-08-24") — 시작시간 입력의 기본값
//   onCreated  : 생성 성공 시 부모에게 알림 (만들어진 이벤트 전달)
//   onClose    : 모달 닫기
import { useState } from "react";
import { apiFetch } from "../../api";
import "./EventCreateModal.css";

function EventCreateModal({ calendars, defaultDate, onCreated, onClose }) {
  const [calendarId, setCalendarId] = useState(calendars[0].id);
  const [title, setTitle] = useState("");
  const [startsAt, setStartsAt] = useState(`${defaultDate}T12:00`); // 클릭 날짜 + 정오 기본

  const handleSubmit = () => {
    apiFetch(`/api/calendars/${calendarId}/events`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        title: title,
        startsAt: new Date(startsAt).toISOString(), // 로컬 입력 → UTC(Instant) 변환
        allDay: false,
      }),
    })
      .then((created) => {
        onCreated(created, calendarId); // 부모가 목록 갱신 + 모달 닫기
      })
      .catch((err) => {
        console.error(err);
        alert(err.message); // @Valid 400 메시지("이름을 입력하세요" 등)가 여기 뜸
      });
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-box" onClick={(e) => e.stopPropagation()}>
        <h3>일정 추가</h3>

        <div className="modal-calendar">
          <select
            value={calendarId}
            onChange={(e) => setCalendarId(Number(e.target.value))}
          >
            {calendars.map((cal) => (
              <option key={cal.id} value={cal.id}>{cal.name}</option>
            ))}
          </select>
        </div>

        <input
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="일정 제목"
        />
        <input
          type="datetime-local"
          value={startsAt}
          onChange={(e) => setStartsAt(e.target.value)}
        />

        <div className="modal-actions">
          <button onClick={onClose}>취소</button>
          <button className="modal-submit" onClick={handleSubmit}>만들기</button>
        </div>
      </div>
    </div>
  );
}

export default EventCreateModal;