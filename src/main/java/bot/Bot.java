package bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {
    public static void main(String[] args) throws Exception {
        String token = System.getenv("DISCORD_BOT_TOKEN");
        if (token == null) {
            System.out.println("Variable d'environnement DISCORD_BOT_TOKEN non définie !");
            return;
        }

        JDABuilder.createDefault(token)
                  .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                  .addEventListeners(new bot.listener.MessageListener())
                  .build();

        System.out.println("Bot en ligne !");
    }
}