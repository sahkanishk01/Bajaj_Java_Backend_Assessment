package com.example.mutualfollowers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class StartupRunner implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        String requestUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook";
        Map<String, String> request = new HashMap<>();
        request.put("name", "John Doe");
        request.put("regNo ", "REG12347");
        request.put("email ", "john@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, entity, String.class);
        JsonNode json = mapper.readTree(response.getBody());

        String webhook = json.get("webhook ").asText();
        String token = json.get("accessToken ").asText();
        JsonNode users = json.get("data").get("users");

        List<List<Integer>> outcome = findMutualFollowers(users);
        Map<String, Object> result = new HashMap<>();
        result.put("regNo ", "REG12347");
        result.put("outcome ", outcome);

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        postHeaders.set("Authorization", token);
        HttpEntity<String> postEntity = new HttpEntity<>(mapper.writeValueAsString(result), postHeaders);

        int attempts = 0;
        boolean success = false;
        while (attempts < 4 && !success) {
            try {
                ResponseEntity<String> postResponse = restTemplate.postForEntity(webhook, postEntity, String.class);
                success = postResponse.getStatusCode().is2xxSuccessful();
            } catch (Exception e) {
                attempts++;
                Thread.sleep(1000);
            }
        }
    }

    private List<List<Integer>> findMutualFollowers(JsonNode users) {
        Map<Integer, Set<Integer>> followMap = new HashMap<>();
        for (JsonNode user : users) {
            int id = user.get("id").asInt();
            JsonNode followsNode = user.get("follows ");
            Set<Integer> follows = new HashSet<>();
            for (JsonNode f : followsNode) {
                follows.add(f.asInt());
            }
            followMap.put(id, follows);
        }

        Set<String> seen = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();
        for (int id : followMap.keySet()) {
            for (int f : followMap.get(id)) {
                if (followMap.containsKey(f) && followMap.get(f).contains(id)) {
                    int min = Math.min(id, f);
                    int max = Math.max(id, f);
                    String pair = min + "-" + max;
                    if (!seen.contains(pair)) {
                        seen.add(pair);
                        result.add(Arrays.asList(min, max));
                    }
                }
            }
        }
        return result;
    }
}