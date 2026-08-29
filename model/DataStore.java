package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public final List<Member> members = new ArrayList<>();
    public final List<RoleChangeRequest> requests = new ArrayList<>();
    public final List<Vote> votes = new ArrayList<>();

    public void loadSeedData() throws IOException {
        String json = Files.readString(Path.of("seed_data.json"));

        for (String object : objectsIn(json, "members")) {
            members.add(new Member(value(object, "id"),value(object, "name"),Role.valueOf(value(object, "role")),Boolean.parseBoolean(value(object, "active"))));
        }

        for (String object : objectsIn(json, "role_change_requests")) {
            Member proposer = findMember(value(object, "requester_id"));
            Member target = findMember(value(object, "target_id"));
            requests.add(new RoleChangeRequest(value(object, "id"), proposer, target,Role.valueOf(value(object, "new_role")),RequestStatus.valueOf(value(object, "status"))));
        }

        for (String object : objectsIn(json, "decisions")) {
            RoleChangeRequest request = findRequest(value(object, "request_id"));
            Vote vote = new Vote(findMember(value(object, "member_id")),VoteChoice.valueOf(value(object, "result")));request.addVote(vote);
            votes.add(vote);
        }
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<RoleChangeRequest> getRequests() {
        return requests;
    }

    public Member findMember(String memberId) {
        for (Member member : members) {
            if (member.getMemberId().equalsIgnoreCase(memberId)) {
                return member;
            }
        }
        return null;
    }

    public RoleChangeRequest findRequest(String requestId) {
        for (RoleChangeRequest request : requests) {
            if (request.getRequestId().equalsIgnoreCase(requestId)) {
                return request;
            }
        }
        return null;
    }

    public void addRequest(RoleChangeRequest request) {
        requests.add(request);
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public String nextRequestId() {
        int highestId = 0;
        for (RoleChangeRequest request : requests) {
            String id = request.getRequestId();
            if (id.startsWith("C")) {
                try {
                    int number = Integer.parseInt(id.substring(1));
                    if (number > highestId) {
                        highestId = number;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        int nextId = highestId + 1;
        if (nextId < 10) {
            return "C0" + nextId;
        }
        return "C" + nextId;
    }

    public List<String> objectsIn(String json, String arrayName) {
        int nameIndex = json.indexOf("\"" + arrayName + "\"");
        int start = json.indexOf('[', nameIndex);
        int end = start;
        int depth = 0;
        for (; end < json.length(); end++) {
            if (json.charAt(end) == '[')
                depth++;
            if (json.charAt(end) == ']' && --depth == 0)
                break;
        }

        List<String> objects = new ArrayList<>();
        String arrayText = json.substring(start + 1, end);
        int position = 0;
        while (position < arrayText.length()) {
            int objectStart = arrayText.indexOf('{', position);
            if (objectStart < 0) {
                break;
            }
            int objectEnd = arrayText.indexOf('}', objectStart);
            if (objectEnd < 0) {
                break;
            }
            objects.add(arrayText.substring(objectStart + 1, objectEnd));
            position = objectEnd + 1;
        }
        return objects;
    }

    public String value(String object, String field) {
        String key = "\"" + field + "\"";
        int keyPosition = object.indexOf(key);
        if (keyPosition < 0) {
            throw new IllegalArgumentException("Missing field: " + field);
        }
        int valueStart = object.indexOf(':', keyPosition) + 1;
        while (valueStart < object.length() && Character.isWhitespace(object.charAt(valueStart))) {
            valueStart++;
        }
        if (object.charAt(valueStart) == '"') {
            int valueEnd = object.indexOf('"', valueStart + 1);
            return object.substring(valueStart + 1, valueEnd);
        }
        int valueEnd = object.indexOf(',', valueStart);
        if (valueEnd < 0) {
            valueEnd = object.length();
        }
        return object.substring(valueStart, valueEnd).trim();
    }
}
