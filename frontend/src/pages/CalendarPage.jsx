import { useState, useEffect } from "react";

function CalendarPage({ user }) {
  const [calendars, setCalendars] = useState([]);
  const [newName, setNewName] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/api/calendars", { credentials: "include" })
      .then((res) => (res.ok ? res.json() : []))
      .then((data) => setCalendars(data))
      .catch((err) => console.error(err));
  }, []);

  const handleCreate = () => {
    fetch("http://localhost:8080/api/calendars", {
      method: "POST",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: newName }),
    })
      .then(async (res) => {
        const data = await res.json();
        if (!res.ok) throw new Error(data.message);
        return data;
      })
      .then((created) => {
        setCalendars((prev) => [...prev, created]);
        setNewName("");
      })
      .catch((err) => console.error(err));
  };

  return (
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
        {calendars.map((cal) => (
          <li key={cal.id}>{cal.name}</li>
        ))}
      </ul>
    </div>
  );
}

export default CalendarPage;