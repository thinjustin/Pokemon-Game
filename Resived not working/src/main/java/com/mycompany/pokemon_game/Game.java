/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.pokemon_game;
import javax.swing.ImageIcon;
import java.awt.Image;

/**
 *
 * @author justi
 */

public class Game extends javax.swing.JFrame {
    private Pokemon currentPlayerPokemon;
    private Pokemon currentBotPokemon;
    
    private final String[] bulbasaurAttacks = {"Seed Bomb", "Sludge Bomb", "Power Whip"};
    private final String[] squirtleAttacks = {"Aqua Jet", "Aqua Tail", "Water Pulse"};
    private final String[] charmanderAttacks = {"Flamethrower", "Flame Charge", "Flame Burst"};
    
    // Player's and Bot's Pokémon
    private Pokemon[] playerPokemons = {
        new Pokemon("Bulbasaur", bulbasaurAttacks),
        new Pokemon("Squirtle", squirtleAttacks),
        new Pokemon("Charmander", charmanderAttacks)
    };
    private Pokemon[] botPokemons = {
        new Pokemon("Bulbasaur", bulbasaurAttacks),
        new Pokemon("Squirtle", squirtleAttacks),
        new Pokemon("Charmander", charmanderAttacks)
    };
    private int playerPokemonIndex = 0;
    private int botPokemonIndex = 0;
    private boolean playerTurn = true;

    public Game(Pokemon currentPlayerPokemon) {
        this.currentPlayerPokemon = currentPlayerPokemon;
        this.currentBotPokemon = generateRandomBotPokemon(); // Optional: Bot chooses a random Pokémon
        initComponents();

        // Display player's chosen Pokémon name
        if (this.currentPlayerPokemon != null) {
            jLabel1.setText("Player's Pokémon: " + this.currentPlayerPokemon.getName());
            setAttackButtons();
            updateUI();
        }
        
    }
    
    private Pokemon generateRandomBotPokemon() {
        // Example of bot randomly choosing a Pokémon
        String[] botPokemons = {"Bulbasaur", "Squirtle", "Charmander"};
        int randomIndex = (int) (Math.random() * botPokemons.length);
        switch (botPokemons[randomIndex]) {
            case "Bulbasaur":
                return new Pokemon("Bulbasaur", new String[]{"Seed Bomb", "Sludge Bomb", "Power Whip"});
            case "Squirtle":
                return new Pokemon("Squirtle", new String[]{"Aqua Jet", "Aqua Tail", "Water Pulse"});
            case "Charmander":
                return new Pokemon("Charmander", new String[]{"Flamethrower", "Flame Charge", "Flame Burst"});
            default:
                return null;
        }
    }
    public Game() {
        initComponents();
    }
    
    private void setAttackButtons() {
    String selectedPokemon = currentPlayerPokemon.getName();
    if ("Bulbasaur".equals(selectedPokemon)) {
        jButton1.setText(bulbasaurAttacks[0]);
        jButton2.setText(bulbasaurAttacks[1]);
        jButton3.setText(bulbasaurAttacks[2]);
    } else if ("Squirtle".equals(selectedPokemon)) {
        jButton1.setText(squirtleAttacks[0]);
        jButton2.setText(squirtleAttacks[1]);
        jButton3.setText(squirtleAttacks[2]);
    } else if ("Charmander".equals(selectedPokemon)) {
        jButton1.setText(charmanderAttacks[0]);
        jButton2.setText(charmanderAttacks[1]);
        jButton3.setText(charmanderAttacks[2]);
    }
    // Enable buttons if Pokémon is not fainted
    jButton1.setEnabled(!currentPlayerPokemon.isFainted());
    jButton2.setEnabled(!currentPlayerPokemon.isFainted());
    jButton3.setEnabled(!currentPlayerPokemon.isFainted());
}

    private void updateUI() {
    jLabel1.setText(currentPlayerPokemon.getName() + " HP: " + currentPlayerPokemon.getHealth() + " | Mana: " + currentPlayerPokemon.getMana());
    jLabel3.setText(currentBotPokemon.getName() + " HP: " + currentBotPokemon.getHealth() + " | Mana: " + currentBotPokemon.getMana());
    
    // Disable attack buttons if player's Pokémon is fainted
    boolean playerPokemonAlive = !currentPlayerPokemon.isFainted();
    jButton1.setEnabled(playerPokemonAlive);
    jButton2.setEnabled(playerPokemonAlive);
    jButton3.setEnabled(playerPokemonAlive);
}
private void performPlayerAttack(int attackType) {
    if (!playerTurn || currentPlayerPokemon.isFainted()) return;

    boolean success = currentPlayerPokemon.attack(currentBotPokemon, attackType);
    if (success) {
        if (currentBotPokemon.isFainted()) {
            switchToNextAvailableBotPokemon();
        }
        if (areAllPokemonsFainted(botPokemons)) {
            // Bot has no Pokémon left
            declareWinner("Player");
            return; // End the game
        }
        playerTurn = false;
        botTurn();
    }
    updateUI();

    // After the attack, check if the player's Pokémon fainted and switch if necessary
    if (currentPlayerPokemon.isFainted()) {
        switchToNextAvailablePlayerPokemon();  // Switch to next available Pokémon
    }
}
    
