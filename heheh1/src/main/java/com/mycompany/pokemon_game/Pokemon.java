/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pokemon_game;

/**
 *
 * @author justi
 */
public class Pokemon {
    private String name;
    private int health = 100;
    private int mana = 50;
    private String[] attacks;
    private int[] manaCost = {10, 15, 20}; // Mana cost for light, normal, and heavy attacks respectively
    private boolean fainted;
    private String imagePath;
    
    public Pokemon(String name, String[] attacks) {
        this.name = name;
        this.attacks = attacks;
        this.fainted = false;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }
    public String getImagePath() {
        return imagePath;
    }

    public boolean attack(Pokemon target, int attackType) {
        if (attackType < 0 || attackType > 2) return false; // Invalid attack type
        if (mana < manaCost[attackType]) return false; // Not enough mana

        mana -= manaCost[attackType];
        if (mana < 0) mana = 0; // Ensure mana doesn't go negative

        // Attack damage logic
        int damage = (attackType + 1) * 10; // Light=10, Normal=20, Heavy=30
        target.health -= damage;
        if (target.health <= 0)target.health = 0;
    
     // Ensure health doesn't go negative

        return true;
    }

    public void regenerateMana() {
        mana += 10; // Regenerate 10 mana
        if (mana > 50) mana = 50; // Max mana cap
    }

    public boolean isFainted() {
        return health == 0;
    }

    @Override
    public String toString() {
        return name + " - Health: " + health + ", Mana: " + mana;
    }
}

