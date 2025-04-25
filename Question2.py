from collections import deque

def nth_level_followers(data, reg_no):
    n = data["n"]
    findId = data["findId"]
    users = data["users"]

    graph = {}
    for user in users:
        graph[user["id"]] = user["follows"]

    queue = deque([(findId, 0)])  # (current node, current level)
    visited = set()
    result = []

    while queue:
        current, level = queue.popleft()
        if level == n:
            result.append(current)
            continue
        if current in graph:
            for neighbor in graph[current]:
                if neighbor not in visited:
                    queue.append((neighbor, level + 1))
                    visited.add(neighbor)

    result.sort()

    return {
        "regNo": reg_no,
        "outcome": result
    }

# Example Input
input_data = {
    "n": 2,
    "findId": 1,
    "users": [
        {"id": 1, "name": "Alice", "follows": [2, 3]},
        {"id": 2, "name": "Bob", "follows": [4]},
        {"id": 3, "name": "Charlie", "follows": [4, 5]},
        {"id": 4, "name": "David", "follows": [6]},
        {"id": 5, "name": "Eva", "follows": [6]},
        {"id": 6, "name": "Frank", "follows": []}
    ]
}

reg_no = "REG12347"
output = nth_level_followers(input_data, reg_no)

print(output)
