# Rion AI Chat（Spring Boot × OpenAI × WebSocket）

Java フレームワーク **Spring Boot** を使用し構築した、  
リアルタイムで OpenAI API と連携するチャットアプリケーションです。  
WebSocket（STOMP）を使用して双方向通信を実現し、会話データをストリームで受信できます。

---

## プロジェクト概要

| 項目 | 内容 |
|:--|:--|
| 目的 | Spring Boot と OpenAI API の連携、および WebSocket によるリアルタイム通信の実装 |
| 機能 | ChatGPT連携、ストリーミング応答、セッション管理、Dockerデプロイ対応 |
| 技術構成 | Java 21 / Spring Boot 3.5 / WebSocket / Maven / OpenAI API |
| 開発環境 | IntelliJ IDEA / VS Code / Render（デプロイ） |
| データ保存 | セッション管理（In-Memory or DB拡張可） |
| 実行形式 | jar実行 / Docker コンテナ対応 |

---

## 技術スタック

| カテゴリ | 使用技術 |
|:--|:--|
| 言語 | Java 21 |
| フレームワーク | Spring Boot 3.5.6 |
| 通信 | WebSocket（STOMP） |
| ビルド管理 | Maven |
| API | OpenAI GPT API |
| デプロイ | Render / Docker |
| 開発補助 | Lombok / Logback / JUnit |

---

## 📂 ディレクトリ構成
rion-ai-chat/
├─ src/
│ ├─ main/
│ │ ├─ java/com/example/chat/
│ │ │ ├─ controller/ # WebSocket・REST コントローラ
│ │ │ ├─ service/ # OpenAI API 呼び出し・ロジック
│ │ │ ├─ config/ # WebSocket設定・CORS設定
│ │ │ ├─ model/ # DTO / メッセージモデル
│ │ │ └─ RionAiChatApplication.java # メインクラス
│ │ └─ resources/
│ │ ├─ application.yml # APIキーなど環境変数を参照
│ │ └─ templates/ # Thymeleaf (UIを使用する場合)
│ └─ test/ # JUnitテスト
├─ .env.example # 環境変数サンプル
├─ Dockerfile # Docker構成
├─ pom.xml # 依存関係・ビルド設定
└─ README.md # 本ドキュメント

## 環境構築手順

### 1 前提条件

- **Java 21**
- **Maven 3.9+**
- **OpenAI APIキー**
  - 取得先：[https://platform.openai.com/](https://platform.openai.com/)

---

### 2 環境変数の設定（`.env`）

```bash
OPENAI_API_KEY=sk-xxxxxxxxxxxxxxxx
OPENAI_MODEL=gpt-4o
SERVER_PORT=8080
3 application.yml（設定例）
yaml
コードをコピーする
server:
  port: ${SERVER_PORT:8080}

app:
  openai:
    apiKey: ${OPENAI_API_KEY}
    model: ${OPENAI_MODEL:gpt-4o}
    baseUrl: https://api.openai.com/v1
4 実行方法
🔹 ローカル実行
bash
コードをコピーする
# 依存関係の取得とビルド
mvn clean package -DskipTests

# 起動
java -jar target/rion-ai-chat-0.0.1-SNAPSHOT.jar
・ Docker 実行
bash
コードをコピーする
# ビルド
docker build -t rion-ai-chat .

# 実行
docker run -p 8080:8080 --env-file .env rion-ai-chat
・ WebSocket 通信例
接続エンドポイント
項目	値
WebSocketエンドポイント	/ws-chat
メッセージ送信先	/app/chat
応答購読先	/topic/reply

JavaScript クライアント例
javascript
コードをコピーする
const socket = new SockJS('/ws-chat');
const stomp = Stomp.over(socket);

stomp.connect({}, () => {
  // サブスクライブ
  stomp.subscribe('/topic/reply', (msg) => {
    const body = JSON.parse(msg.body);
    console.log('AI応答:', body.content);
  });

  // メッセージ送信
  stomp.send('/app/chat', {}, JSON.stringify({ content: 'こんにちは！' }));
});
 ・実装のポイント
項目	説明
WebSocket通信	STOMPを使用してサーバー→クライアント間でリアルタイム更新
OpenAI連携	RestTemplate / WebClient 経由でリクエスト送信
ストリーミング	部分的な応答を順次受信し、UIへリアルタイム出力
例外処理	APIエラー・ネットワーク障害時の再試行処理
セキュリティ	.env によるAPIキー管理、CORS制御、HTTPS前提設計
ログ管理	Logback による送受信ログ・リクエスト監視
拡張性	会話履歴をDB化、認証導入、RAG検索統合も容易

・テスト
bash
コードをコピーする
mvn test
OpenAI 呼び出しを Mock 化し、応答フォーマットを検証

WebSocket 接続の統合テスト（@SpringBootTest(webEnvironment = RANDOM_PORT)）

・デプロイ手順（Render例）
GitHubリポジトリをRenderに接続

“New Web Service” → Environment: Docker

環境変数を設定

ini
コードをコピーする
OPENAI_API_KEY=sk-xxxx
OPENAI_MODEL=gpt-4o
SERVER_PORT=10000
自動デプロイ後、https://rion-ai-chat.onrender.com にアクセス

・よくあるエラーと解決法
エラー内容	原因と対処
401 Unauthorized	APIキーが無効または未設定。.envを確認。
CORS Policy Error	CorsRegistryの設定でallowedOrigins("*")を許可。
WebSocket接続失敗	/ws-chat エンドポイントまたはポートの不一致。
TimeoutException	OpenAIレスポンス遅延。WebClient の timeout 設定を延長。

・今後の拡張予定
Chat履歴をDBに保存（User別セッション管理）

Spring Securityによるユーザー認証

RAG構成（ファイルアップロード＋ベクトル検索）

Function Calling / Tool Calling の導入

フロントエンド統合（React or Vue）

・作者情報
項目	内容
名前	仲村莉穏（Rion）
GitHub	Umintyu-Okinawa
技術分野	Java / Spring Boot / AI連携 / データ処理 / Web開発

🌐 公開URL（任意）
https://rion-ai-chat.onrender.com
