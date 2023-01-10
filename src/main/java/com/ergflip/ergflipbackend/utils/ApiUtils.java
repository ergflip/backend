package com.ergflip.ergflipbackend.utils;

import com.ergflip.ergflipbackend.service.ReceiveBetService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiUtils {
    private static final String BALANCE_API = "https://api.ergoplatform.com/api/v1/addresses/";
    private static final String[] serverAddresses = {"9g9b9XdDBrvENHiR86cjVdrJxJ7KBDtWbwdQQJvsFqoTTJ5EYte","9fSuzs5tQUSaitGQhurYFfHJUBqQ4pz94BTvwQKL37vT81UcEij","9hkrzEE9TgWBjJn8oNG5FXP5bh5SjaDE6892QkwCfwP3hbXFmnw","9gc3Rb9noL3wd5tu9Fk1663WFF381C8SCkR8LCkJJ8HHXySn4jm"};

    @Autowired
    private static ReceiveBetService receiveBetService;
    public static boolean serverWalletHasEnoughFunds() {
        try {
            int freeServerWallet = receiveBetService.getLastAddress();
            URL url = new URL(BALANCE_API + serverAddresses[freeServerWallet] + "/balance/total");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if (responsecode == 200) {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                //Close the scanner
                scanner.close();
                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);
                JSONObject confirmed = (JSONObject) data_obj.get("confirmed");
                Long amountOfNanoErgs = (Long) confirmed.get("nanoErgs");
                double amountOfErgs = amountOfNanoErgs / 1000000000.0;
                if (amountOfErgs < 50) return false;
                return true;
            } else {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
