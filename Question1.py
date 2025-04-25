def find_mutual_followers(data, reg_no):
    follows_map = {}
    mutual_pairs = set()

    # Create a map of who follows whom
    for user in data["users"]:
        user_id = user["id"]
        follows_map[user_id] = set(user["follows"])

    # Check for mutual follow relationships
    for user_id in follows_map:
        for followed_id in follows_map[user_id]:
            if followed_id in follows_map and user_id in follows_map[followed_id]:
                pair = tuple(sorted([user_id, followed_id]))
                mutual_pairs.add(pair)

    result = {
        "regNo": reg_no,
        "outcome": [list(pair) for pair in sorted(mutual_pairs)]
    }

    return result

# Example Input
input_data = {
    "users": [
        {"id": 1, "name": "Alice", "follows": [2, 3]},
        {"id": 2, "name": "Bob", "follows": [1]},
        {"id": 3, "name": "Charlie", "follows": [4]},
        {"id": 4, "name": "David", "follows": [3]}
    ]
}

# Call the function
reg_no = "REG12347"
output = find_mutual_followers(input_data, reg_no)

# Print result
print(len(output["outcome"]))
print(output)
