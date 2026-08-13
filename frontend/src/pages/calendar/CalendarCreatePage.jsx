// 캘린더 생성 마법사: 이름 → 토픽 선택 → 완료(초대 링크)
import { useState } from "react";
import { useNavigate } from "react-router";
import { apiFetch } from "../../api";
import { useTopics } from "../../hooks/useTopics";
import "./CalendarCreatePage.css";

function CalendarCreatePage() {
    const navigate = useNavigate();
    const [step, setStep] = useState(1);
    const [name, setName] = useState("");
    const [selectedTopicIds, setSelectedTopicIds] = useState([]);
    const [error, setError] = useState("");
    const { topics, error: topicsError } = useTopics();

    // 토픽 토글
    const toggleTopic = (topicId) => {
        setSelectedTopicIds((prev) =>
            prev.includes(topicId)
                ? prev.filter((id) => id !== topicId)
                : [...prev, topicId]
        );
    };

    // 캘린더 생성 (이름 / 토픽)
    const handleCreate = () => {
        apiFetch("/api/calendars", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, topicIds: selectedTopicIds }),
        })
            .then((cal) => {
                const savedIds = JSON.parse(localStorage.getItem("visibleCalendarIds") ?? "[]");
                localStorage.setItem("visibleCalendarIds", JSON.stringify([...savedIds, cal.id]));
                navigate("/");
            })
            .catch((err) => setError(err.message));
    };


    return (
        <div className="create-page">
            <div className="create-header">
                <button
                    className="back-btn"
                    onClick={() => (step === 1 ? navigate(-1) : setStep(step - 1))}
                >
                    ←
                </button>
                <h2>캘린더 만들기</h2>
                <span className="create-step">{step} / 2</span>
            </div>

            {/* 1단계 — 이름 */}
            {step === 1 && (
                <>
                    <label className="create-label">캘린더 이름</label>
                    <input
                        className="create-input"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="이름을 입력하세요"
                    />
                    <button
                        className="create-submit"
                        disabled={name.trim() === ""}
                        onClick={() => setStep(2)}
                    >
                        다음
                    </button>
                </>
            )}

            {/* 2단계 — 토픽 선택 */}
            {step === 2 && (
                <>
                    <p className="create-hint">
                        추적할 아티스트를 고르면 일정이 자동으로 수집돼요. 나중에 추가해도
                        괜찮아요.
                    </p>

                    <ul className="topic-list">
                        {topics.map((topic) => {
                            const on = selectedTopicIds.includes(topic.id);
                            return (
                                <li key={topic.id} className="topic-row">
                                    <div>
                                        <span className="topic-name">{topic.name}</span>
                                        <span className="topic-category">{topic.category}</span>
                                    </div>
                                    <button
                                        className={on ? "topic-btn topic-btn--on" : "topic-btn"}
                                        onClick={() => toggleTopic(topic.id)}
                                    >
                                        {on ? "선택됨" : "선택"}
                                    </button>
                                </li>
                            );
                        })}
                    </ul>
                    {topicsError && <p className="create-error">{topicsError}</p>}
                    {error && <p className="create-error">{error}</p>}

                    <div className="create-actions">
                        <button className="create-submit" onClick={handleCreate}>
                            만들기
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}

export default CalendarCreatePage;