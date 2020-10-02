package club.sk1er.hytilities.command;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.mods.core.ModCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class HytilitiesCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "hytilities";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("hytils");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            ModCore.getInstance().getGuiHandler().open(Hytilities.INSTANCE.getConfig().gui());
        } else {
            Hytilities.INSTANCE.sendMessage("&cIncorrect arguments. Command usage is: " + getCommandUsage(sender));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
