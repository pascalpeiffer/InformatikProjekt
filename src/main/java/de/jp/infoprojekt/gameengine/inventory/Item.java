package de.jp.infoprojekt.gameengine.inventory;

import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.item.ItemResource;

public class Item {

    private final Type type;

    public Item(Type type) {
        this.type = type;
    }

    public GameResource getResource() {
        return type.getGameResource();
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        AcidGen(ItemResource.AcidGen),
        Amoniak(ItemResource.Amoniak),
        ColaBomb(ItemResource.ColaBomb),
        ColaEmpty(ItemResource.ColaEmpty),
        Destillation(ItemResource.Distillation),
        Electrolysis(ItemResource.Electrolysis),
        FogFluid(ItemResource.FogFluid),
        Hydrogen(ItemResource.Hydrogen),
        Nitrator(ItemResource.Nitrator),
        Glycerin(ItemResource.Glycerin),
        NitricAcid(ItemResource.NitricAcid),
        Oxygen(ItemResource.Oxygen),
        SulfuricAcid(ItemResource.SulfuricAcid);

        private final GameResource gameResource;

        Type(GameResource gameResource) {
            this.gameResource = gameResource;
        }

        public GameResource getGameResource() {
            return gameResource;
        }
    }
}
