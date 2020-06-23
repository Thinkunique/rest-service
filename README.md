# Hacker News Service API Assignment

Prerequisites:-

1) Download and install Redis from https://redis.io/download.
2) Run Redis server in background.

App Features:-

1) Ability to run on docker.
2) Ability to cache the service response for 10 minutes.
3) Abiltity to save and retrieve data from redis datastore.
4) Ability to run as microservice.
5) Built on spring boot latest version 2.3.1.
6) Integrated swagger-api.

Endpoints:-

1) GET /{storyId}/comments
   - Get Top 10 comments list for given story.

2) GET /past-stories
   - Get Top past stories list.

3) GET /top-stories
   - Get Top 10 stories list.
