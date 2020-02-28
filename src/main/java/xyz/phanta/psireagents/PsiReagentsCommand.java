package xyz.phanta.psireagents;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import xyz.phanta.psireagents.constant.LangConst;

public class PsiReagentsCommand extends CommandBase { // TODO subcommands for modifying and saving costs

    @Override
    public String getName() {
        return "psireagents";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/psireagents <reload>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && args[0].equals("reload")) {
            PsiReagents.PROXY.getPieceCosts().reloadFromDisk(true);
            sender.sendMessage(new TextComponentTranslation(LangConst.CMD_FEEDBACK_RELOADED));
        } else {
            throw new CommandException(getUsage(sender));
        }
    }

}
