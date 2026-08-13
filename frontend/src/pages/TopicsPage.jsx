// 아티스트(토픽) 팔로우 페이지
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import { apiFetch } from "../api";
import "./TopicsPage.css";

function TopicsPage() {
    const navigate = useNavigate();
    const { calendarId } = useParams(); // 주소의 :calendarId
    const [topics, setTopics] = useState([]); // 전체 카탈로그
    const [followedIds, setFollowedIds] = useState([]); // 이 캘린더가 팔로우 중인 id들
    const [keyword, setKeyword] = useState("");
    const [error, setError] = useState("");

    // 카탈로그 조회
    useEffect(() => {
        apiFetch("/api/topics")
            .then((data) => setTopics(data))
            .catch((err) => setError(err.message));
    }, []);

    // 이 캘린더가 팔로우 중인 토픽 조회
    useEffect(() => {
        apiFetch(`/api/calendars/${calendarId}/topics`)
            .then((data) => setFollowedIds(data.map((t) => t.id)))
            .catch((err) => setError(err.message));
    }, [calendarId]);

    const toggle = (topicId) => {
        const followed = followedIds.includes(topicId);

        apiFetch(`/api/calendars/${calendarId}/topics/${topicId}`, {
            method: followed ? "DELETE" : "PUT",
        })
            .then(() => {
                setFollowedIds((prev) =>
                    followed ? prev.filter((id) => id !== topicId) : [...prev, topicId]
                );
                setError("");
            })
            .catch((err) => setError(err.message));
    };

    const visible = topics.filter((t) =>
        t.name.toLowerCase().includes(keyword.toLowerCase())
    );

    return (
        <div className="topics-page">
            <div className="topics-header">
                <button className="back-btn" onClick={() => navigate(-1)}>
                    ←
                </button>
                <h2>아티스트 팔로우</h2>
            </div>

            <p className="topics-hint">등록한 아티스트의 일정이 자동으로 수집돼요</p>

            <input
                className="topics-search"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                placeholder="아티스트 검색"
            />

            {error && <p className="topics-error">{error}</p>}

            <ul className="topic-list">
                {visible.map((topic) => {
                    const followed = followedIds.includes(topic.id);
                    return (
                        <li key={topic.id} className="topic-row">
                            <div>
                                <span className="topic-name">{topic.name}</span>
                                <span className="topic-category">{topic.category}</span>
                            </div>
                            <button
                                className={followed ? "topic-btn topic-btn--on" : "topic-btn"}
                                onClick={() => toggle(topic.id)}
                            >
                                {followed ? "팔로우중" : "팔로우"}
                            </button>
                        </li>
                    );
                })}
            </ul>
        </div>
    );
}

export default TopicsPage;