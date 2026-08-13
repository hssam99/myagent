// 캘린더 설정 — 멤버 초대 / 이름 변경 / 토픽 관리
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import { apiFetch } from "../../api";
import "./CalendarSettingsPage.css";
import DangerConfirm from "../../components/DangerConfirm";

function CalendarSettingsPage() {
  const navigate = useNavigate();
  const { calendarId } = useParams();
  const [calendar, setCalendar] = useState(null);
  const [name, setName] = useState("");
  const [error, setError] = useState("");
  const [saved, setSaved] = useState(false);

  // 캘린더 가져오기
  useEffect(() => {
    apiFetch(`/api/calendars/${calendarId}`)
      .then((data) => {
        setCalendar(data);
        setName(data.name);
      })
      .catch((err) => setError(err.message));
  }, [calendarId]);

  const handleRename = () => {
    apiFetch(`/api/calendars/${calendarId}`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name }),
    })
      .then((updated) => {
        setCalendar(updated);
        setSaved(true);
        setError("");
      })
      .catch((err) => setError(err.message));
  };

  const handleDelete = () => {
    apiFetch(`/api/calendars/${calendarId}`, { method: "DELETE" })
      .then(() => {
        // 표시 목록에서도 제거
        const savedIds = JSON.parse(localStorage.getItem("visibleCalendarIds") ?? "[]");
        localStorage.setItem(
          "visibleCalendarIds",
          JSON.stringify(savedIds.filter((id) => id !== Number(calendarId)))
        );
        navigate("/");
      })
      .catch((err) => setError(err.message));
  };

  const handleLeave = () => {
    if (!confirm("이 캘린더에서 나갈까요?")) return;
    apiFetch(`/api/calendars/${calendarId}/members/me`, { method: "DELETE" })
      .then(() => navigate("/"))
      .catch((err) => setError(err.message));
  };

  const inviteLink = calendar
    ? `${window.location.origin}/invite/${calendar.inviteCode}`
    : "";

  return (
    <div className="settings-page">
      <div className="settings-header">
        <button className="back-btn" onClick={() => navigate(-1)}>←</button>
      </div>

      {error && <p className="settings-error">{error}</p>}

      {/* 멤버 */}
      <p className="settings-label">멤버</p>
      <div className="settings-card">
        <button
          className="settings-invite"
          disabled={!calendar}
          onClick={() => {
            navigator.clipboard.writeText(inviteLink);
            alert("초대 링크를 복사했어요");
          }}
        >
          멤버 초대하기
        </button>
      </div>

      {/* 캘린더 정보 */}
      <p className="settings-label">캘린더 정보</p>
      <div className="settings-card">
        <div className="settings-row">
          <span>캘린더 이름</span>
          <input
            className="settings-input"
            value={name} // 기존 이름
            onChange={(e) => {
              setName(e.target.value); // 타이핑할 때마다 name 갱신
              setSaved(false); // "저장됨" 표시 지우기
            }}
          />
        </div>
        <button
          className="settings-save"
          disabled={!calendar || name === calendar.name || name.trim() === ""}
          onClick={handleRename}
        >
          {saved ? "저장됨" : "저장"}
        </button>
      </div>

      {/* 토픽 */}
      <p className="settings-label">토픽 팔로우</p>
      <div className="settings-card">
        <button
          className="settings-row settings-link"
          onClick={() => navigate(`/calendars/${calendarId}/topics`)}
        >
          <span>아티스트 팔로우</span>
          <span className="settings-arrow">›</span>
        </button>
      </div>
      {/* 위험 구역 */}
      <p className="settings-label"></p>
      <div className="settings-card">
        {calendar?.owner ? (
          <DangerConfirm
            label="캘린더 삭제"
            confirmText={calendar.name}
            description="삭제하면 이 캘린더의 일정과 팔로우가 모두 사라지고 되돌릴 수 없어요."
            onConfirm={handleDelete}
          />
        ) : (
          <button className="settings-danger" onClick={handleLeave}>
            캘린더 나가기
          </button>
        )}
      </div>
    </div>
  );
}

export default CalendarSettingsPage;