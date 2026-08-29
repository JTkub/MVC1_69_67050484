package model;

import java.util.ArrayList;
import java.util.List;

public class RoleChangeRequest {
    public final String requestId;
    public final Member proposer;
    public final Member target;
    public final Role requestedRole;
    public RequestStatus status;
    public final List<Vote> votes = new ArrayList<>();

    public RoleChangeRequest(String requestId, Member proposer, Member target, Role requestedRole, RequestStatus status) {
        this.requestId = requestId;
        this.proposer = proposer;
        this.target = target;
        this.requestedRole = requestedRole;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public Member getProposer() {
        return proposer;
    }

    public Member getTarget() {
        return target;
    }

    public Role getRequestedRole() {
        return requestedRole;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public int countApproveVotes() {
        int count = 0;
        for (Vote vote : votes) {
            if (vote.getChoice() == VoteChoice.APPROVE) {
                count++;
            }
        }
        return count;
    }

    public int countRejectVotes() {
        int count = 0;
        for (Vote vote : votes) {
            if (vote.getChoice() == VoteChoice.REJECT) {
                count++;
            }
        }
        return count;
    }

    public boolean isPending() {
        return status == RequestStatus.PENDING;
    }

    public boolean hasVoted(String memberId) {
        for (Vote vote : votes) {
            if (vote.getVoter().getMemberId().equalsIgnoreCase(memberId)) {
                return true;
            }
        }
        return false;
    }
}
