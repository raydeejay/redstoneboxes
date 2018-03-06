package net.raydeejay.redstoneboxes.commands;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class TeleportCommand extends CommandBase {
    private final List<String> aliases;

    public TeleportCommand() {
        aliases = Lists.newArrayList(RedstoneBoxesMod.MODID, "TPDIM", "tpdim");
    }

    @Override
    @Nonnull
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
                                                @Nullable BlockPos pos) {
        return Collections.emptyList();
    }

    @Override
    @Nonnull
    public String getCommandName() {
        return "tpdim";
    }

    @Override
    @Nonnull
    public String getCommandUsage(ICommandSender sender) {
        return "tpdim <id>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            return;
        }

        String s = args[0];
        int dim;

        try {
            dim = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Error parsing dimension!"));
            return;
        }

        if (sender instanceof EntityPlayer) {
            CustomTeleporter.teleportToDimension((EntityPlayer) sender, dim, 0, 100, 0);
        }
    }
}
