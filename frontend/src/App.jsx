import LoginPage from "./pages/LoginPage";
import CalendarPage from "./pages/CalendarPage";
import { useState, useEffect } from "react";
import { API_BASE_URL } from "./config";


function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    fetch(`${API_BASE_URL}/api/users/me`, { credentials: "include" })
      .then((res) => (res.ok ? res.json() : null))
      .then((data) => setUser(data))
      .catch((err) => console.error(err));
  }, []);

  const handleLogin = () => {
    window.location.href = `${API_BASE_URL}/oauth2/authorization/google`;
  };

  return (
    <div style={{ padding: "40px", textAlign: "center" }}>
      {user ? <CalendarPage user={user} /> : <LoginPage onLogin={handleLogin} />}
    </div>
  );
}

export default App;