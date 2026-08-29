package model;

import java.util.List;
import java.util.ArrayList;

public class RequestService {
    public final DataStore dataStore;

    public RequestService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public String createRequest(String proposerId, String targetId, Role role) {
        Member proposer = dataStore.findMember(proposerId);
        Member target = dataStore.findMember(targetId);

        if (proposer == null || target == null) return "ไม่พบสมาชิก";
        if (!proposer.isActive()) return "ผู้เสนอไม่ได้อยู่ในสถานะ Active";
        if (proposer.getMemberId().equalsIgnoreCase(target.getMemberId())) return "ผู้เสนอและสมาชิกเป้าหมายต้องเป็นคนละคน";
        for (RoleChangeRequest request : dataStore.getRequests()) {
            if (request.isPending() && request.getTarget().getMemberId().equalsIgnoreCase(targetId)) {
                return "สมาชิกเป้าหมายมีคำขอที่รอพิจารณาอยู่แล้ว";
            }
        }

        RoleChangeRequest request = new RoleChangeRequest(dataStore.nextRequestId(), proposer, target, role, RequestStatus.PENDING);
        dataStore.addRequest(request);
        return "สร้างคำขอ " + request.getRequestId() + " สำเร็จ (PENDING)";
    }

    public String addVote(String requestId, String voterId, VoteChoice choice) {
        RoleChangeRequest request = dataStore.findRequest(requestId);
        Member voter = dataStore.findMember(voterId);

        if (request == null) return "ไม่พบคำขอ";
        if (voter == null) return "ไม่พบสมาชิก";
        if (!request.isPending()) return "คำขอนี้สิ้นสุดแล้ว ไม่สามารถลงความเห็นได้";
        if (!voter.isActive()) return "สมาชิกไม่ได้อยู่ในสถานะ Active";
        if (request.getProposer().getMemberId().equalsIgnoreCase(voterId)) return "ผู้เสนอไม่มีสิทธิ์ลงความเห็น";
        if (request.getTarget().getMemberId().equalsIgnoreCase(voterId)) return "สมาชิกเป้าหมายไม่มีสิทธิ์ลงความเห็น";
        if (request.hasVoted(voterId)) return "สมาชิกลงความเห็นต่อคำขอนี้แล้ว";

        Vote vote = new Vote(voter, choice);
        request.addVote(vote);
        dataStore.addVote(vote);

        if (request.countApproveVotes() >= 2) {
            request.setStatus(RequestStatus.APPROVED);
            request.getTarget().changeRole(request.getRequestedRole());
            return "อนุมัติแล้ว: " + request.getTarget().getMemberId() + " เปลี่ยนบทบาทเป็น " + request.getRequestedRole();
        }
        if (request.countRejectVotes() >= 2) {
            request.setStatus(RequestStatus.REJECTED);
            return "ไม่อนุมัติ: บทบาทของ " + request.getTarget().getMemberId() + " ไม่เปลี่ยน";
        }
        return "บันทึกความเห็นแล้ว";
    }

    public String cancelRequest(String requestId, String proposerId) {
        RoleChangeRequest request = dataStore.findRequest(requestId);

        if (request == null) return "ไม่พบคำขอ";
        if (!request.isPending()) return "คำขอนี้สิ้นสุดแล้ว ไม่สามารถยกเลิกได้";
        if (!request.getProposer().getMemberId().equalsIgnoreCase(proposerId)) return "เฉพาะผู้เสนอเท่านั้นที่ยกเลิกคำขอได้";
        if (!request.getVotes().isEmpty()) return "ยกเลิกไม่ได้ เพราะมีผู้ลงความเห็นแล้ว";

        request.setStatus(RequestStatus.CANCELLED);
        return "ยกเลิกคำขอ " + request.getRequestId() + " สำเร็จ";
    }

    public List<RoleChangeRequest> getRequestGroups(RequestStatus status) {
        List<RoleChangeRequest> result = new ArrayList<>();
        for (RoleChangeRequest request : dataStore.getRequests()) {
            if (request.getStatus() == status) {
                result.add(request);
            }
        }
        return result;
    }

    public List<Member> getMembers() {
        return dataStore.getMembers();
    }
}
