# post-downloader

Program downloads posts from https://jsonplaceholder.typicode.com/posts and saves them on disk.

Files are stored into `./output` directory under current path.

## Usage

### Build

```bash
./gradlew fatJar
```

### Run

```bash
java -jar ./build/lib/posts-downloader-1.0-SNAPSHOT-standalone.jar
```

### Run tests

```bash
./gradlew test
```

## Potential future extensions

- configuration via CLI parameters (e.g. destination directory or service's base url)
- skipping existing files
- filtering posts to download (e.g. by authorId or keywords)
- different file storage (e.g. DB)

## Author

- Andrzej Kimel andrzej.kimel@gmail.com

