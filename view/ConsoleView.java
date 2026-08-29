package view;

import controller.RequestController;
import model.*;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    public final Scanner scanner = new Scanner(System.in);
    public String currentMemberId;

    public void run(RequestController controller) {
        while (true) {
            String choice = showMenu();
            if (choice.equals("1")) {
                selectMember(controller);
            } else if (choice.equals("2")) {
                showMembers(controller.showMembers());
            } else if (choice.equals("3")) {
                createRequest(controller);
            } else if (choice.equals("4")) {
                voteOnRequest(controller);
            } else if (choice.equals("5")) {
                cancelRequest(controller);
            } else if (choice.equals("6")) {
                showSummary(controller);
            } else if (choice.equals("0")) {
                showMessage("ปิดโปรแกรม");
                return;
            } else {
                showError("เลือกเมนูไม่ถูกต้อง");
            }
        }
    }

    public String showMenu() {
        System.out.println("\n");
        System.out.println("member: " + (currentMemberId == null ? "" : currentMemberId));
        System.out.println("1. Select member");
        System.out.println("2. Show member");
        System.out.println("3. Create role");
        System.out.println("4. Vote request");
        System.out.println("5. Cancel request");
        System.out.println("6. Show request summary");
        System.out.println("0. Exit");
        return read("Choose: ");
    }

    public void selectMember(RequestController controller) {
        showMembers(controller.showMembers());
        String memberId = read("Member ID: ").toUpperCase();
        boolean exists = false;
        for (Member member : controller.showMembers()) {
            if (member.getMemberId().equalsIgnoreCase(memberId)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            showError("ไม่พบสมาชิก");
            return;
        }
        currentMemberId = memberId;
        showMessage("เลือกผู้ใช้งาน " + currentMemberId + " แล้ว");
    }

    public void showMembers(List<Member> members) {
        System.out.println("\n Members");
        for (Member member : members) {
            System.out.printf("%s | %s | %s | Active: %s%n",
                    member.getMemberId(), member.getName(), member.getRole(), member.isActive());
        }
    }

    public void createRequest(RequestController controller) {
        if (!requireCurrentMember()) return;
        String[] input = readRequestInput();
        try {
            showResult(controller.createRequest(currentMemberId, input[0], Role.valueOf(input[1])));
        } catch (IllegalArgumentException exception) {
            showError("บทบาทไม่ถูกต้อง");
        }
    }

    public String[] readRequestInput() {
        String targetId = read("Target member ID: ").toUpperCase();
        String role = read("New role (PRODUCER, FINANCE, EDITOR, CREATOR): ").toUpperCase();
        return new String[]{targetId, role};
    }

    public void voteOnRequest(RequestController controller) {
        if (!requireCurrentMember()) return;
        String[] input = readVoteInput();
        try {
            showResult(controller.voteOnRequest(input[0], currentMemberId, VoteChoice.valueOf(input[1])));
        } catch (IllegalArgumentException exception) {
            showError("ความเห็นไม่ถูกต้อง");
        }
    }

    public String[] readVoteInput() {
        String requestId = read("Request ID: ").toUpperCase();
        String choice = read("Vote (APPROVE, REJECT): ").toUpperCase();
        return new String[]{requestId, choice};
    }

    public void cancelRequest(RequestController controller) {
        if (!requireCurrentMember()) return;
        String requestId = read("Request ID: ").toUpperCase();
        showResult(controller.cancelRequest(requestId, currentMemberId));
    }

    public void showSummary(RequestController controller) {
        System.out.println("\n Request Summary");
        for (RequestStatus status : RequestStatus.values()) {
            System.out.println("[" + status + "]");
            List<RoleChangeRequest> requests = controller.showSummary(status);
            if (requests.isEmpty()) {
                System.out.println("- none");
            }
            for (RoleChangeRequest request : requests) {
                System.out.printf("%s | %s -> %s | New role: %s | Approve: %d | Reject: %d%n",
                        request.getRequestId(), request.getProposer().getMemberId(),
                        request.getTarget().getMemberId(), request.getRequestedRole(),
                        request.countApproveVotes(), request.countRejectVotes());
            }
        }
        showMembers(controller.showMembers());
    }

    public void showResult(String message) {
        System.out.println("Result: " + message);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println("Error: " + message);
    }

    public boolean requireCurrentMember() {
        if (currentMemberId != null) return true;
        showError("กรุณาเลือกผู้ใช้งานก่อน");
        return false;
    }

    public String read(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
