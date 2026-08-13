// 토픽 카탈로그 조회 훅
import { useState, useEffect } from "react";
import { apiFetch } from "../api";

export function useTopics() {
  const [topics, setTopics] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    apiFetch("/api/topics")
      .then((data) => setTopics(data))
      .catch((err) => setError(err.message));
  }, []);

  return { topics, error };
}