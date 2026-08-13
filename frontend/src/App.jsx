import { Routes, Route } from "react-router";
import { useState, useEffect } from "react";

import LoginPage from "./pages/LoginPage";
import CalendarPage from "./pages/calendar/CalendarPage";
import CalendarCreatePage from "./pages/calendar/CalendarCreatePage";
import CalendarSettingsPage from "./pages/calendar/CalendarSettingsPage";
import TopicsPage from "./pages/TopicsPage"

import { apiFetch } from "./api";
import { API_BASE_URL } from "./config";


function App() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiFetch("/api/users/me")
      .then((data) => setUser(data))
      .catch(() => setUser(null)) // 401 = 로그인 안 됨
      .finally(() => setLoading(false));
  }, []);

  const handleLogin = (provider) => {
    window.location.href = `${API_BASE_URL}/oauth2/authorization/${provider}`;
  };

  if (loading) return null;
  if (!user) return <LoginPage onLogin={handleLogin} />;

  return (
    <Routes>
      <Route path="/" element={<CalendarPage />} />
      <Route path="/calendars/new" element={<CalendarCreatePage />} />
      <Route path="/calendars/:calendarId/settings" element={<CalendarSettingsPage />} />
      <Route path="/calendars/:calendarId/topics" element={<TopicsPage />} />
    </Routes>
  );
}

export default App;