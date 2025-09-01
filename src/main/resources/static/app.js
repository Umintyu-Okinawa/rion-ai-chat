let stomp;
const $ = (id) => document.getElementById(id);
const messagesEl = $("messages");
const statusDot = $("statusDot");
const statusText = $("statusText");

// 名前の保存（再読込しても保持）
$("name").value = localStorage.getItem("name") || "";

// ユーティリティ
function setStatus(connected) {
  statusDot.className =
    "w-2.5 h-2.5 rounded-full " +
    (connected ? "bg-emerald-500" : "bg-zinc-600");
  statusText.textContent = connected ? "connected" : "disconnected";
}
function timeStr(ts) {
  return new Date(ts).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });
}

/**
 * 中央寄せバージョン
 * - 行全体は中央寄せ（wrap: justify-center）
 * - バブルは最大幅を80%にし、PCでは 40rem を上限にして読みやすく
 * - 自分/相手で色を変えるのは今まで通り
 */
function addMessage({ sender, content, at }, isSelf) {
  // 行ラッパー（中央寄せ）
  const wrap = document.createElement("div");
  wrap.className = "flex justify-center px-2";

  // バブル
  const bubble = document.createElement("div");
  bubble.className =
    // 幅と見た目の共通スタイル
    "w-full max-w-[80%] md:max-w-[40rem] rounded-2xl px-4 py-2 shadow " +
    // 自分/相手で配色だけ変える
    (isSelf
      ? "bg-zinc-200 text-zinc-950"
      : "bg-zinc-900/70 border border-zinc-800");

  const meta = document.createElement("div");
  meta.className = "text-[10px] opacity-70 mb-0.5";
  meta.textContent = `${sender ?? "anon"}・${timeStr(at ?? Date.now())}`;

  const body = document.createElement("div");
  body.className =
    "text-base md:text-lg leading-relaxed whitespace-pre-wrap break-words";
  body.textContent = content ?? "";

  bubble.appendChild(meta);
  bubble.appendChild(body);
  wrap.appendChild(bubble);
  messagesEl.appendChild(wrap);

  // 自動スクロール
  messagesEl.scrollTop = messagesEl.scrollHeight;
}

// WebSocket 接続
function connect() {
  const sock = new SockJS("/ws");
  stomp = Stomp.over(sock);
  stomp.debug = null;

  stomp.connect(
    {},
    () => {
      setStatus(true);
      // 受信購読
      stomp.subscribe("/topic/room.general", (frame) => {
        const m = JSON.parse(frame.body);
        const me =
          (m.sender || "") ===
          ($("name").value || localStorage.getItem("name"));
        addMessage({ ...m, at: m.at || Date.now() }, me);
      });
    },
    () => setStatus(false)
  );
}

// 送信（Enter=送信 / Shift+Enter=改行）
$("composer").addEventListener("submit", (e) => {
  e.preventDefault();
  const sender = $("name").value.trim() || "anon";
  const content = $("msg").value.trim();
  if (!content) return;

  localStorage.setItem("name", sender);
  stomp?.send(
    "/app/chat",
    {},
    JSON.stringify({
      roomId: "general",
      sender,
      content,
      at: Date.now(),
    })
  );
  $("msg").value = "";
  $("msg").focus();
});
$("msg").addEventListener("keydown", (e) => {
  if (e.key === "Enter" && !e.shiftKey) {
    e.preventDefault();
    $("send").click();
  }
});

connect();
