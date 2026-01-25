package agh.ics.oop.model.elements;

import agh.ics.oop.model.map.GlobeMap;

public class Parasite extends Animal {
    private NormalAnimal host = null;
    private final int panicLoss;

    public Parasite(agh.ics.oop.model.Vector2d pos, int energy, int genLen, int panicLoss) {
        super(pos, energy, genLen);
        this.panicLoss = panicLoss;
    }

    @Override
    public void move(GlobeMap map) {
        if (host != null) {
            if (host.getEnergy() <= 0) {
                host.removeParasite(this);
                host = null;
                subEnergy(panicLoss * 2);
                performStandardMove(map);
            } else {
                this.pos = map.getParasiteSpot(host.getPosition());
                return;
            }
        }
        subEnergy(panicLoss);
        performStandardMove(map);
    }

    public void setHost(NormalAnimal h) {
        if (this.host != null) this.host.removeParasite(this);
        this.host = h;
        if (h != null) h.addParasite(this);
    }

    public NormalAnimal getHost() { return host; }
    @Override public String getTexturePath() { return "/parasite.png"; }
}