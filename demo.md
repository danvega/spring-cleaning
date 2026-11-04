# Demo notes

Two live demos for the talk "Spring Cleaning: What Boot 4 Lets You Delete". Upgrade first, delete second.

Before you start:

- Run `sdk env` to get Java 21.
- Practice on a throwaway branch so the start branch stays clean: `git switch -c take-1 demo-01-start`. When you're done: `git switch demo-01-start && git branch -D take-1`.
- Run `./mvnw clean verify` after every step. Always use `clean` after a version change.
- The answer key for each demo is a diff: `git diff demo-01-start demo-02-start` and `git diff demo-02-start main`.

## Demo 1: "It worked yesterday"

Start on `demo-01-start`. Spring Boot 3.5.16 to 4.1.2, one break at a time. About 6 minutes.

### Walk through the code

1. `pom.xml`: Boot 3.5.16. Point out the four extras:
   - `spring-retry` and `spring-boot-starter-aop`
   - `spring-cloud-starter-openfeign` and the Spring Cloud 2025.0.3 BOM
   - `flyway-core`
   - `spring-boot-starter-webflux`, scoped to tests, only for WebTestClient
2. `Application`: `@EnableRetry` and `@EnableFeignClients`.
3. `speaker/`:
   - `SpeakerClient` is the `@FeignClient`.
   - `SpeakerService` retries with `@Retryable(maxAttempts = 4)`.
   - `FakeSpeakerApi` stands in for the real API. It fails 2 calls out of 3.
4. `versioning/VersionFilter` and `talk/TalkV2Controller`: API versioning by hand.
5. `config/JacksonConfig` and `talk/ScheduleExporter`: Jackson 2. The exporter writes dates as arrays, because the mobile app reads them that way.
6. Run it with `./mvnw spring-boot:run`, then use `requests.http`:
   - The speaker request. The log shows three calls.
   - `X-API-Version: 2.0` returns summaries.
   - `X-API-Version: 3.0` returns a 400.
   - http://localhost:8080/h2-console opens the H2 console.

### Break it and fix it

**1. Bump the parent to `4.1.2`.**
Breaks: Maven can't read the POM. `spring-boot-starter-aop` has no version.
Fix: rename it to `spring-boot-starter-aspectj`.

**2. Rebuild.**
Breaks: compile errors in `JacksonConfig` and `ScheduleExporter`. The `com.fasterxml.jackson.databind` packages are gone.
Fix `JacksonConfig`:

```java
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import tools.jackson.databind.SerializationFeature;

@Bean
JsonMapperBuilderCustomizer prettyPrint() {
    return builder -> builder.enable(SerializationFeature.INDENT_OUTPUT);
}
```

Fix `ScheduleExporter`. Jackson 3 exceptions are unchecked, so the try/catch goes too:

```java
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

private final JsonMapper mapper = JsonMapper.builder()
        .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();

public String export(List<Talk> talks) {
    return mapper.writeValueAsString(talks);
}
```

Leave out `WRITE_DATES_AS_TIMESTAMPS` and `exportWritesDatesAsArrays` fails. Jackson 3 writes ISO-8601 strings by default.

**3. Rebuild.**
Breaks: test compile errors. `@WebMvcTest`, `@AutoConfigureMockMvc` and `@MockBean` moved or went away.
Fix:

- `spring-boot-starter-test` becomes `spring-boot-starter-webmvc-test`.
- `@WebMvcTest` and `@AutoConfigureMockMvc` now come from `org.springframework.boot.webmvc.test.autoconfigure`.
- `@MockBean` becomes `@MockitoBean`, from `org.springframework.test.context.bean.override.mockito`.

**4. Rebuild.**
Breaks: every Spring context fails to start, in `SimpleDiscoveryClientAutoConfiguration`. Spring Cloud 2025.0 is the Boot 3.5 release train.
Fix: set `spring-cloud.version` to `2025.1.4`.
Line for the room: "Feign is what's holding the upgrade back."

**5. Rebuild.**
Breaks: `Table "TALK" not found`. Flyway didn't run, and nothing told you.
Fix: `flyway-core` becomes `spring-boot-starter-flyway`.

**6. Rebuild.**
Breaks: `ApiTests` can't find a `WebTestClient` bean.
Fix: `spring-boot-starter-webflux` becomes `spring-boot-webtestclient`. Add `@AutoConfigureWebTestClient` from `org.springframework.boot.webtestclient.autoconfigure` to `ApiTests`.

**7. Green.** Now run the app.
Breaks: http://localhost:8080/h2-console returns a 404. No test caught it.
Fix: add `spring-boot-h2console`.

Optional, these don't break anything:

- Rename `spring-boot-starter-web` to `spring-boot-starter-webmvc`.
- Switch each `package-info.java` to JSpecify `@NullMarked`, and `@Nullable` to `org.jspecify.annotations.Nullable`.

The end state of Demo 1 is the `demo-02-start` branch.

## Demo 2: "Demolition, live"

Start on `demo-02-start`. Boot 4.1.2 and green, with all four extras still in. Delete them one at a time. About 6 minutes.

**1. Spring Retry.**

- Remove `spring-retry` and `spring-boot-starter-aspectj`.
- `@EnableRetry` becomes `@EnableResilientMethods`.
- `@Retryable(retryFor = FeignException.class, maxAttempts = 4, backoff = @Backoff(delay = 200, multiplier = 2))` becomes `@Retryable(maxRetries = 3, includes = HttpServerErrorException.class, delay = 200, multiplier = 2)`.
- Watch the count: `maxAttempts = 4` is `maxRetries = 3`. The log still shows three calls.

**2. OpenFeign.**

- Remove `spring-cloud-starter-openfeign` and the whole Spring Cloud BOM. Add `spring-boot-starter-restclient`.
- `@FeignClient` becomes a plain interface, and `@GetMapping` becomes `@GetExchange`.
- `@EnableFeignClients` becomes `@ImportHttpServices(group = "speakers", types = SpeakerClient.class)`.
- The URL moves to `spring.http.serviceclient.speakers.base-url` in `application.yaml`.

**3. The version filter.**

- Delete `VersionFilter` and `TalkV2Controller`.
- In `TalkController`, use `@GetMapping(version = "1.0")` and `@GetMapping(version = "2.0")`.
- Add `spring.mvc.apiversion` to `application.yaml`: header `X-API-Version`, supported `1.0,2.0`, default `"1.0"`.
- Version 3.0 still gets a 400.

**4. WebTestClient.**

- `spring-boot-webtestclient` becomes `spring-boot-resttestclient`.
- `@AutoConfigureWebTestClient` becomes `@AutoConfigureRestTestClient`.
- The field type `WebTestClient` becomes `RestTestClient`. The test bodies don't change.

Finish with this. It prints nothing:

```bash
./mvnw dependency:tree | grep -E "webflux|spring-cloud|spring-retry|feign"
```

The end state of Demo 2 is `main`.
