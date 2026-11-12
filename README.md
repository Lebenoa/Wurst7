# Wurst Client v7

![Wurst Client logo](https://img.wimods.net/github.com/Wurst-Imperium/Wurst7?to=https://wurst.wiki/_media/logo/wurst_758x192.webp)

- **Downloads:** [https://www.wurstclient.net/download/](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https%3A%2F%2Fwww.wurstclient.net%2Fdownload%2F%3Futm_source%3DGitHub%26utm_medium%3DWurst7%2Brepo)

- **Installation guide:** [https://www.wurstclient.net/tutorials/how-to-install/](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https%3A%2F%2Fwww.wurstclient.net%2Ftutorials%2Fhow-to-install%2F%3Futm_source%3DGitHub%26utm_medium%3DWurst7%2Brepo)

- **Feature list:** [https://www.wurstclient.net/](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https%3A%2F%2Fwww.wurstclient.net%2F%3Futm_source%3DGitHub%26utm_medium%3DWurst7%2Brepo)

- **Wiki:** [https://wurst.wiki/](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https%3A%2F%2Fwurst.wiki%2F%3Futm_source%3DGitHub%26utm_medium%3DWurst7%2Brepo)

- **Forum:** [https://wurstforum.net/](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https%3A%2F%2Fwurstforum.net%2F%3Futm_source%3DGitHub%26utm_medium%3DWurst7%2Brepo)	

- **Twitter/X:** [https://x.com/Wurst_Imperium](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https://x.com/Wurst_Imperium)

- **YouTube:** [https://www.youtube.com/@Alexander01998](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https://www.youtube.com/@Alexander01998)

- **Donations/Perks:** [https://ko-fi.com/wurst](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https://ko-fi.com/wurst)

## Differences from upstream

This fork adds a **"Prefer Fortune"** setting to AutoFarm. When enabled, AutoFarm equips the tool with the highest Fortune enchantment from the hotbar before harvesting by mining — but only if it beats the Fortune level of the currently held item. Replanting is unaffected.

Why not in AutoTool: upstream decided in
[#822](https://github.com/Wurst-Imperium/Wurst7/issues/822) that tool selection belongs in AutoTool, not AutoFarm:

> Update: I've changed my mind about this and am no longer in favor of adding Fortune support directly to AutoFarm. IMO, tool selection should be handled by AutoTool instead, which can then either be enabled alongside AutoFarm or used separately.

As of 2026-11-05 no branch of upstream (master, 26.2, 26.3, 26.4) implements Fortune preference in AutoTool, so this fork carries the setting in AutoFarm directly. Background: [#822](https://github.com/Wurst-Imperium/Wurst7/issues/822), [#371](https://github.com/Wurst-Imperium/Wurst7/issues/371), [#846](https://github.com/Wurst-Imperium/Wurst7/pull/846).

This fork also adds **PlantAura**, a new hack that automatically plants held seeds on empty farmland — AutoFarm's planting mechanics (Range, Check line of sight, Face target, Interact swing settings) extracted into a standalone hack. Line of sight is off by default; AutoFarm enforces it. PlantAura never switches items: it only plants what you are already holding, where AutoFarm falls back to `InventoryUtils.selectItem()` / creative give. Upstream has no equivalent hack.

## Installation

Wurst 7 can be installed just like any other Fabric mod. Here are the basic installation steps:

1. Run the Fabric installer.
2. Add the Wurst Client and Fabric API to your mods folder.

Please refer to the [full Wurst 7 installation guide](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https%3A%2F%2Fwww.wurstclient.net%2Ftutorials%2Fhow-to-install%2F%3Futm_source%3DGitHub%26utm_medium%3DWurst7%2Brepo) if you need more detailed instructions or run into any problems.

Also, this should be obvious, but you do need to have a licensed copy of Minecraft Java Edition in order to use Wurst. Wurst is a cheat client, not a pirate client.

## Development Setup

> [!IMPORTANT]
> Make sure you have [Java Development Kit 25](https://adoptium.net/temurin/releases?version=25&os=any&arch=any) installed. It won't work with other versions.

### Development using Eclipse

1. Clone the repository:

   ```pwsh
   git clone https://github.com/Wurst-Imperium/Wurst7.git
   cd Wurst7
   ```

2. Generate the sources:

   ```pwsh
   ./gradlew genSources genEclipseRuns
   ```

3. In Eclipse, go to `Import...` > `Gradle` > `Existing Gradle Project` and select this project.

4. **Optional:** Right-click on the project and select `Properties` > `Java Code Style`. Then under `Clean Up`, `Code Templates`, `Formatter`, import the respective files in the `codestyle` folder.

### Development using VSCode / Cursor

> [!TIP]
> You'll probably want to install the [Extension Pack for Java](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https%3A%2F%2Fmarketplace.visualstudio.com%2Fitems%3FitemName%3Dvscjava.vscode-java-pack) to make development easier.

1. Clone the repository:

   ```pwsh
   git clone https://github.com/Wurst-Imperium/Wurst7.git
   cd Wurst7
   ```

2. Generate the sources:

   ```pwsh
   ./gradlew genSources vscode
   ```

3. Open the `Wurst7` folder in VSCode / Cursor.

4. **Optional:** In the VSCode settings, set `java.format.settings.url` to `https://raw.githubusercontent.com/Wurst-Imperium/Wurst7/master/codestyle/formatter.xml` and `java.format.settings.profile` to `Wurst-Imperium`.

### Development using IntelliJ IDEA

I don't use or recommend IntelliJ, but the commands to run would be:

```pwsh
git clone https://github.com/Wurst-Imperium/Wurst7.git
cd Wurst7
./gradlew genSources ideaSyncTask --no-configuration-cache
```

**Note:** IntelliJ IDEA is [not yet compatible](https://github.com/FabricMC/fabric-loom/issues/1349) with Gradle's configuration cache. You will run into issues.

Possible workarounds:
- Turn off args files ([this setting](https://i.imgur.com/zHqIOYg.png)). Won't work for some users because of a command length limit.
- Add `--no-configuration-cache` to all of your Gradle commands.

## Contributing

Please always [contact me](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https%3A%2F%2Fwww.wurstclient.net%2Fcontact%2F%3Futm_source%3DGitHub%26utm_medium%3DWurst7%2Brepo) before opening a Pull Request. Any method works. That way we can discuss your ideas early and avoid wasting your time working on unwanted features or having to make lots of changes later.

We also have [contributing guidelines](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https://github.com/Wurst-Imperium/Wurst7/blob/master/CONTRIBUTING.md) to help you get started.

## Translations

To enable translations in-game, go to Wurst Options > Translations > ON.

The preferred way to submit translations is through a Pull Request here on GitHub. The translation files are located in [this folder](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https://github.com/Wurst-Imperium/Wurst7/tree/master/src/main/resources/assets/wurst/translations).

Names of features (hacks/commands/etc.) should always be kept in English. This ensures that everyone can use the same commands, keybinds, etc. regardless of their language setting. It also makes it easier to communicate with someone who uses Wurst in a different language.

## License

This code is licensed under the GNU General Public License v3. **You can only use this code in open-source clients that you release under the same license! Using it in closed-source/proprietary clients is not allowed!**
