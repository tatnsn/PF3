# ベースイメージとしてopenjdkを使用
FROM openjdk:17-jdk-slim

# Mavenのインストール
RUN apt-get update && apt-get install -y maven

# 作業ディレクトリを設定
WORKDIR /app

# プロジェクトファイルをコンテナにコピー
COPY . /app

# Mavenビルド
RUN mvn clean package

# アプリケーション実行
CMD ["java", "-jar", "target/SampleWeb-0.0.1-SNAPSHOT.jar"]

# 必要なポートを公開
EXPOSE 9130
