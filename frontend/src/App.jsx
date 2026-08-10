import { useState, useEffect } from "react";

function App() {
  const [user, setUser] = useState(null);
  const [calendars, setCalendars] = useState([]);   // ← 캘린더 목록 담을 상자

  useEffect(() => {
    fetch("http://localhost:8080/api/users/me", { credentials: "include" })
      .then((res) => (res.ok ? res.json() : null))
      .then((data) => setUser(data))
      .catch((err) => console.error(err));
  }, []);

  // user가 생기면(로그인되면) 캘린더 목록도 불러오기
  useEffect(() => {
    if (!user) return;   // 로그인 안 됐으면 아무것도 안 함
    fetch("http://localhost:8080/api/calendars", { credentials: "include" })
      .then((res) => (res.ok ? res.json() : []))
      .then((data) => setCalendars(data))
      .catch((err) => console.error(err));
  }, [user]);            // ← user가 바뀔 때마다 실행

  const handleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  return (
    <div style={{ padding: "40px", textAlign: "center" }}>
      <h1>myagent</h1>
      {user ? (
        <div>
          <p>{user.nickname}님, 환영합니다!</p>
          <h2>내 캘린더</h2>
          <ul style={{ listStyle: "none", padding: 0 }}>
            {calendars.map((cal) => (
              <li key={cal.id}>{cal.name}</li>
            ))}
          </ul>
        </div>
      ) : (
        <button onClick={handleLogin}>구글로 로그인</button>
      )}
    </div>
  );
}

export default App;