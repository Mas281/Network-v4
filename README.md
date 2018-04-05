# Network v4

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/eb182e6e4ed044f0923a4c03f65121b3)](https://app.codacy.com/app/Mas281/Network-v4?utm_source=github.com&utm_medium=referral&utm_content=Mas281/Network-v4&utm_campaign=badger)

A fully fledged Minecraft server network system

## Libraries

- [Project Lombok](https://projectlombok.org) - Reduce boilerplate code
- [Spigot API](https://www.spigotmc.org) - Minecraft server library
- [BungeeCord API](https://www.spigotmc.org/wiki/bungeecord) - Minecraft server proxy
- [MongoDB Java Drivers](https://mongodb.github.io/mongo-java-driver) - MongoDB integration
- [Morphia](https://mongodb.github.io/morphia) - Object mapping and serialization for MongoDB
- [Jedis](https://github.com/xetorthio/jedis) - Java [Redis](https://redis.io/) library
- [Sentry Java Client](https://docs.sentry.io/clients/java) - [Sentry.io](https://sentry.io) java implementation
- [Google Guice](https://github.com/google/guice) - Dependency injection framework
- [Google Guava](https://github.com/google/guava) - Miscellaneous java libraries
- [Google Gson](https://github.com/google/gson) - JSON library
- [Junit](https://junit.org) - Unit testing framework
- [Mockito](http://site.mockito.org) - Object mocking for unit testing

## What is this? Why v4?

As well as developing smaller, library-like projects, I like to challenge myself once in a while and attempt to create
much larger projects to a high standard as a demonstration of my knowledge as I progress. So far, my favourite way to
try and achieve this has been through creating functioning Minecraft network systems.
 
As you could probably tell by the name, this isn't the first time I've attempted a project like this. Previously when
I've attempted to create a system like this, I feel as if I've slightly rushed through the process for the sake of a
working, finished project and the immediate gratification that comes with that.

In this latest version I intend to instead spend much longer thinking about the design each feature I wish to implement
and generally take more time than I previously have, so I don't just end up with a finished product, but a high-quality
one that can be easily built on - and hopefully won't end up getting re-written as "Network v5" in a year's time.

## Features

Completed/In Progress:
- MongoDB database integration with [Morphia](https://mongodb.github.io/morphia)
- Redis server communication system
- Global error tracking using [Sentry](https://sentry.io)

Upcoming:
- Internationalization support - translations for different languages per-player
- Bungee dynamic server management
- Flexible command system
- Discord bot for logging and server interaction
- NodeJS based REST API for querying server information

## Notes

A brief description of the modules in the project:
- common - Shared systems and utilities between all programs
- server - Code used to run on the Spigot server instances
- proxy - Code used to run on the BungeeCord proxy instance
- bot - Code powering the discord bot (soon)
- api - NodeJS code powering the web API (soon)

The directory structure for the network is as follows, with an environment variable named "NETWORK_ROOT" pointing to
the root folder.

```
%NETWORK_ROOT%
├───bot (discord bot)
├───lang (network translations)
├───libs (libraries)
├───proxy (BungeeCord server)
├───servers (Spigot servers)
│   ├───production
│   └───test
├───template (server template)
└───network_config.json (network-wide options, credentials)
```

To avoid unnecessarily large jar sizes, shading is avoided wherever possible in favour of placing libraries inside
the "libs" folder in the network directory, which are injected into the classpath at runtime via the "classpath"
command-line flag when servers are started.

Network-wide configuration options (including database credentials) are stored in the "network_config.json" file
in the root folder.

Any method/constructor parameters within the project should be assumed non-null unless specifically annotated with
@Nullable.
