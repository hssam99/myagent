import LoginPage from "./pages/LoginPage";
import CalendarPage from "./pages/CalendarPage";
import { useState, useEffect } from "react";

function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/api/users/me", { credentials: "include" })
      .then((res) => (res.ok ? res.json() : null))
      .then((data) => setUser(data))
      .catch((err) => console.error(err));
  }, []);

  const handleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  return (
    <div style={{ padding: "40px", textAlign: "center" }}>
      <h1>myagent</h1>
      {user ? <CalendarPage user={user} /> : <LoginPage onLogin={handleLogin} />}
    </div>
  );
}

export default App;