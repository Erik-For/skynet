package se.skynet.skywars.perks;

public enum SkywarsPerk {
    /*
    Ender Mastery I-V
Instant Smelting
Mining Expertise I-V
Resistance Boost I-III
Knowledge I-III
Savior I-V
Nourishment
Bridger I-V
Lucky Charm I-XX
Arrow Recovery I-V
Blazing Arrows I-V
Juggernaut I-V
Speed Boost I-V
Annoy-o-mite I-V
Fat I-V
Environmental Expert I-III
Marksmanship
Bulldozer I-V
Necromancer I-V
Black Magic I-V
Robbery I-X
Frost I-XX
Diamondpiercer (Tournament Exclusive)
Barbarian
     */
    ENDER_M(5),
    INSTANT_SMELTING(1),
    MINING_EXPERTISE(5),
    RESISTANCE_BOOST(3),
    KNOWLEDGE(3),
    SAVIOR(5),
    NOURISHMENT(1),
    BRIDGER(5),
    LUCKY_CHARM(20),
    ARROW_RECOVERY(5),
    BLAZING_ARROWS(5),
    JUGGERNAUT(5),
    SPEED_BOOST(5),
    ANNOY_O_MITE(5),
    FAT(5),
    ENVIRONMENTAL_EXPERT(3),
    MARKSMANSHIP(1),
    BULLDOZER(5),
    NECROMANCER(5),
    BLACK_MAGIC(5),
    ROBBERY(10),
    FROST(20),
    DIAMONDPIERCER(1),
    BARBARIAN(1)

    ;
    private final int maxLevel;
    SkywarsPerk(int maxLevel) {
        this.maxLevel = maxLevel;
    }
}