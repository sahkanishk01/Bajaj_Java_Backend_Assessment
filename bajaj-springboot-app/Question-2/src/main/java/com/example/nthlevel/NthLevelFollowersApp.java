package com.example.nthlevel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class NthLevelFollowersApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NthLevelFollowersApp.class, args);
    }

    @Override
    public void run(String... args) {
        RestTemplate restTemplate = new RestTemplate();

        String registerUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"name\": \"John Doe\", \"regNo \": \"REG12347\", \"email \": \"john@example.com\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(registerUrl, entity, Map.class);
        Map<String, Object> body = response.getBody();

        String webhook = (String) body.get("webhook ");
        String accessToken = (String) body.get("accessToken ");
        Map<String, Object> data = (Map<String, Object>) body.get("data");

        Map<String, Object> usersData = (Map<String, Object>) data.get("users");
        int n = (int) usersData.get("n");
        int findId = (int) usersData.get("findId");

        List<Map<String, Object>> usersList = (List<Map<String, Object>>) usersData.get("users");

        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (Map<String, Object> user : usersList) {
            Integer id = (Integer) user.get("id");
            List<Integer> follows = (List<Integer>) user.get("follows ");
            graph.put(id, follows);
        }

        Set<Integer> result = bfsNthLevel(graph, findId, n);

        Map<String, Object> output = new HashMap<>();
        output.put("regNo ", "REG12347");
        output.put("outcome ", new ArrayList<>(result));

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        postHeaders.set("Authorization", accessToken);

        HttpEntity<Map<String, Object>> postEntity = new HttpEntity<>(output, postHeaders);

        for (int i = 0; i < 4; i++) {
            try {
                restTemplate.postForEntity(webhook, postEntity, String.class);
                break;
            } catch (Exception e) {
                if (i == 3) throw e;
            }
        }
    }

    private Set<Integer> bfsNthLevel(Map<Integer, List<Integer>> graph, int start, int level) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(start);
        visited.add(start);
        int currentLevel = 0;

        while (!queue.isEmpty() && currentLevel < level) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int node = queue.poll();
                for (int neighbor : graph.getOrDefault(node, Collections.emptyList())) {
                    if (!visited.contains(neighbor)) {
                        queue.offer(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
            currentLevel++;
        }
        return new HashSet<>(queue);
    }
}
