package bot.handler;

import bot.command.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Map;
import java.util.HashMap;

public class CommandHandler {
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("!ping", new PingCommand());
        commands.put("!say", new SayCommand());
        commands.put("!shutdown", new ShutdownCommand());
        commands.put("!dice", new DiceCommand());
        commands.put("!coinflip", new CoinFlipCommand());
        commands.put("!roll", new RollCommand());
        commands.put("!userinfo", new UserInfoCommand());
        commands.put("!serverinfo", new ServerInfoCommand());
        commands.put("!avatar", new AvatarCommand());
    }

    public static void handle(String message, MessageReceivedEvent event) {
        String commandKey = message.split(" ")[0].toLowerCase();
        Command command = commands.get(commandKey);
        if (command != null) {
            command.execute(event);
        }
    }
}