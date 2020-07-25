package club.sk1er.hytilities.command;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.handlers.silent.SilentRemoval;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class SilentRemoveCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "silentremove";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " <username|clear>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            Hytilities.INSTANCE.sendMessage("&cInvalid usage: " + getCommandUsage(sender));
        } else if (args[0].equalsIgnoreCase("clear")) {
            SilentRemoval.getSilentUsers().clear();
        } else {
            if (SilentRemoval.getSilentUsers().contains(args[0])) {
                SilentRemoval.getSilentUsers().remove(args[0]);
                Hytilities.INSTANCE.sendMessage("&aRemoved &e" + args[0] + " &afrom the removal queue.");
                return;
            }

            SilentRemoval.getSilentUsers().add(args[0]);
            Hytilities.INSTANCE.sendMessage("&aAdded &e" + args[0] + " &ato the removal queue.");
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
