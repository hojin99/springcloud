FROM postgis/postgis:13-master

# 기본 패키지 설치 
RUN apt-get update
RUN apt-get -y install vim
RUN apt-get -y install net-tools
RUN apt-get -y install iputils-ping
RUN apt-get -y install netcat

# 환경 변수 설정
ENV POSTGRES_DB=sc_db
ENV POSTGRES_USER=sc
ENV POSTGRES_PASSWORD=djsjffkdlwj!1

# EXTENSION 추가 작업
RUN mkdir -p /docker-entrypoint-initdb.d
COPY ./initdb-extension.sh /docker-entrypoint-initdb.d/extension.sh

# 초기화 sh, sql 실행
COPY ./init_dump*.sql /docker-entrypoint-initdb.d/
