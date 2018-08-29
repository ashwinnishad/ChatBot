import org.jibble.pircbot.PircBot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import  java.text.DecimalFormat;

public class bot extends PircBot
{
    static final String defaultLocation = "75080"; // Provide any default zip-code.
    final Pattern regex = Pattern.compile("(\\d{5})"); // to extract zip code
    String temperature;
    public bot()
    {
        this.setName("WeatherBot"); //this is the name the bot will use to join the IRC server
    } // end of constructor

    public void onMessage(String channel, String sender, String login, String hostname, String message)
    {
        message = message.toLowerCase();
        if (message.contains("weather"))
        {
            String location = defaultLocation;
            String[] words = message.split(" ");
            if (words.length == 2)
            {
                if (words[0].equals("weather"))
                {
                    location = words[1];
                }
                else
                {
                    location = words[0];
                }
            }
            else
            {
                Matcher matcher = regex.matcher(message);
                if (matcher.find())
                {
                    location = matcher.group(1);
                }
                else
                {
                    sendMessage(channel, "Unable to determine location. Assuming Richardson."); // Change city name here based on provided default zip-code.
                }
            }
            temperature = startWebRequest(location);
            sendMessage(channel, "Hey " + sender + "! "+ temperature);
        }// end of outer if
    } // end of class onMessage

    static String startWebRequest(String zipcode)
    {
        String weatherURL = "http://api.openweathermap.org/data/2.5/weather?zip="+zipcode+"&appid=""; // Get Application ID from OpenWeather API
        StringBuilder result = new StringBuilder();
        try
        {
            URL url = new URL(weatherURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null)
            {
                result.append(line);
            }
            rd.close();
            return parseJson(result.toString());
        }
        catch(Exception e)
        {return "Error! Exception: " + e;}
    } // end of startWebRequest

    static String parseJson(String json)
    {
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        String cityName = object.get("name").getAsString();
        JsonObject main = object.getAsJsonObject("main");
        double temp = main.get("temp").getAsDouble();
        temp = (temp-273.15) * 1.8 + 32;
        double tempMin = main.get("temp_min").getAsDouble();
        tempMin = (tempMin-273.15) * 1.8 + 32;
        double tempMax = main.get("temp_max").getAsDouble();
        tempMax = (tempMax-273.15) * 1.8 + 32;
        DecimalFormat df = new DecimalFormat("####0.0");
        // Bot output
        return "The current temperature in " + cityName + " is " + df.format(temp) + "˚F with a high of " + df.format(tempMax) +
                "˚F and a low of " + df.format(tempMin) + "˚F." ;
    }// end of parseJson
} // end of class
