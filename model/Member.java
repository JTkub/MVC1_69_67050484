package model;

public class Member {
    public final String memberId;
    public final String name;
    public Role role;
    public final boolean active;

    public Member(String memberId, String name, Role role, boolean active) {
        this.memberId = memberId;
        this.name = name;
        this.role = role;
        this.active = active;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }
}
