function LoginPage({ onLogin }) {
  return (
    <div>
      <p>내 아티스트의 스케줄을 챙겨주는 팬덤 공유 캘린더</p>
      <button onClick={onLogin}>구글로 로그인</button>
    </div>
  );
}

export default LoginPage;