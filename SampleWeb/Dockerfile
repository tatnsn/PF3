# 1. Java 17を使用するベースイメージ
FROM eclipse-temurin:17-jdk

# 2. 作業ディレクトリを作成
WORKDIR /app

# 3. ビルドしたJARファイルをコピー
COPY target/SampleWeb-0.0.1-SNAPSHOT.jar app.jar

# 4. コンテナ内でアプリケーションを起動
CMD ["java", "-jar", "app.jar"]

# 5. 使用するポートを指定
EXPOSE 8080
