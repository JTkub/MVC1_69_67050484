package model;

public class Vote {
    public final Member voter;
    public final VoteChoice choice;

    public Vote(Member voter, VoteChoice choice) {
        this.voter = voter;
        this.choice = choice;
    }

    public Member getVoter() {
        return voter;
    }

    public VoteChoice getChoice() {
        return choice;
    }
}
