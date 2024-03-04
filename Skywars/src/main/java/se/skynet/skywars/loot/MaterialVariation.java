package se.skynet.skywars.loot;

public class MaterialVariation {
    private final float min;
    private final float max;
    private final String materialVariation;

    public MaterialVariation(String materialVariation, float min, float max) {
        this.materialVariation = materialVariation;
        this.min = min;
        this.max = max;
    }

    public boolean isInRange(float value) {
        return value > min && value <= max;
    }
    public String getMaterialVariation(String template) {
        return template.replace("{type}", materialVariation);
    }
}
