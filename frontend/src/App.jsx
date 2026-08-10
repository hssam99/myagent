import { useState, useEffect } from "react";

function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/api/users/me", {
      credentials: "include",
    })
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
      {user ? (
        <p>{user.nickname}님, 환영합니다!</p>
      ) : (
        <button onClick={handleLogin}>구글로 로그인</button>
      )}
    </div>
  );
}

export default App;