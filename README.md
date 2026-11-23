# Wordify

A daily word game for Android, in Portuguese. Guess the 5-letter word in six tries, keep a streak in Avalanche mode, or challenge a friend with a shareable code.

<a href="https://play.google.com/store/apps/details?id=com.cezila.palavreando"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="80"/></a>

---

## Game modes

- **Daily Word:** one word per day, the same for every player (Brazil time).
- **Avalanche:** solve words back to back and build the longest streak.
- **Challenge Friends:** pick a word, get a code, and send it to a friend.
- **Statistics:** current and best streaks for Daily and Avalanche.

---

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** Clean Architecture, MVVM, multi-module
- **Dependency injection:** Hilt
- **Async:** Coroutines + Flow
- **Network:** Retrofit + OkHttp, talking to a serverless AWS backend (API Gateway + Lambda + DynamoDB)
- **Local storage:** DataStore
- **Min SDK:** 31 · **Target SDK:** 36

---

## Architecture

The app is split into 11 Gradle modules. Feature modules depend only on `domain` and the shared `core` modules, never on each other.

```
palavrao-android/
├── app/                  # Entry point, navigation host, Hilt setup
├── domain/               # Pure Kotlin: models, use cases, repository interfaces
├── data/                 # Retrofit API, DataStore, repository implementations
├── core-designsystem/    # Theme, keyboard, letter tiles, shared components
├── core-navigation/      # Routes shared between features
├── feature-home/         # Mode selection
├── feature-daily/        # Daily Word
├── feature-avalanche/    # Avalanche mode
├── feature-challenge/    # Create and play friend challenges
├── feature-stats/        # Statistics
├── feature-settings/     # Settings and help
└── buildSrc/             # Shared Gradle configuration
```

Game rules live in use cases in `domain` (`SubmitGuessUseCase`, `StartDailyRoundUseCase`, `CreateFriendChallengeUseCase`, ...) and are unit tested with fake repositories, including the `GuessEvaluator` that scores each letter.

---

## CI/CD

Every push to `main` runs a GitHub Actions workflow that builds a signed release AAB and APK. The keystore, `google-services.json`, API URL and API key are injected from GitHub Secrets at build time; none of them live in the repository.

---

## Building locally

The app needs a backend URL and API key, read from environment variables at build time:

```bash
export PALAVRAO_BASE_URL="https://your-api.example.com/"
export API_KEY="your-api-key"
./gradlew assembleDebug
```

You also need your own `app/google-services.json` from a Firebase project.

The backend lives in [palavrao-backend](https://github.com/VitorCezila/palavrao-backend).

---

## License

MIT
