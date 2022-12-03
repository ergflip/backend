package com.ergflip.ergflipbackend.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TransactionMapper {

    private static final String addressAPI = "https://api.ergoplatform.com/api/v1/transactions/";

    private static final String[] serverAddresses = {"9g9b9XdDBrvENHiR86cjVdrJxJ7KBDtWbwdQQJvsFqoTTJ5EYte","9fSuzs5tQUSaitGQhurYFfHJUBqQ4pz94BTvwQKL37vT81UcEij","9hkrzEE9TgWBjJn8oNG5FXP5bh5SjaDE6892QkwCfwP3hbXFmnw","9gc3Rb9noL3wd5tu9Fk1663WFF381C8SCkR8LCkJJ8HHXySn4jm"};

    public static Long getAmountToSend(String txId) {
        int triesAttempted = 0;
        while(true) {
            try {
                URL url = new URL(addressAPI + txId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int responsecode = conn.getResponseCode();
                if (responsecode == 404) {
                    triesAttempted++;
                    if (triesAttempted > 200) {
                        return 0L;
                    }
                    Thread.sleep(10 * 1000);
                    continue;
                } else if (responsecode == 200){
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
                    JSONArray outputs = (JSONArray) data_obj.get("outputs");
                    Long totalAmountBet = 0L;
                    for (int i = 0; i < outputs.size(); i++) {
                        JSONObject output = (JSONObject) outputs.get(i);
                        String address = (String) output.get("address");
                        //output receiver has to be the server or else it doesnt count
                        if (!Arrays.asList(serverAddresses).contains(address)) continue;
                        totalAmountBet += (Long) output.get("value");
                    }
                    return totalAmountBet;
                } else throw new RuntimeException("HttpResponseCode: " + responsecode);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static List<Long> getTokenAmountToSend(String txId, String tokenId) {
        int triesAttempted = 0;
        while(true) {
            try {
                URL url = new URL(addressAPI + txId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int responsecode = conn.getResponseCode();
                if (responsecode == 404) {
                    triesAttempted++;
                    if (triesAttempted > 200) {
                        return Arrays.asList(0L, 0L);
                    }
                    Thread.sleep(10 * 1000);
                    continue;
                } else if (responsecode == 200){
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
                    JSONArray outputs = (JSONArray) data_obj.get("outputs");
                    Long totalAmountBet = 0L;
                    Long decimals = null;
                    for (int i = 0; i < outputs.size(); i++) {
                        JSONObject output = (JSONObject) outputs.get(i);
                        String address = (String) output.get("address");
                        //output receiver has to be the server or else it doesnt count
                        if (!Arrays.asList(serverAddresses).contains(address)) continue;
                        JSONArray assets = (JSONArray) output.get("assets");
                        for (int j = 0; j < assets.size(); j++) {
                            JSONObject asset = (JSONObject) assets.get(j);
                            String assetId = (String) asset.get("tokenId");
                            if (assetId.equals(tokenId)) {
                                totalAmountBet += (Long) asset.get("amount");
                                decimals = (Long) asset.get("decimals");
                            }
                        }
                    }
                    return Arrays.asList(totalAmountBet, decimals);
                } else throw new RuntimeException("HttpResponseCode: " + responsecode);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
