// ENTER A MESSAGE WITH THE KEYWORD "WEATHER" AND ZIP-CODE, GIVES WEATHER INFORMATION

public class ChatBot
{
    public static void main(String[] args) throws Exception
    {
        bot ChatBot = new bot();
        ChatBot.setVerbose(true);
        ChatBot.connect("irc.freenode.net");
        ChatBot.joinChannel("#testChannel"); // Name of channel is testChannel
        ChatBot.sendMessage("#testChannel",
                "Hey! Enter any message with the keyword -weather- along with a zip-code and I can tell you the weather!");
    }
}