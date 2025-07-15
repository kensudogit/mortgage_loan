@echo off
echo ========================================
echo 住宅ローンシステム - Gradleテスト実行
echo ========================================

echo.
echo 1. プロジェクトのクリーンアップ...
call gradlew clean

echo.
echo 2. 依存関係のダウンロード...
call gradlew --refresh-dependencies

echo.
echo 3. ユニットテストの実行...
call gradlew test

echo.
echo 4. 統合テストの実行...
call gradlew runIntegrationTests

echo.
echo 5. 全テストの実行...
call gradlew runAllTests

echo.
echo ========================================
echo テスト実行完了
echo ========================================

echo.
echo テストレポートの場所:
echo - ユニットテスト: build/reports/tests/test/index.html
echo - 統合テスト: build/reports/tests/integrationTest/index.html

pause 