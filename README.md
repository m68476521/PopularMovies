# Popular Movies

Popular Movies is a native Android application that lets users discover the most popular and highest-rated movies, view trailers and reviews, and save favorites for offline viewing. It was built as the first project for Udacity's Android Developer Nanodegree program.

## Features

- Browse the most popular and highest-rated movies (via [The Movie Database](https://www.themoviedb.org/))
- Save movies locally as favorites
- View favorite movies offline
- Watch movie trailers
- Read user reviews

## Tech Stack

| Component | Details |
|---|---|
| Language | Java |
| Platform | Android |
| Data Source | [TMDB API](https://developers.themoviedb.org/3) |

## Getting Started

### Prerequisites

- Android Studio (latest stable release recommended)
- An [API key from TheMovieDB](https://www.themoviedb.org/documentation/api) — required to fetch movie data

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/m68476521/PopularMovies.git
   cd PopularMovies
   ```

2. Add your TMDB API key. Depending on how the project is configured, this is typically done by adding a line to `gradle.properties` (create the file if it doesn't exist):
   ```properties
   MOVIE_DB_API_KEY=your_api_key_here
   ```

3. Open the project in Android Studio and let Gradle sync.

4. Build and run on an emulator or physical device.

## Project Structure

```
PopularMovies/
└── PopularMovies/    # Main Android application module
```

## Contributing

Contributions, issues, and feature requests are welcome.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m "Add my feature"`)
4. Push to your branch (`git push origin feature/my-feature`)
5. Open a Pull Request

## License

This project is licensed under the [MIT License](LICENSE) — see the `LICENSE` file for details.

## Acknowledgments

- [Udacity Android Developer Nanodegree](https://www.udacity.com/) for the project brief
- [The Movie Database (TMDB)](https://www.themoviedb.org/) for movie data
