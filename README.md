# 住宅ローンシステム

## 概要

住宅ローンに関する業務全般（見積もりの作成、申し込み手続き、審査プロセス、融資実行から返済管理まで）を一元的に支援するシステムです。

## 技術スタック

- **言語**: Java 8
- **フレームワーク**: Struts 2.5.30
- **DIコンテナ**: Spring Framework 5.3.20
- **ORM**: MyBatis 3.5.9
- **データベース**: MySQL 8.0.28
- **ビルドツール**: Maven 3.8.1
- **Webサーバー**: Apache Tomcat 9.0

## 機能一覧

### 1. ローン見積もり機能
- 複数のローン商品から選択
- 融資額・期間・金利による見積もり計算
- 月次返済額・総返済額・総利息の表示
- 返済シミュレーション

### 2. ローン申し込み機能
- 顧客情報の入力
- 物件情報の登録
- 収入情報の入力
- 銀行口座情報の登録
- 自動審査機能

### 3. 審査管理機能
- 自動審査（年収・物件価格による判定）
- 審査状況の管理
- 承認・却下の処理

### 4. 商品管理機能
- 固定金利・変動金利・ミックスプランの対応
- 商品条件の設定
- 金利の管理

## システム構成

```
src/
├── main/
│   ├── java/
│   │   └── com/mortgage/
│   │       ├── action/          # Struts2 Action
│   │       ├── model/           # エンティティクラス
│   │       ├── service/         # ビジネスロジック
│   │       └── dao/             # データアクセス層
│   ├── resources/
│   │   ├── mybatis/            # MyBatisマッパー
│   │   ├── sql/                # SQLスクリプト
│   │   ├── applicationContext.xml
│   │   ├── struts.xml
│   │   └── database.properties
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── jsp/            # JSPファイル
│       │   └── web.xml
│       └── index.jsp
└── test/
    └── java/
        └── com/mortgage/
            └── service/         # 単体テスト
```

## セットアップ手順

### 1. 前提条件
- Java 8以上
- Maven 3.6以上
- MySQL 8.0以上
- Apache Tomcat 9.0以上

### 2. データベース設定

1. MySQLにログイン
```bash
mysql -u root -p
```

2. データベースとユーザーを作成
```sql
CREATE DATABASE mortgage_loan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'mortgage_user'@'localhost' IDENTIFIED BY 'mortgage_pass';
GRANT ALL PRIVILEGES ON mortgage_loan.* TO 'mortgage_user'@'localhost';
FLUSH PRIVILEGES;
```

3. 初期データを投入
```bash
mysql -u mortgage_user -p mortgage_loan < src/main/resources/sql/init.sql
```

### 3. アプリケーション設定

1. データベース接続設定を編集
```bash
# src/main/resources/database.properties
jdbc.url=jdbc:mysql://localhost:3306/mortgage_loan?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Tokyo
jdbc.username=mortgage_user
jdbc.password=mortgage_pass
```

### 4. ビルドとデプロイ

1. プロジェクトをビルド
```bash
mvn clean package
```

2. WARファイルをTomcatにデプロイ
```bash
cp target/mortgage-loan-system-1.0.0.war $TOMCAT_HOME/webapps/mortgage-loan.war
```

3. Tomcatを起動
```bash
$TOMCAT_HOME/bin/startup.sh
```

4. アプリケーションにアクセス
```
http://localhost:8080/mortgage-loan/
```

## 使用方法

### 1. ローン見積もり
1. トップページから「ローン見積もり」を選択
2. ローン商品を選択
3. 融資希望額と期間を入力
4. 「見積もり計算」ボタンをクリック
5. 計算結果を確認

### 2. ローン申し込み
1. 見積もり結果画面から「この条件で申し込む」を選択
2. 顧客情報を入力
3. 物件情報を入力
4. 収入情報を入力
5. 銀行口座情報を入力
6. 「申し込みを完了する」ボタンをクリック

## テスト実行

### 単体テスト
```bash
mvn test
```

### 統合テスト
```bash
mvn verify
```

## 開発ガイドライン

### 1. コーディング規約
- Javaコーディング規約に準拠
- クラス名はPascalCase
- メソッド名・変数名はcamelCase
- 定数はUPPER_SNAKE_CASE

### 2. データベース設計
- テーブル名はsnake_case
- 主キーは`id`または`{table_name}_id`
- 外部キーは`{referenced_table}_id`
- 作成日時・更新日時は必須

### 3. セキュリティ
- SQLインジェクション対策（MyBatis使用）
- XSS対策（Struts2のエスケープ機能使用）
- CSRF対策（Struts2のトークン機能使用）

## トラブルシューティング

### よくある問題

1. **データベース接続エラー**
   - MySQLサービスが起動しているか確認
   - 接続情報（URL、ユーザー名、パスワード）を確認

2. **コンパイルエラー**
   - Java 8がインストールされているか確認
   - Mavenの依存関係が正しく解決されているか確認

3. **デプロイエラー**
   - Tomcatのバージョンを確認（9.0以上推奨）
   - WARファイルが正しく配置されているか確認

## ライセンス

このプロジェクトは社内利用を目的としています。

## お問い合わせ

システムに関するお問い合わせは、開発チームまでご連絡ください。 "# mortgage_loan" 
