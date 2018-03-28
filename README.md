# Network v4

A fully fledged Minecraft server network system

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
- MongoDB database integration with Morphia

Upcoming:
- Redis JSON-based packet messaging system
- Flexible command system
- Discord bot for logging and server interaction
- NodeJS based REST API for querying server information

## Other Notes

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
└───template (server template)
```

To avoid unnecessarily large jar sizes, shading is avoided wherever possible in favour of placing libraries inside
the "libs" folder in the network directory, which are injected into the classpath via the "classpath" command-line flag
when servers are started.
