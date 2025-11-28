# Linux Integration

## Requirements

- Linux base filepath and project storage directory

## Files Changed

1. DataConfigurator
2. DataReceiver

## Changes

1. [x] Update win11, macOS filepaths
2. add linux filepaths
    1. [x] constructor()
3. add default linux serial comm port
    1. [x] generateSettingsFile()
   > User must change the port for this platform

## Updates

1. App Storage (Screenshots, CSVs, etc.) will now use "user.home" instead of "user.name".
2. Added linux basepath: userHome + "/.local/share/miramarwaterjets/FloatDataVisualizer/" + RELEASE_PROJECT_VERSION

3. DataReceiver:
    1. Updated to throw exception JOptionPane when serial port does not exist.
    2. Exitter when exitting checks if datakeeper is not interupted and datareceiver is not null. 