package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;


public class Main {

    public static void main(String[] args) {
        String consumerKey = "TU_CONSUMER_KEY";
        String consumerSecret = "TU_CONSUMER_SECRET";
        String accessToken = "TU_ACCESS_TOKEN";
        String accessTokenSecret = "TU_ACCESS_TOKEN_SECRET";

        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

        try {
            double bitcoinPrice = getBitcoinPrice();
            String tweet = "¡Buenos días! El valor actual de Bitcoin es $no " + bitcoinPrice;

            // Publica el tweet
            Status status = twitter.updateStatus(tweet);
            System.out.println("Tweet enviado: " + status.getText());
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getBitcoinPrice() throws Exception {
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String jsonResponse = response.toString();
        double bitcoinPrice = parseBitcoinPrice(jsonResponse);
        // Imprimir el precio en la consola
        System.out.println("El precio actual de Bitcoin es: $" + bitcoinPrice);

        return bitcoinPrice;
    }

    private static double parseBitcoinPrice(String jsonResponse) {
        int start = jsonResponse.indexOf(":") + 1;
        int end = jsonResponse.indexOf("}");
        String priceString = jsonResponse.substring(start, end);
        return Double.parseDouble(priceString);
    }
}
