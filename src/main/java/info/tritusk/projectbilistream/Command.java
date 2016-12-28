package info.tritusk.projectbilistream;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class Command extends CommandBase {

	@Override
	public String getCommandName() {
		return "bililivejoin";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/bililivejoin <url id>";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0; //Anyone can use it
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		BiliLiveMonitor.keepRunning = false;
		while (BiliStreamMod.t.isAlive()) {
		}
		BiliStreamMod.t = new Thread(new BiliLiveMonitor(args[0]));
		BiliLiveMonitor.keepRunning = true;
		BiliStreamMod.t.start();
	}
}
