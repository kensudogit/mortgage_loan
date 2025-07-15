# 住宅ローンシステム

Java 8 + Struts2 + Spring + MyBatis + MySQL + React + TypeScriptを使用した包括的な住宅ローン管理システムです。

## 技術スタック

### バックエンド
- **Java 8**: メインのプログラミング言語
- **Struts2**: Webフレームワーク
- **Spring Framework**: DIコンテナ、トランザクション管理
- **MyBatis**: ORMフレームワーク
- **MySQL**: データベース
- **Maven**: ビルドツール

### フロントエンド
- **React 18**: UIライブラリ
- **TypeScript**: 型安全なJavaScript
- **React Router**: クライアントサイドルーティング
- **Axios**: HTTP通信ライブラリ
- **CSS3**: モダンなスタイリング

## 主な機能

### ローン商品管理
- 新商品条件設定
- 金利設定
- 返済方法設定
- 商品の有効/無効管理

### 見積もり機能
- リアルタイム見積もり計算
- 複数の返済プラン比較
- 金利シミュレーション
- 月次返済額計算

### 申し込み機能
- オンライン申し込み
- 自動審査システム
- 申し込み状況追跡
- 審査結果通知

### 管理機能
- 申し込み一覧表示
- 審査状況管理
- 顧客情報管理
- 統計レポート

## プロジェクト構造

```
mortgage_loan/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mortgage/
│   │   │       ├── action/
│   │   │       ├── model/
│   │   │       ├── service/
│   │   │       └── dao/
│   │   ├── resources/
│   │   │   ├── mybatis/
│   │   │   └── applicationContext.xml
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml
│   │       │   └── struts.xml
│   │       └── frontend/
│   │           └── build/
│   └── test/
│       └── java/
│           └── com/mortgage/
│               └── service/
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── types/
│   │   └── App.tsx
│   ├── package.json
│   └── tsconfig.json
├── pom.xml
└── README.md
```

## セットアップ手順

### 前提条件
- Java 8以上
- Maven 3.6以上
- MySQL 5.7以上
- Node.js 16以上
- npm または yarn

### 1. データベース設定

```sql
-- MySQLにログイン
mysql -u root -p

-- データベース作成
CREATE DATABASE mortgage_loan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ユーザー作成
CREATE USER 'mortgage_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON mortgage_loan.* TO 'mortgage_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. バックエンド設定

```bash
# プロジェクトディレクトリに移動
cd devlop/mortgage_loan

# データベース初期化
mysql -u mortgage_user -p mortgage_loan < src/main/resources/init.sql

# Mavenでビルド
mvn clean install

# アプリケーション起動
mvn tomcat7:run
```

### 3. フロントエンド設定

```bash
# フロントエンドディレクトリに移動
cd frontend

# 依存関係インストール
npm install

# 開発サーバー起動
npm start

# 本番ビルド
npm run build
```

## 使用方法

### 1. 見積もり計算
1. トップページから「見積もりを始める」をクリック
2. ローン商品を選択
3. 融資希望額と期間を入力
4. 「見積もり計算」をクリック
5. 結果を確認

### 2. ローン申し込み
1. 見積もり結果から「この条件で申し込む」をクリック
2. お客様情報を入力
3. 物件情報を入力
4. 収入・職業情報を入力
5. 口座情報を入力
6. 「申し込みを確定する」をクリック

### 3. 申し込み状況確認
1. 申し込み完了画面で審査状況を確認
2. メールで審査結果を受信
3. 承認の場合は契約手続きへ

## 開発ガイドライン

### コーディング規約
- Java: Google Java Style Guide準拠
- TypeScript: ESLint + Prettier使用
- コミットメッセージ: Conventional Commits準拠

### テスト
```bash
# バックエンドテスト
mvn test

# フロントエンドテスト
npm test
```

### デプロイ
```bash
# 本番ビルド
mvn clean package
npm run build

# WARファイルをTomcatにデプロイ
cp target/mortgage-loan.war $TOMCAT_HOME/webapps/
```

## API仕様

### ローン商品API
- `GET /api/loan/products` - 商品一覧取得
- `GET /api/loan/products/{id}` - 商品詳細取得

### 見積もりAPI
- `POST /api/loan/estimate/calculate` - 見積もり計算
- `GET /api/loan/estimate/{id}` - 見積もり詳細取得

### 申し込みAPI
- `POST /api/loan/application/submit` - 申し込み送信
- `GET /api/loan/application/{id}` - 申し込み詳細取得
- `GET /api/loan/application/customer/{id}` - 顧客別申し込み一覧

## トラブルシューティング

### よくある問題

1. **データベース接続エラー**
   - MySQLサービスが起動しているか確認
   - 接続情報が正しいか確認

2. **フロントエンドビルドエラー**
   - Node.jsバージョンを確認
   - 依存関係を再インストール

3. **API通信エラー**
   - バックエンドサーバーが起動しているか確認
   - CORS設定を確認

### ログ確認
```bash
# アプリケーションログ
tail -f logs/mortgage-loan.log

# データベースログ
tail -f /var/log/mysql/error.log
```

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。

## 貢献

プルリクエストやイシューの報告を歓迎します。貢献する前に、コーディング規約を確認してください。

## サポート

技術的な質問や問題がある場合は、以下の方法でお問い合わせください：
- イシュー: GitHub Issues
- メール: support@mortgage-loan.com 
