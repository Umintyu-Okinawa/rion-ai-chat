<div id="top"></div>

# Rion AI Chat (Spring Boot × OpenAI × WebSocket)

OpenAI GPT API と Spring Boot を組み合わせて構築したチャットアプリケーションです。  
WebSocket (STOMP) によるリアルタイム双方向通信に対応し、Docker / Render でのデプロイも可能です。

---

## 使用技術一覧

<p style="display:inline">
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
3. [主要ディレクトリ](#主要ディレクトリ)
4. [開発環境構築](#開発環境構築)
5. [環境変数一覧](#環境変数一覧)
6. [コマンド一覧](#コマンド一覧)
7. [トラブルシューティング](#トラブルシューティング)
8. [作者情報](#作者情報)
9. [公開URL](#公開url)

---

## プロジェクトについて

| 項目 | 内容 |
|:--|:--|
| 名称 | Rion AI Chat |
| 概要 | Spring Boot × OpenAI × WebSocket を利用したリアルタイム AI チャットアプリ |
| 目的 | AI API 利用、リアルタイム通信、Docker デプロイまでの一連の実装学習 |
| デプロイ先 | Render |
| 対応環境 | Java 21 / Maven 3.9+ |
| 公開URL | https://rion-ai-chat.onrender.com |

---

## 環境

| カテゴリ | 使用技術・バージョン |
|:--|:--|
| 言語 | Java 21 |
| フレームワーク | Spring Boot 3.5.6 |
| 通信 | WebSocket (STOMP) |
| ビルドツール | Maven |
| API | OpenAI GPT API |
| デプロイ | Render / Docker |
| テスト | JUnit |
| ログ管理 | Logback |

---

## 主要ディレクトリ

| パス | 役割 |
|:--|:--|
| `src/main/java/com/example/chat/controller/` | WebSocket・REST コントローラ |
| `src/main/java/com/example/chat/service/` | OpenAI API 呼び出し・業務ロジック |
| `src/main/java/com/example/chat/config/` | WebSocket・CORS 設定 |
| `src/main/java/com/example/chat/model/` | DTO / メッセージモデル |
| `src/main/java/com/example/chat/RionAiChatApplication.java` | メインクラス |
| `src/main/resources/application.yml` | 設定ファイル（環境変数参照） |
| `src/main/resources/templates/` | Thymeleaf テンプレート（UI使用時） |
| `src/test/` | JUnit テスト |
| `.env.example` | 環境変数サンプル |
| `Dockerfile` / `pom.xml` | コンテナ構成・依存関係管理 |

---

## 開発環境構築

### 1. 事前準備

- Java 21  
- Maven 3.9 以上  
- OpenAI API キー（取得先: https://platform.openai.com/）

### 2. `.env` の作成

```bash
OPENAI_API_KEY=sk-xxxxxxxxxxxxxxxx
OPENAI_MODEL=gpt-4o
SERVER_PORT=8080
3. application.yml 設定例
yaml
コードをコピーする
server:
  port: ${SERVER_PORT:8080}

app:
  openai:
    apiKey: ${OPENAI_API_KEY}
    model: ${OPENAI_MODEL:gpt-4o}
    baseUrl: https://api.openai.com/v1
4. 実行（ローカル）
bash
コードをコピーする
mvn clean package -DskipTests
java -jar target/rion-ai-chat-0.0.1-SNAPSHOT.jar
5. Docker 実行
bash
コードをコピーする
docker build -t rion-ai-chat .
docker run -p 8080:8080 --env-file .env rion-ai-chat
環境変数一覧
変数名	説明	デフォルト値	備考
OPENAI_API_KEY	OpenAI API キー	なし	.env で管理
OPENAI_MODEL	使用モデル名	gpt-4o	任意に変更可能
SERVER_PORT	サーバーポート番号	8080	Render 環境では自動割り当てあり

コマンド一覧
コマンド	説明
mvn clean package -DskipTests	ビルド（テスト除外）
java -jar target/rion-ai-chat-0.0.1-SNAPSHOT.jar	アプリ起動
docker build -t rion-ai-chat .	Docker イメージビルド
docker run -p 8080:8080 --env-file .env rion-ai-chat	コンテナ起動
mvn test	JUnit テスト実行

トラブルシューティング
.env: no such file or directory
.env ファイルが存在しません。上記の「環境変数一覧」を参考に作成してください。

401 Unauthorized
OpenAI API キーが無効または未設定です。.env の値を確認してください。

CORS Policy Error
CorsRegistry の allowedOrigins("*") を設定してください（開発環境では *、本番は限定ドメイン推奨）。

TimeoutException
OpenAI のレスポンスが遅延しています。WebClient の timeout を延長、またはプロンプト・モデルを調整してください。

Ports are not available: address already in use
他のプロセスがポートを使用中です。使用ポートを変更するか、該当プロセスを停止してください。

作者情報
項目	内容
名前	仲村莉穏（Rion）
GitHub	Umintyu-Okinawa
技術分野	Java / Spring Boot / WebSocket / AI Integration / Docker

公開URL
https://rion-ai-chat.onrender.com
