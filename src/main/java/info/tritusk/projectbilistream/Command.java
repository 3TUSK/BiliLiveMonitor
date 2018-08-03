package info.tritusk.projectbilistream;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class Command extends CommandBase {

	@Override
	public String getName() {
		return "bililivejoin";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/bililivejoin <roomid>";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0; //Anyone can use it
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		BiliStreamMod.t = new Thread(new BiliLiveMonitor(args[0]));
		BiliStreamMod.t.start();
	}
}
