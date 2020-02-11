# Docker Gitlab Back-up and Restore



## Back-Up

Docker를 사용하고 있다면 다음과 같이 백업할 수 있다.

version <= 12.1

```shell
docker exec -t <container name> gitlab-rake gitlab:backup:create
```

version > 12.1 

```shell
docker exec -t <container name> gitlab-backup create
```

예제 결과

```
Dumping database tables:
- Dumping table events... [DONE]
- Dumping table issues... [DONE]
- Dumping table keys... [DONE]
- Dumping table merge_requests... [DONE]
- Dumping table milestones... [DONE]
- Dumping table namespaces... [DONE]
- Dumping table notes... [DONE]
- Dumping table projects... [DONE]
- Dumping table protected_branches... [DONE]
- Dumping table schema_migrations... [DONE]
- Dumping table services... [DONE]
- Dumping table snippets... [DONE]
- Dumping table taggings... [DONE]
- Dumping table tags... [DONE]
- Dumping table users... [DONE]
- Dumping table users_projects... [DONE]
- Dumping table web_hooks... [DONE]
- Dumping table wikis... [DONE]
Dumping repositories:
- Dumping repository abcd... [DONE]
Creating backup archive: $TIMESTAMP_gitlab_backup.tar [DONE]
Deleting tmp directories...[DONE]
Deleting old backups... [SKIPPING]
```



docker voulme을 지정해두지 않았다면 기본 경로는 '/var/opt/gitlab/backups` 이다.

본인이 docker volume을 설정해 두었다면 해당 위치에 <[timestamp]_gitlab_backup.tar> 파일이 생성될 것이다.



## Restore

기본 경로는 '/var/opt/gitlab/backups` 이다.

본인이 docker volume을 지정해 놓았으면 해당 하는 위치에 파일을 올려놓으면 된다.



version <= 12.1

```shell
docker exec -it <name of container> gitlab-rake gitlab:backup:restore
```

version > 12.1

```shell
docker exec -it <name of container> gitlab-backup restore
```

