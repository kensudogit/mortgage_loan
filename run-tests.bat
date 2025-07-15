@echo off
echo ========================================
echo 住宅ローンシステム テスト実行スクリプト
echo ========================================

echo.
echo 1. バックエンドテストを実行中...
cd /d "%~dp0"
call mvn test

if %ERRORLEVEL% NEQ 0 (
    echo バックエンドテストが失敗しました。
    pause
    exit /b 1
)

echo.
echo 2. フロントエンドテストを実行中...
cd frontend
call npm test -- --watchAll=false

if %ERRORLEVEL% NEQ 0 (
    echo フロントエンドテストが失敗しました。
    pause
    exit /b 1
)

echo.
echo ========================================
echo すべてのテストが正常に完了しました！
echo ========================================
pause 