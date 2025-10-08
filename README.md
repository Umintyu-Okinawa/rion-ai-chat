<div id="top"></div>

# Rion AI Chat (Spring Boot × OpenAI × WebSocket)

OpenAI GPT API と Spring Boot を組み合わせて構築したチャットアプリケーションです。
**WebSocket (STOMP)** によるリアルタイム双方向通信に対応し、**Docker / Render** でのデプロイも可能です。

---

## 使用技術一覧

<p>
  <img src="https://img.shields.io/badge/-Java-007396.svg?logo=java&style=for-the-badge" alt="Java">
  <img src="https://img.shields.io/badge/-Spring%20Boot-6DB33F.svg?logo=springboot&style=for-the-badge" alt="Spring Boot">
  <img src="https://img.shields.io/badge/-WebSocket-20232A.svg?style=for-the-badge&logo=websocket&logoColor=white" alt="WebSocket">
  <img src="https://img.shields.io/badge/-OpenAI-412991.svg?logo=openai&style=for-the-badge" alt="OpenAI">
  <img src="https://img.shields.io/badge/-Maven-C71A36.svg?logo=apachemaven&style=for-the-badge" alt="Maven">
  <img src="https://img.shields.io/badge/-Docker-1488C6.svg?logo=docker&style=for-the-badge" alt="Docker">
  <img src="https://img.shields.io/badge/-Render-46E3B7.svg?logo=render&style=for-the-badge" alt="Render">
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
|:---|:---|
| 名称 | Rion AI Chat |
| 概要 | Spring Boot × OpenAI × WebSocket を利用したリアルタイム AI チャットアプリ |
| 目的 | AI API 利用、リアルタイム通信、Docker デプロイまでの一連の実装学習 |
| デプロイ先 | Render |
| 対応環境 | Java 21 / Maven 3.9+ |
| 公開URL | https://rion-ai-chat.onrender.com |

---

## 環境

| カテゴリ | 使用技術・バージョン |
|:---|:---|
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
|:---|:---|
| `src/main/java/com/example/chat/controller/` | WebSocket・REST コントローラ |
| `src/main/java/com/example/chat/service/` | OpenAI API 呼び出し・業務ロジック |
| `src/main/java/com/example/chat/config/` | WebSocket・CORS 設定 |
| `src/main/java/com/example/chat/model/` | DTO / メッセージモデル |
| `src/main/java/com/example/chat/RionAiChatApplication.java` | メインクラス |
| `src/main/resources/application.yml` | 設定ファイル（環境変数参照） |
| `src/main/resources/templates/` | Thymeleaf テンプレート（UI使用時） |
| `src/test/` | JUnit テスト |
| `.env.example` | 環境変数サンプル |
| `Dockerfile` / `pom.xml` | コンテナ構成・依存管理 |

<details>
<summary>ディレクトリツリー</summary>

```text
rion-ai-chat/
├─ src/
│  ├─ main/
│  │  ├─ java/com/example/chat/
│  │  │  ├─ controller/
│  │  │  ├─ service/
│  │  │  ├─ config/
│  │  │  ├─ model/
│  │  │  └─ RionAiChatApplication.java
│  │  └─ resources/
│  │     ├─ application.yml
│  │     └─ templates/
│  └─ test/
├─ .env.example
├─ Dockerfile
├─ pom.xml
└─ README.md
```
</details>

## 作者情報

名前	仲村莉穏

GitHub	https://github.com/Umintyu-Okinawa

学習分野	Java / Spring Boot / WebSocket / AI Integration / Docker

---
## 公開URL

https://rion-ai-chat.onrender.com

---



