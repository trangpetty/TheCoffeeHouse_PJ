package com.example.thecoffeehouse.entity.user;

public enum MembershipLevel {
    BRONZE("Bronze", 0),
    SILVER("Silver", 10),
    GOLD("Gold", 30),
    DIAMOND("Diamond", 50);

    private final String name;
    private final int minPoints;

    MembershipLevel(String name, int minPoints) {
        this.name = name;
        this.minPoints = minPoints;
    }

    public String getName() {
        return name;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public static MembershipLevel getLevel(int points) {
        MembershipLevel currentLevel = BRONZE;
        for (MembershipLevel level : values()) {
            if (points >= level.getMinPoints()) {
                currentLevel = level;
            }
        }
        return currentLevel; // Trả về cấp độ phù hợp nhất
    }
}

