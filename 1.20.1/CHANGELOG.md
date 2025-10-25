# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v20.1.3-1.20.1] - 2025-10-25

### Fixed

- Fix the missing darkened background on some hopper screens
- Fix a start-up crash when installed together with the Lithium mod on Fabric by disabling Lithium's hopper
  optimizations (thanks [Mavendow](https://github.com/Mavendow)!)
- There is unfortunately no way around this as there is an incompatibility in one of their Mixins, which cannot be
  disabled individually

## [v20.1.2-1.20.1] - 2025-07-20

### Changed

- Ignore component-based item data for grated hopper filtering to help with sorting damaged and enchanted equipment

## [v20.1.1-1.20.1] - 2025-06-19

### Changed

- Update the bundled Extensible Enums library to v20.1.1

## [v20.1.0-1.20.1] - 2025-04-29

### Changed

- Port to Minecraft 1.20.1
