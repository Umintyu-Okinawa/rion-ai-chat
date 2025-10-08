#  Spring BootとMyBatisを使ったバッチETLアプリケーション

Javaのフレームワークである **Spring Boot** を利用して構築された、  
**CSV ⇄ DB（MariaDB）間のデータ連携（ETL）処理** を行うバッチアプリケーションです。  
Spring BatchとMyBatisを組み合わせ、実務レベルのデータ処理とログ管理を実装しました。

---

## ファイル構成と解説

このプロジェクトの主要なファイルとディレクトリの役割は以下の通りです。

### 1. プロジェクトルート

- `pom.xml`  
  - Mavenプロジェクトの設定ファイル。依存関係やビルド設定を管理しています。  
  - **主な依存関係**: Spring Boot Batch, MyBatis, MariaDB Driver, JUnit5, Lombok  
  - Javaのバージョンは `21` に設定。

- `Dockerfile`  
  - 本アプリケーションをDockerコンテナ化して実行するための設定。  
    MariaDBとの連携を容易にし、どの環境でも再現性の高い実行を実現。

- `docker-compose.yml`  
  - MariaDBとアプリケーションをまとめて起動する設定。  
    `docker compose up -d` で簡単に検証環境を構築できます。

- `mvnw`, `mvnw.cmd`  
  - Maven Wrapper。ローカルにMavenがなくてもビルド/実行可能。

- `src/`  
  - すべてのソースコード、設定、リソースファイルを格納するメインディレクトリ。

---

### 2. `src/main/` - アプリケーション本体

- `java/com/example/batch/`  
  - アプリケーションロジックを構成する主要パッケージです。

    | ファイル | 役割 |
    |:--|:--|
    | `JavaBatchEtlApplication.java` | Spring Bootの起動クラス（エントリーポイント） |
    | `config/BatchConfig.java` | Job/Step/ItemReader/Writerの定義 |
    | `domain/Customer.java` | CSV/DBで扱うデータ構造（エンティティ） |
    | `mapper/CustomerMapper.java` | MyBatisのMapperインターフェース |
    | `repository/BatchJobLogRepository.java` | ジョブログの永続化処理 |
    | `listener/JobCompletionListener.java` | Job/Stepの実行結果をログに集計 |
    | `service/BatchSchedulingService.java` | 定期実行を行うスケジューラクラス |

- `resources/`  
  設定ファイルやSQL/CSVを格納。

  | ファイル | 役割 |
  |:--|:--|
  | `application.yml` | 環境設定（DB接続・スケジューラ設定） |
  | `application-test.yml` | テスト環境用設定 |
  | `mapper/CustomerMapper.xml` | MyBatisのSQL定義ファイル |
  | `input/customers.csv` | サンプル入力CSVデータ |

---

### 3. `src/test/` - テストコード

- `java/com/example/batch/`
  - テスト用クラスを配置。JUnitを使用して動作確認を行います。

  | ファイル | 内容 |
  |:--|:--|
  | `CsvToDbJobTest.java` | CSV→DBジョブのStep件数アサート |
  | `DbToCsvJobTest.java` | DB→CSVジョブの出力確認テスト |

---

## ⚙️ 主な機能

| 機能 | 内容 |
|:--|:--|
| CSV→DB取り込み | FlatFileItemReader + MyBatisWriterで一括登録 |
| DB→CSV出力 | 条件抽出結果をCSVファイルとして出力 |
| バリデーション | 必須列・型チェック（不正データはskip） |
| Job実行ログ | read/write/skip件数をDBに記録 |
| スケジュール実行 | cron設定により定期実行（ON/OFF切替可能） |
| Docker再現性 | MariaDB + アプリをDocker Composeで起動 |
| JUnitテスト | Step件数・出力ファイル確認の自動テスト |

---

## 起動方法

### 1. ローカル環境での実行

```bash
# 1) DB起動（XAMPP or Dockerなど）
# 2) ビルド
./mvnw clean package -DskipTests

# 3) CSV→DBジョブの実行
java -jar target/java-batch-etl-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=test \
  --spring.batch.job.name=csvToDbJob

# 4) DB→CSVジョブの実行
java -jar target/java-batch-etl-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=test \
  --spring.batch.job.name=dbToCsvJob
