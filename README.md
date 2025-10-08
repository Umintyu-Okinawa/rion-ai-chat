<div id="top"></div>

# Rion AI Chat（Spring Boot × OpenAI × WebSocket）

OpenAI GPT API と Spring Boot を組み合わせて構築したチャットアプリケーションです。  
WebSocket（STOMP）を利用してリアルタイムな双方向通信を実現し、Docker / Render によるデプロイも対応しています。

---

## 使用技術一覧

<p style="display: inline">
  <img src="https://img.shields.io/badge/-Java-007396.svg?logo=java&style=for-the-badge">
  <img src="https://img.shields.io/badge/-Spring%20Boot-6DB33F.svg?logo=springboot&style=for-the-badge">
  <img src="https://img.shields.io/badge/-WebSocket-20232A.svg?style=for-the-badge&logo=websocket&logoColor=white">
  <img src="https://img.shields.io/badge/-OpenAI-412991.svg?logo=openai&style=for-the-badge">
  <img src="https://img.shields.io/badge/-Maven-C71A36.svg?logo=apachemaven&style=for-the-badge">
  <img src="https://img.shields.io/badge/-Docker-1488C6.svg?logo=docker&style=for-the-badge">
  <img src="https://img.shields.io/badge/-Render-46E3B7.svg?logo=render&style=for-the-badge">
</p>

---

## 目次

1. [プロジェクトについて](#プロジェクトについて)
2. [環境](#環境)
3. [ディレクトリ構成](#ディレクトリ構成)
4. [開発環境構築](#開発環境構築)
5. [環境変数一覧](#環境変数一覧)
6. [コマンド一覧](#コマンド一覧)
7. [トラブルシューティング](#トラブルシューティング)
8. [作者情報](#作者情報)

---

## プロジェクトについて

| 項目 | 内容 |
|:--|:--|
| 名称 | Rion AI Chat |
| 概要 | Spring Boot × OpenAI × WebSocket を利用したリアルタイムAIチャットアプリ |
| 目的 | AI API の利用経験を積み、リアルタイム通信・Dockerデプロイまでの流れを学習 |
| デプロイ先 | Render |
| 対応環境 | Java 21 / Maven 3.9+ |
| 公開URL | [https://rion-ai-chat.onrender.com](https://rion-ai-chat.onrender.com) |

<p align="right">(<a href="#top">トップへ</a>)</p>

---

## 環境

| カテゴリ | 使用技術・バージョン |
|:--|:--|
| 言語 | Java 21 |
| フレームワーク | Spring Boot 3.5.6 |
| 通信 | WebSocket（STOMP） |
| ビルドツール | Maven |
| API | OpenAI GPT API |
| デプロイ | Render / Docker |
| テスト | JUnit |
| ログ管理 | Logback |

<p align="right">(<a href="#top">トップへ</a>)</p>

---

## ディレクトリ構成

rion-ai-chat/
├── src/
│ ├── main/
│ │ ├── java/com/example/chat/
│ │ │ ├── controller/ # WebSocket・REST コントローラ
│ │ │ ├── service/ # OpenAI API 呼び出し・ロジック
│ │ │ ├── config/ # WebSocket・CORS設定
│ │ │ ├── model/ # DTO / メッセージモデル
│ │ │ └── RionAiChatApplication.java # メインクラス
│ │ └── resources/
│ │ ├── application.yml # 環境変数参照
│ │ └── templates/ # Thymeleafテンプレート
│ └── test/ # JUnitテスト
├── .env.example # 環境変数サンプル
├── Dockerfile # Docker構成
├── pom.xml # 依存関係・ビルド設定
└── README.md # 本書

yaml
コードをコピーする

<p align="right">(<a href="#top">トップへ</a>)</p>

---

## 開発環境構築

### 1. 事前準備

- Java 21  
- Maven 3.9 以上  
- OpenAI APIキー（[https://platform.openai.com/](https://platform.openai.com/) より取得）

### 2. `.env` の作成

```bash
OPENAI_API_KEY=sk-xxxxxxxxxxxxxxxx
OPENAI_MODEL=gpt-4o
SERVER_PORT=8080
3. application.yml（設定例）
yaml
コードをコピーする
server:
  port: ${SERVER_PORT:8080}

app:
  openai:
    apiKey: ${OPENAI_API_KEY}
    model: ${OPENAI_MODEL:gpt-4o}
    baseUrl: https://api.openai.com/v1
4. 実行
bash
コードをコピーする
# ビルド
mvn clean package -DskipTests

# 起動
java -jar target/rion-ai-chat-0.0.1-SNAPSHOT.jar
5. Docker 実行
bash
コードをコピーする
# ビルド
docker build -t rion-ai-chat .

# 実行
docker run -p 8080:8080 --env-file .env rion-ai-chat
<p align="right">(<a href="#top">トップへ</a>)</p>
環境変数一覧
変数名	説明	デフォルト値	備考
OPENAI_API_KEY	OpenAI API キー	-	.envで管理
OPENAI_MODEL	使用モデル名	gpt-4o	任意変更可
SERVER_PORT	サーバーポート番号	8080	Render環境では自動割り当て

<p align="right">(<a href="#top">トップへ</a>)</p>
コマンド一覧
コマンド	処理内容
mvn clean package -DskipTests	ビルド（テスト除外）
java -jar target/rion-ai-chat-0.0.1-SNAPSHOT.jar	アプリ起動
docker build -t rion-ai-chat .	Docker イメージのビルド
docker run -p 8080:8080 --env-file .env rion-ai-chat	Docker コンテナ起動
mvn test	JUnit テスト実行

<p align="right">(<a href="#top">トップへ</a>)</p>
トラブルシューティング
.env: no such file or directory
.env ファイルが存在しません。
上記 環境変数一覧 を参照して作成してください。

401 Unauthorized
OpenAI API キーが無効です。
.env に正しいキーを設定してください。

CORS Policy Error
CorsRegistry の設定で allowedOrigins("*") を許可してください。

TimeoutException
OpenAIのレスポンス遅延。
WebClient の timeout 設定を延長してください。

Ports are not available: address already in use
別プロセスがポートを使用中です。
使用ポートを変更または停止してください。

<p align="right">(<a href="#top">トップへ</a>)</p>
作者情報
項目	内容
名前	仲村莉穏（Rion）
GitHub	Umintyu-Okinawa
技術分野	Java / Spring Boot / WebSocket / AI連携 / Docker

公開URL
https://rion-ai-chat.onrender.com

<p align="right">(<a href="#top">トップへ</a>)</p> ```
