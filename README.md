# DogPoop Plugin

[![Build and Release](https://github.com/klbibbi/mcbajs/actions/workflows/release.yml/badge.svg)](https://github.com/klbibbi/mcbajs/actions/workflows/release.yml)

A Minecraft Paper plugin that gives **tamed dogs** the ability to poop cocoa beans with sound effects.

## Features

- Tamed dogs near players randomly poop cocoa beans
- Plays a poop sound when it happens
- Configurable probability and interval
- Only tamed dogs within a certain distance from players are affected
- Wild wolves do NOT poop

## Requirements

- Minecraft 1.21.10
- Paper server (or Paper-based server like Purpur)
- Java 21 or later
- Docker (if running the server in Docker)

## Installation

### Option A: Download Pre-built Release (Recommended)

#### 1. Download and install plugin

**Download from GitHub:**
```bash
# Replace X.X.X with the version number (e.g., 1.0.0)
VERSION="1.0.0"

# Download plugin
wget https://github.com/klbibbi/mcbajs/releases/download/v${VERSION}/DogPoop-${VERSION}.jar

# Install on Docker server
docker cp DogPoop-${VERSION}.jar CONTAINER_NAME:/data/plugins/

# Or using Docker Compose
docker compose cp DogPoop-${VERSION}.jar minecraft:/data/plugins/
```

Or manually download from [Releases](https://github.com/klbibbi/mcbajs/releases)

#### 2. Configure server resource pack (Recommended)

Configure the server to automatically prompt players to download the resource pack when they join.

**Edit `server.properties` on your server:**

```bash
# Using Docker
docker exec -it CONTAINER_NAME nano /data/server.properties

# Or using Docker Compose
docker compose exec minecraft nano /data/server.properties
```

**Add/update these lines:**
```properties
resource-pack=https://github.com/klbibbi/mcbajs/releases/download/v1.0.0/DogPoopSounds-1.0.0.zip
resource-pack-prompt=Download the DogPoop sound pack for the full experience!
require-resource-pack=false
```

**Important:** Replace `1.0.0` with the current version number.

**Options:**
- `require-resource-pack=false` - Players can decline (no sounds for them)
- `require-resource-pack=true` - Players must accept or they get kicked

**After editing, restart the server.** Players will be prompted to download the resource pack when they join.

#### 2b. Alternative: Manual local installation (Optional)

If players prefer to install manually instead of using the server prompt:

**Windows:**
```
Download DogPoopSounds-1.0.0.zip and copy to: %APPDATA%\.minecraft\resourcepacks\
```

**macOS:**
```bash
VERSION="1.0.0"
wget https://github.com/klbibbi/mcbajs/releases/download/v${VERSION}/DogPoopSounds-${VERSION}.zip
cp DogPoopSounds-${VERSION}.zip ~/Library/Application\ Support/minecraft/resourcepacks/
```

**Linux:**
```bash
VERSION="1.0.0"
wget https://github.com/klbibbi/mcbajs/releases/download/v${VERSION}/DogPoopSounds-${VERSION}.zip
cp DogPoopSounds-${VERSION}.zip ~/.minecraft/resourcepacks/
```

Then in Minecraft:
- Go to **Options** → **Resource Packs**
- Activate "DogPoopSounds"
- Click **Done**

#### 3. Restart the server

**Using Docker:**
```bash
docker restart CONTAINER_NAME

# Follow the logs:
docker logs -f CONTAINER_NAME
```

**Using Docker Compose:**
```bash
docker compose restart

# Follow the logs:
docker compose logs -f
```

The plugin automatically creates a `config.yml` file.

### Option B: Build from Source (Development)

#### 1. Build the plugin

**In IntelliJ IDEA:**
- Open the project
- Open the Maven panel (right side)
- Expand "Lifecycle"
- Double-click on "package"

**Or in terminal:**
```bash
mvn clean package
```

This creates `target/DogPoop-1.0.0.jar`

#### 2. Build resource pack

```bash
cd resource_pack
zip -r ../DogPoopSounds.zip *
cd ..
```

This creates `DogPoopSounds.zip` in the project root.

#### 3. Install plugin on Docker server

```bash
# Using Docker
docker cp target/DogPoop-1.0.0.jar CONTAINER_NAME:/data/plugins/

# Or using Docker Compose
docker compose cp target/DogPoop-1.0.0.jar minecraft:/data/plugins/
```

#### 4. Install resource pack locally (for testing)

Copy the built zip to your Minecraft resourcepacks folder:

**Windows:**
```
Copy DogPoopSounds.zip to: %APPDATA%\.minecraft\resourcepacks\
```

**macOS:**
```bash
cp DogPoopSounds.zip ~/Library/Application\ Support/minecraft/resourcepacks/
```

**Linux:**
```bash
cp DogPoopSounds.zip ~/.minecraft/resourcepacks/
```

Then in Minecraft:
- Go to **Options** → **Resource Packs**
- Activate "DogPoopSounds"
- Click **Done**

#### 5. Restart the server

```bash
# Using Docker
docker restart CONTAINER_NAME

# Using Docker Compose
docker compose restart
```

## Configuration

Edit `plugins/DogPoop/config.yml`:

```yaml
# How often the scheduler runs (in minutes)
scheduler-interval-minutes: 1

# Probability that a dog poops (1/X chance)
# 30 means 1/30 chance each time the scheduler runs
poop-chance: 30

# How many cocoa beans are dropped
cocoa-beans-amount: 2

# Distance from player for dog to be able to poop (in blocks)
player-distance: 50
```

After changes, reload the server or use `/reload confirm`.

## How it works

1. Every minute (configurable) a scheduler runs
2. The scheduler checks all dogs in all worlds
3. For each dog within 50 blocks of a player:
   - 1/30 chance that the dog poops
   - If it poops: drops cocoa beans and plays sound

## Testing

1. Join the server
2. Tame a wolf: `/summon wolf ~ ~ ~` and give it a bone
3. Wait 1 minute
4. Listen for poop sound and look for cocoa beans on the ground

## Releases

To create a new release:

1. Update version in `pom.xml`
2. Commit and push changes
3. Create and push a tag:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
4. GitHub Actions will automatically build and create a release with:
   - `DogPoop-1.0.0.jar` (plugin)
   - `DogPoopSounds-1.0.0.zip` (resource pack)

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

