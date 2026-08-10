import { useState, useEffect } from "react";

function App() {
  const [user, setUser] = useState(null);
  const [calendars, setCalendars] = useState([]);   // 캘린더 목록 담을 상자
  const [newName, setNewName] = useState("");   // 새 캘린더 이름 입력값

  useEffect(() => {
    fetch("http://localhost:8080/api/users/me", { credentials: "include" })
      .then((res) => (res.ok ? res.json() : null))
      .then((data) => setUser(data))
      .catch((err) => console.error(err));
  }, []);

  // user가 생기면(로그인되면) 캘린더 목록도 불러오기
  useEffect(() => {
    if (!user) return;
    fetch("http://localhost:8080/api/calendars", { credentials: "include" })
      .then((res) => (res.ok ? res.json() : []))
      .then((data) => setCalendars(data))
      .catch((err) => console.error(err));
  }, [user]);            // ← user가 바뀔 때마다 실행

  const handleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  const handleCreate = () => {
    fetch("http://localhost:8080/api/calendars", {
      method: "POST",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: newName }),
    })
      .then(async (res) => {
        const data = await res.json();

        if (!res.ok) {
          throw new Error(data.message);
        }

        return data;
      })
      .then((created) => {
        setCalendars((prev) => [...prev, created]);
        setNewName("");
      })
      .catch((err) => {
        console.error(err);
      });
  };

  return (
    <div style={{ padding: "40px", textAlign: "center" }}>
      <h1>myagent</h1>
      {user ? (
        <div>
          <p>{user.nickname}님, 환영합니다!</p>
          <h2>내 캘린더</h2>
          <div>
            <input
              value={newName}
              onChange={(e) => setNewName(e.target.value)}
              placeholder="새 캘린더 이름"
            />
            <button onClick={handleCreate}>만들기</button>
          </div>
          <ul style={{ listStyle: "none", padding: 0 }}>
            {calendars.map((cal) => {
              return (
                <li key={cal.id}>{cal.name}</li>
              );
            })}
          </ul>

        </div>
      ) : (
        <button onClick={handleLogin}>구글로 로그인</button>
      )}
    </div>
  );
}

export default App;