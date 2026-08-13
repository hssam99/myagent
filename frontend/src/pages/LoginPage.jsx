import "./LoginPage.css"; // CSS를 import하면 이 컴포넌트가 쓰일 때 스타일도 적용됨

function LoginPage({ onLogin }) {
  return (
    <div className="login-page">
      <h1 className="login-logo">myagent</h1>
      <p className="login-tagline">
        <div>아티스트 스케줄을</div>
        <div>관리하는 AI 캘린더</div>
      </p>

      <div className="login-buttons">
        <button
          className="login-btn login-btn--apple"
          onClick={() => alert("Apple 로그인은 준비 중입니다")}
        >
          Apple로 가입하기
        </button>
        <button
          className="login-btn login-btn--google"
          onClick={() => onLogin("google")}>
          Google로 시작하기
        </button>
      </div>
    </div>
  );
}

export default LoginPage;