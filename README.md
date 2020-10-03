# Hytilities

## Users
### What is Hytilities?
Hytilities is a [Hypixel](https://hypixel.net) focused Forge 1.8.9 mod, adding tons of Quality of Life features that
you would want while on Hypixel, such as a player-advertisement blocker, an NPC hider, etc.

### How do I use Hytilities?
Hytilities is installed just like any other Forge mod.

If you do not know how to install a Forge mod, then we recommended looking up "How to install a Forge mod for Minecraft 1.8.9?".

## Developers
### How can I contribute to Hytilities?
You can contribute to Hytilities by following the instructions below.

Press the green "Code" button.

![GitHub Code button](.github/code_button.png)

Depending on how your Git is set up/you want to clone, you will click on either HTTPS, SSH, or GitHub CLI,
then press the clipboard button beside the link.

![GitHub HTTPS Clone](.github/https_clone.png) ![GitHub SSH Clone](.github/ssh_clone.png) ![GitHub CLI Clone](.github/cli_clone.png)

Open your preferred IDE, in this example we will be using [IntelliJ](https://www.jetbrains.com/idea/).
You can choose the "Community" tab if you do not already own the Ultimate version.

<!--- todo: include images here instead of a lot of text. i don't have ij 2020.2 currently, and the context menu on 2020.3 eap is incredibly different. -->
On IntelliJ's main menu, press the button that says "Check out from Version Control", click "GitHub", 
and paste the URL to the "Git Repository URL" text field. If you want to change the directory it will be cloned to, 
change the "Parent Directory" text field. Once you are done changing where you want it to be, click "Clone", 
then wait for it to finish. Once prompted with "You have checked out an IDEA project file: [file location]. Would you like to open it?", 
press the "Yes" button.

Once it is done cloning, click on the Gradle tab on the very right sidebar.

![IntelliJ Gradle tab](.github/gradle_tab.png)

Once that is open, click the dropdown beside the Tasks folder.

![Gradle Tasks directory](.github/tasks.png)

Then click the `forgegradle` directory dropdown.

![ForgeGradle directory](.github/forgegradle.png)

Then proceed to double-click the `setupDecompWorkspace` task. This may take a few minutes.

![setupDecompWorkspace task](.github/setupDecompWorkspace.png)

Once that is complete, you will want to click on this button in the top left of the Gradle tab.

![Refresh Gradle button](.github/refresh_gradle.png)

Once the project is done refreshing, you will want to run the `genIntellijRuns` task.

![genIntellijRuns task](.github/genIntellijRuns.png)

Once that task is complete, you can start Minecraft by clicking on this dropdown in the top right, usually saying Minecraft Client.

![Minecraft Client](.github/minecraft_client.png)

Click the `Edit Configurations...` button.

![Edit Configurations](.github/edit_configurations.png)

TODO: Complete this, context menu has completely changed in 2020.3 EAP, need to reinstall 2020.2.
