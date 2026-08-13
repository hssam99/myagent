// 위험한 작업 확인 — 지정된 문자열을 정확히 입력해야 실행됨
// props:
//   label        : 평소 버튼 텍스트 ("캘린더 삭제", "회원 탈퇴")
//   confirmText  : 사용자가 그대로 입력해야 하는 값 (캘린더 이름, 닉네임 등)
//   description  : 경고 문구
//   confirmLabel : 최종 실행 버튼 텍스트 (기본 "영구 삭제")
//   onConfirm    : 확인되면 실행할 함수
import { useState } from "react";
import "./DangerConfirm.css";

function DangerConfirm({ label, confirmText, description, confirmLabel = "영구 삭제", onConfirm }) {
  const [open, setOpen] = useState(false);
  const [input, setInput] = useState("");

  if (!open) {
    return (
      <button className="danger-btn" onClick={() => setOpen(true)}>
        {label}
      </button>
    );
  }

  return (
    <>
      <p className="danger-desc">
        {description}
        <br />
        계속하려면 <b>{confirmText}</b> 을(를) 입력해주세요.
      </p>

      <input
        className="danger-input"
        value={input}
        onChange={(e) => setInput(e.target.value)}
        placeholder={confirmText}
      />

      <div className="danger-actions">
        <button
          className="danger-cancel"
          onClick={() => {
            setOpen(false);
            setInput("");
          }}
        >
          취소
        </button>
        <button
          className="danger-confirm"
          disabled={input !== confirmText}
          onClick={onConfirm}
        >
          {confirmLabel}
        </button>
      </div>
    </>
  );
}

export default DangerConfirm;