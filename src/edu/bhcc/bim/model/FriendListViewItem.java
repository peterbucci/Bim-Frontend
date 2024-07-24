package edu.bhcc.bim.model;

public class FriendListViewItem {
    private String displayName;
    private User friend;

    public FriendListViewItem(User friend) {
        this.friend = friend;
        this.displayName = friend.getUsername() + " (" + friend.getStatus() + ")";
    }

    public String getDisplayName() {
        return displayName;
    }

    public User getFriend() {
        return friend;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
