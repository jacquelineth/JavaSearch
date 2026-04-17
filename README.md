# Find4j 0.3.0

Find4j is a file search utility that can find files in any nested hierarchy of directories and zip-compatible archives, according to user-specified file patterns and search terms.

## Prerequisites

- Java 1.6 or later
- The Java bin folder available in your system PATH

## Installation

### Windows Installer

Double-click Find4j-x.y.z-Install.exe.

### Java Installer

From a command prompt:

```bash
java -jar Find4j-x.y.z-Install.jar
```

## CLI Usage

Run:

```bash
# Windows
search.cmd <rootPath> <filePattern> [<searchString>]

# Linux / Unix
./search.sh <rootPath> <filePattern> [<searchString>]
```

### Arguments

- rootPath: Path to a folder or supported archive.
  - If it is a folder, search runs recursively through subdirectories and contained archives.
  - Nested archives are supported (for example: JAR inside WAR inside EAR).
- filePattern: Pattern matched against every visited file name.
  - Wildcards supported: * and ?
- searchString: Optional text to search inside visited files.

Running search without arguments displays usage instructions.

## Build and Run with Maven / VS Code

From project root:

```bash
mvn clean compile
mvn clean package
```

VS Code launch configurations are available in .vscode/launch.json:

- CLI Search launches org.javatb.search.Search (prompts for root path at runtime)
- UI Launcher launches org.javatb.search.ui.UILauncher

Both keep Java logging configured with:

```text
-Djava.util.logging.config.file=config/logging.properties
```

## GUI Usage

If installed with the Windows installer, right-click any folder in Windows Explorer and choose Find4j.

You can also run:

```bash
# Windows
searchUI.cmd [<rootPath>]

# Linux / Unix
./searchUI.sh [<rootPath>]
```

If rootPath is omitted, Find4j uses the current user directory.

### GUI Features

- File pattern and Search term support auto-completion while typing
- Ctrl + Space explicitly triggers auto-completion
- Go starts the search
- Stop interrupts an active search

## Examples

### Example 1

```bash
search C:\Workspaces\SourceForgeSVN\jca-client\build *.class J2EEDemo
```

Results:

```text
C:\Workspaces\SourceForgeSVN\jca-client\build\JPPF_J2EE_Demo_Geronimo.ear\lib/jppf-j2ee-client.jar\org/jppf/jca/demo/J2EEDemo.class
C:\Workspaces\SourceForgeSVN\jca-client\build\lib\jppf-j2ee-client.jar\org/jppf/jca/demo/J2EEDemo.class
Found 2 matches
```

### Example 2

```bash
search C:\Workspaces\JPPF-b3.3\x-weblogic\build\TestWeblogic.ear *.cl* demo
```

Results:

```text
C:\Workspaces\JPPF-b3.3\x-weblogic\build\TestWeblogic.ear\weblogic_test.war\WEB-INF/lib/jppf-j2ee-client.jar\org/jppf/jca/demo/DemoTask.class
C:\Workspaces\JPPF-b3.3\x-weblogic\build\TestWeblogic.ear\weblogic_test.war\WEB-INF/lib/jppf-j2ee-client.jar\org/jppf/jca/demo/J2EEDemo.class
C:\Workspaces\JPPF-b3.3\x-weblogic\build\TestWeblogic.ear\weblogic_test.war\WEB-INF/lib/jppf-j2ee-client.jar\org/jppf/jca/demo/JPPFHelper.class
C:\Workspaces\JPPF-b3.3\x-weblogic\build\TestWeblogic.ear\weblogic_test.war\WEB-INF/lib/jppf-j2ee-client.jar\org/jppf/jca/demo/package-info.class
Found 4 matches
```

## Change History

### v0.3

- Results view now provides sortable columns for size, compressed size, last modified date, and attributes
- Search term field now supports auto-completion
- Auto-completion history size is configurable in config/config.properties
- Explicit status bar message is shown when no files are found
- Window size/position and table column sizes are persisted and restored at startup
- New Windows installer (still requires Java)

### v0.2

- Search is no longer limited to archives and now includes every visited file
- Added support for searching a specified string in visited files
- Switched to JDK logging so log4j.jar is no longer required (smaller distribution)
