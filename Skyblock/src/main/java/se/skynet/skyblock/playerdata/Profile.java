package se.skynet.skyblock.playerdata;

import se.skynet.skyblock.SkyblockPlayer;

import java.util.List;

public class Profile {

    static int maxPlayers = 5;

    private List<PlayerProfile> members;

    private String name;

    private String uuid;

    public Profile(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getUUID() {
        return uuid;
    }

    public List<PlayerProfile> getMembers() {
        return members;
    }

    public PlayerProfile addMember(SkyblockPlayer player) {
        /*
        if(members.size() < maxPlayers) {
            //PlayerProfile profile = new PlayerProfile()
            members.add(profile);
            return profile;
        }
        return null;
         */
        // TODO make this functional
        return null;
    }
}
