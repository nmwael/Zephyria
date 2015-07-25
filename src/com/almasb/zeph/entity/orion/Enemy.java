package com.almasb.zeph.entity.orion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.orion.Attribute.AttributeInfo;

public class Enemy extends GameCharacter {

    private static final long serialVersionUID = -4175008430166158773L;

    public enum EnemyType {
        NORMAL, MINIBOSS, BOSS
    }

    public final EnemyType type;

    private Element element;

    private List<DroppableItem> drops = new ArrayList<>();

    /**
     * Runtime ID of players who attacked this monster
     */
    private transient ArrayList<Integer> attackers = new ArrayList<Integer>();

    public Enemy(int id, String name, String description, String textureName,
            EnemyType type, Element element, int level, AttributeInfo attrs, Experience xp, DroppableItem... drops) {
        super(id, name, description, textureName, GameCharacterClass.MONSTER);
      this.type = type;
      this.element = element;
      this.baseLevel = level;
      this.xp = xp;
      this.drops.addAll(Arrays.asList(drops));

      attributes.put(Attribute.STRENGTH, attrs.str);
      attributes.put(Attribute.VITALITY, attrs.vit);
      attributes.put(Attribute.DEXTERITY, attrs.dex);
      attributes.put(Attribute.AGILITY, attrs.agi);
      attributes.put(Attribute.INTELLECT, attrs.int_);
      attributes.put(Attribute.WISDOM, attrs.wis);
      attributes.put(Attribute.WILLPOWER, attrs.wil);
      attributes.put(Attribute.PERCEPTION, attrs.per);
      attributes.put(Attribute.LUCK, attrs.luc);

      calculateStats();
      setHP((int)getTotalStat(Stat.MAX_HP));   // set current hp/sp to max
      setSP((int)getTotalStat(Stat.MAX_SP));
    }


    public Enemy(Enemy copy) {
        this(copy.id, copy.name, copy.description, copy.textureName, copy.type, copy.element, copy.baseLevel,
                new AttributeInfo().str(copy.getBaseAttribute(Attribute.STRENGTH))
                .vit(copy.getBaseAttribute(Attribute.VITALITY))
                .dex(copy.getBaseAttribute(Attribute.DEXTERITY))
                .agi(copy.getBaseAttribute(Attribute.AGILITY))
                .int_(copy.getBaseAttribute(Attribute.INTELLECT))
                .wis(copy.getBaseAttribute(Attribute.WISDOM))
                .wil(copy.getBaseAttribute(Attribute.WILLPOWER))
                .per(copy.getBaseAttribute(Attribute.PERCEPTION))
                .luc(copy.getBaseAttribute(Attribute.LUCK)), copy.xp, copy.drops.toArray(new DroppableItem[0]));
    }

    public void addAttackerRuntimeID(int runtimeID) {
        if (!attackers.contains(runtimeID)) {
            attackers.add(runtimeID);
        }
    }

    /**
     *
     * @return
     *          runtime IDs of attackers
     */
    public ArrayList<Integer> getAttackers() {
        return attackers;
    }

    /**
     *
     * @param p
     *           The player who landed the killing blow
     * @param players
     *          players who attacked the monster, including the killing person
     * @return
     */
    public void onDeath(Player p, ArrayList<Player> players) {
        //alive = false;

        for (Player attacker : players) {
            // if the killer
            if (p == attacker) {

            }

            for (DroppableItem item : drops) {
                if (GameMath.checkChance(item.dropChance)) {
                    attacker.getInventory().addItem(ObjectManager.getItemByID(item.itemID));
                }
            }

            attacker.incMoney(GameMath.random(this.baseLevel * 100));
            attacker.gainXP(getXP());
        }
    }

    /**
     *
     * @return
     *          Experience object containing base/stat/job xp
     *          for this enemy
     */
    public Experience getXP() {
        return xp;
    }

    @Override
    public Element getWeaponElement() {
        return element;
    }

    @Override
    public Element getArmorElement() {
        return element;
    }

    @Override
    public Entity toEntity() {
        Entity e = Entity.noType();
        e.setProperty("enemy_data", new Enemy(this));
        return e;
    }

    public static class EnemyBuilder {
        private int id;
        private String name;
        private String desc;
        private String texture;
        private EnemyType type = EnemyType.NORMAL;
        private Element element = Element.NEUTRAL;
        private int level = 1;
        private AttributeInfo attributes = new AttributeInfo();
        private Experience xp;
        private DroppableItem[] items = new DroppableItem[0];

        public EnemyBuilder id(int id) {
            this.id = id;
            return this;
        }

        public EnemyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EnemyBuilder description(String description) {
            this.desc = description;
            return this;
        }

        public EnemyBuilder textureName(String textureName) {
            this.texture = textureName;
            return this;
        }

        public EnemyBuilder level(int level) {
            this.level = level;
            return this;
        }

        public EnemyBuilder element(Element element) {
            this.element = element;
            return this;
        }

        public EnemyBuilder type(EnemyType type) {
            this.type = type;
            return this;
        }

        public EnemyBuilder attributes(AttributeInfo attrs) {
            this.attributes = attrs;
            return this;
        }

        public EnemyBuilder xp(Experience xp) {
            this.xp = xp;
            return this;
        }

        public EnemyBuilder drops(DroppableItem... items) {
            this.items = items;
            return this;
        }

        public Enemy build() {
            return new Enemy(id, name, desc, texture, type, element, level, attributes, xp, items);
        }
    }
}