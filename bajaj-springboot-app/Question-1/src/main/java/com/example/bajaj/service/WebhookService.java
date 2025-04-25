package com.example.bajaj.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void process() {
        String initUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook";
        Map<String, String> request = Map.of(
                "name", "John Doe",
                "regNo", "REG12347",
                "email", "john@example.com"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(initUrl, request, Map.class);
        Map body = response.getBody();

        String webhook = (String) body.get("webhook");
        String accessToken = (String) body.get("accessToken");

        List<Map<String, Object>> users = (List<Map<String, Object>>)
                ((Map) body.get("data")).get("users");

        List<List<Integer>> mutuals = findMutuals(users);

        Map<String, Object> result = Map.of(
                "regNo", "REG12347",
                "outcome", mutuals
        );

        sendWithRetry(webhook, accessToken, result);
    }

    private List<List<Integer>> findMutuals(List<Map<String, Object>> users) {
        Map<Integer, Set<Integer>> followsMap = new HashMap<>();
        for (Map<String, Object> user : users) {
            Integer id = (Integer) user.get("id");
            List<Integer> follows = (List<Integer>) user.get("follows");
            followsMap.put(id, new HashSet<>(follows));
        }

        Set<String> seen = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();

        for (Integer user1 : followsMap.keySet()) {
            for (Integer user2 : followsMap.get(user1)) {
                if (followsMap.containsKey(user2) && followsMap.get(user2).contains(user1)) {
                    List<Integer> pair = List.of(Math.min(user1, user2), Math.max(user1, user2));
                    String key = pair.get(0) + "-" + pair.get(1);
                    if (seen.add(key)) {
                        result.add(pair);
                    }
                }
            }
        }
        return result;
    }

    private void sendWithRetry(String url, String token, Map<String, Object> payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        int attempts = 0;
        while (attempts < 4) {
            try {
                restTemplate.postForEntity(url, entity, String.class);
                break;
            } catch (Exception e) {
                attempts++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
