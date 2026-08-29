package controller;

import model.*;

import java.util.List;

public class RequestController {
    public final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    public List<Member> showMembers() {
        return requestService.getMembers();
    }

    public String createRequest(String proposerId, String targetId, Role role) {
        return requestService.createRequest(proposerId, targetId, role);
    }

    public String voteOnRequest(String requestId, String voterId, VoteChoice choice) {
        return requestService.addVote(requestId, voterId, choice);
    }

    public String cancelRequest(String requestId, String proposerId) {
        return requestService.cancelRequest(requestId, proposerId);
    }

    public List<RoleChangeRequest> showSummary(RequestStatus status) {
        return requestService.getRequestGroups(status);
    }
}
