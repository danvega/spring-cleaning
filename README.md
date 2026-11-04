# Spring Cleaning

The demo app for the talk "Spring Cleaning: What Boot 4 Lets You Delete". It's a small conference talks API: Spring MVC, Spring Data JDBC, Flyway and H2.

## Branches

| Branch | What's on it |
|---|---|
| `demo-01-start` | Spring Boot 3.5. The app still uses Spring Retry, Spring Cloud OpenFeign, a hand-written API version filter, and WebTestClient (which pulls WebFlux onto the test classpath). Demo 1 starts here: upgrade it to Boot 4. |
| `demo-02-start` | The same app on Spring Boot 4.1, upgraded and green, with all four of those still in place. Demo 2 starts here: delete them. |
| `main` | The finished code. Boot 4.1 with Spring Retry, OpenFeign, the filter and WebTestClient gone. |

Upgrade first, delete second. Don't mix the two in one pull request.

## Run it

You need Java 21. If you use SDKMAN, run `sdk env` first.

```bash
./mvnw spring-boot:run
```

The API runs on port 8080. `requests.http` has a request for every endpoint. The H2 console is at http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:testdb`, user `sa`, no password).

```bash
./mvnw clean verify
```

Run `clean` every time you change the Spring Boot version. Stale classes can give you a result that isn't real.

## The endpoints

- `GET /api/talks` lists the talks. Send `X-API-Version: 2.0` for the summary version.
- `GET /api/talks/{id}` returns one talk.
- `GET /api/talks/search?title=...` finds a talk by title.
- `GET /api/talks/{id}/speaker` fetches the speaker's profile from the speaker API, with retries.
- `GET /api/talks/export` is the schedule feed for the mobile app. It writes dates as arrays.

The speaker API is a fake inside the app (`FakeSpeakerApi`). It fails two calls out of three, so the retries have something to do, and the demo runs offline.