    private void botTurn() {
    if (currentBotPokemon.isFainted()) {
        // Bot switches to a new Pokémon if the current one is fainted
        switchToNextAvailableBotPokemon();
    } else {
        int choice = (int) (Math.random() * 3);

        // Check for valid actions
        if (choice == 0 && currentBotPokemon.getMana() >= 10) {
            // Bot performs a random attack
            int attackType = (int) (Math.random() * 3);
            currentBotPokemon.attack(currentPlayerPokemon, attackType);
        } else if (choice == 1) {
            // Bot regenerates mana
            currentBotPokemon.regenerateMana();
        } else {
            // Bot switches Pokémon
            switchToNextAvailableBotPokemon();
        }
    }

    if (areAllPokemonsFainted(playerPokemons)) {
        // Player has no Pokémon left
        declareWinner("Bot");
        return; // End the game
    }

    playerTurn = true;
    updateUI();
    
    // After bot's turn, check if the player’s Pokémon is fainted
    if (currentPlayerPokemon.isFainted()) {
        switchToNextAvailablePlayerPokemon();  // Switch to next available Pokémon
    }
}

    private void switchToNextAvailableBotPokemon() {
    for (int i = 0; i < botPokemons.length; i++) {
        botPokemonIndex = (botPokemonIndex + 1) % botPokemons.length;
        currentBotPokemon = botPokemons[botPokemonIndex];
        if (!currentBotPokemon.isFainted()) {
            break;
        }
    }
    updateUI();
}
    private void switchToNextAvailablePlayerPokemon() {
    boolean switched = false;
    
    // Loop through the player's Pokémon to find the next available Pokémon that is not fainted
    for (int i = 0; i < playerPokemons.length; i++) {
        playerPokemonIndex = (playerPokemonIndex + 1) % playerPokemons.length;
        currentPlayerPokemon = playerPokemons[playerPokemonIndex];

        // Check if the Pokémon is not fainted (HP > 0) before switching to it
        if (currentPlayerPokemon.getHealth() > 0) {
            switched = true;
            break; // Exit once a valid (non-fainted) Pokémon is found
        }
    }

    if (!switched) {
        // Handle scenario where all player's Pokémon are fainted, for example:
        System.out.println("All your Pokémon are fainted! You lose!");
    }

    updateUI();  // Update the UI after switching
}

    private boolean areAllPokemonsFainted(Pokemon[] pokemons) {
    for (Pokemon pokemon : pokemons) {
        if (!pokemon.isFainted()) {
            return false; // If any Pokémon is not fainted, return false
        }
    }
    return true; // All Pokémon are fainted
}
private void declareWinner(String winner) {
    // Disable all buttons after the game ends
    jButton1.setEnabled(false);
    jButton2.setEnabled(false);
    jButton3.setEnabled(false);
    jButton7.setEnabled(false); // Disable the "Change Pokémon" button
    jButton8.setEnabled(false); // Disable the "Regen Mana" button

    // Display the winner message
    jLabel1.setText(winner + " wins the game!");
    jLabel3.setText(""); // Clear the bot's info
    
    // Optionally, you can add a "Restart" button to reset the game if needed
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("jLabel1");

        jLabel2.setText("                   VS");
        jLabel2.setToolTipText("");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel3.setText("jLabel2");

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("jButton4");
        jButton4.setEnabled(false);

        jButton5.setText("jButton5");
        jButton5.setEnabled(false);

        jButton6.setText("jButton6");
        jButton6.setEnabled(false);

        if (playerTurn) {
            currentPlayerPokemon.regenerateMana();
            playerTurn = false;
            botTurn();
        }
        updateUI();
        jButton7.setText("Change Pokemon");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Regen Mana");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("jButton9");
        jButton9.setEnabled(false);

        jButton10.setText("jButton10");
        jButton10.setEnabled(false);

        jLabel4.setText("jLabel4");

        jLabel5.setText("jLabel5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton7)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addComponent(jButton8)))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10))
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(47, 47, 47))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8)
                    .addComponent(jButton9)
                    .addComponent(jButton10))
                .addGap(226, 226, 226))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         performPlayerAttack(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
         if (playerTurn) {
        Choose chooseFrame = new Choose();
        chooseFrame.setVisible(true);
        this.dispose();
    }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        performPlayerAttack(1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        performPlayerAttack(2);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        if (currentPlayerPokemon.isFainted()) {
        jButton8.setEnabled(false);  // Disable the button
        return; // Exit early if the Pokémon is fainted
    }
        
        if (playerTurn) {
            currentPlayerPokemon.regenerateMana();
            playerTurn = false;
            botTurn();
        }
        updateUI();
    }//GEN-LAST:event_jButton8ActionPerformed
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        {
        java.awt.EventQueue.invokeLater(() -> new Game(null).setVisible(true));
    }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Game().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables
}
