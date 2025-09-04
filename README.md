# Spring BootとOpenAIを使ったチャットアプリケーション

JavaのフレームワークであるSpring Bootを利用して構築された、AIチャットアプリケーションです。
ユーザーからのメッセージに対し、OpenAIのGPTモデルが自動で返信を生成します。

## ファイル構成と解説

このプロジェクトの主要なファイルとディレクトリの役割は以下の通りです。

### 1. プロジェクトルート

- `pom.xml`
  - Mavenプロジェクトの重要な設定ファイルです。プロジェクトの依存関係や、ビルド設定が定義されています。
  - **主な依存関係**: Spring Boot Web, WebSocket, JPA (データベース), Security, OpenAI, MySQL Driver.
  - Javaのバージョンは `17` に設定されています。

- `Dockerfile`
  - このアプリケーションをDockerコンテナとしてビルドするための手順書です。これにより、どのような環境でも簡単にデプロイできます。

- `mvnw`, `mvnw.cmd`
  - MavenWrapper。ローカル環境にMavenをインストールしていなくても、プロジェクトのビルドや実行を可能にします。

- `src/`
  - すべてのソースコードが格納されているメインディレクトリです。

### 2. `src/main/` - アプリケーション本体

- `java/com/rion/chat/`
  - アプリケーションの動作を定義するJavaソースコードが配置されています。
    - `ChatApplication.java`: Spring Bootアプリケーションを起動するエントリーポイント。
    - `WebSocketConfig.java`: リアルタイム通信を実現するWebSocketの設定。
    - `ChatController.java`: HTTPリクエストとWebSocketメッセージを処理するコントローラー。
    - `AiReplyService.java`, `GptReplyService.java`: OpenAIのAPIと通信し、AIからの返信を取得するサービス。
    - `ChatMessageEntity.java`: データベースに保存されるチャットメッセージの構造を定義するエンティティ。
    - `ChatMessageRepository.java`: データベース操作を行うためのJPAリポジトリ。

- `resources/`
  - 設定ファイルや静的リソースが配置されています。
    - `application.properties`: アプリケーションのグローバル設定ファイル。
      - `openai.api.key`: OpenAIのAPIキー（環境変数から読み込み）。
      - `openai.model`: 使用するGPTモデル（例: `gpt-4o-mini`）。
      - `server.port`: アプリケーションが動作するポート（`8081`）。
    - `application-dev.properties`: 開発環境用の設定。
    - `application-prod.properties`: 本番環境用の設定。

### 3. `src/test/` - テストコード

- `java/com/rion/chat/`
  - アプリケーションの品質を保証するためのテストコードが配置されています。
    - `ChatApplicationTests.java`: アプリケーションが正常に起動するかどうかを確認する基本的なテスト。

## 起動方法

1. **前提条件**:
   - JDK 17
   - OpenAI APIキー


2. **ビルドと実行**:
   プロジェクトのルートディレクトリで以下のコマンドを実行します。
   ```bash
   ./mvnw spring-boot:run
   ```

3. **アクセス**:
   WebブラウザまたはAPIクライアントで `http://localhost:8081` にアクセスします。
   （開発用の認証情報は `application.properties` に記載されています: `admin`/`admin123`）
