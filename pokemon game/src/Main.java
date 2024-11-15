import java.util.Random;
import java.util.Scanner;

class Pokemon {
    String name;
    int hp = 100;
    int mana = 50;
    boolean isFainted = false;

    Pokemon(String name) {
        this.name = name;
    }

    boolean isAlive() {
        return hp > 0;
    }

    void attack(Pokemon target, int damage, int manaCost) {
        if (mana >= manaCost) {
            mana -= manaCost;
            target.hp -= damage;
            System.out.println(name + " attacks " + target.name + " for " + damage + " damage!");
            if (target.hp <= 0) {
                target.isFainted = true;
                target.hp = 0; // Ensure HP does not go below 0
                System.out.println(target.name + " has fainted!");
            }
        } else {
            System.out.println(name + " doesn't have enough mana to attack!");
        }
    }

    void regenMana() {
        mana += 10;
        if (mana > 50) mana = 50;
        System.out.println(name + " regenerates mana! Current mana: " + mana);
    }
}

class Player {
    Pokemon[] pokemons = new Pokemon[3];
    int currentPokemonIndex;

    Player(String[] names) {
        for (int i = 0; i < names.length; i++) {
            pokemons[i] = new Pokemon(names[i]);
        }
    }

    Pokemon getCurrentPokemon() {
        return pokemons[currentPokemonIndex];
    }

    boolean hasAlivePokemon() {
        for (Pokemon p : pokemons) {
            if (p.isAlive()) return true;
        }
        return false;
    }

    // Switch Pokémon using an index
    void switchPokemon(int index) {
        if (index < 0 || index >= pokemons.length || !pokemons[index].isAlive()) {
            System.out.println("Invalid choice! Pokémon is either fainted or out of range.");
            return;
        }
        currentPokemonIndex = index;
        System.out.println("Switched to " + pokemons[currentPokemonIndex].name);
    }

    // Interactive Pokémon switching for player
    void switchPokemon() {
        if (!hasAlivePokemon()) {
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a Pokémon to switch to:");
        for (int i = 0; i < pokemons.length; i++) {
            if (pokemons[i].isAlive()) {
                System.out.println(i + ": " + pokemons[i].name + " (HP: " + pokemons[i].hp + ")");
            }
        }
        int choice;
        while (true) {
            choice = scanner.nextInt();
            if (choice >= 0 && choice < pokemons.length && pokemons[choice].isAlive()) {
                break;
            }
            System.out.println("Invalid choice! Please choose a Pokémon that is still alive.");
        }
        switchPokemon(choice); // Use the overloaded method
    }
}

public class Main{
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    public static void main(String[] args) {
        // Initialize Player's Pokémon
        String[] userPokemonNames = {"Bulbasaur", "Pikachu", "Squirtle"};
        Player user = new Player(userPokemonNames);
        user.switchPokemon();

        // Initialize Bot's Pokémon
        String[] botPokemonNames = {"Botachu", "Botizard", "Botasaur"};
        Player bot = new Player(botPokemonNames);
        bot.currentPokemonIndex = random.nextInt(3); // Randomly select bot's starting Pokémon
        System.out.println("Bot chose " + bot.getCurrentPokemon().name + " as its starting Pokémon!");

        // Game loop
        while (user.hasAlivePokemon() && bot.hasAlivePokemon()) {
            playerTurn(user, bot);
            if (!bot.hasAlivePokemon()) break;
            botTurn(bot, user);
        }

        // Display the winner
        if (user.hasAlivePokemon()) {
            System.out.println("Congratulations! You won the game!");
        } else {
            System.out.println("Game Over! You lost to the bot.");
        }
    }

    static void playerTurn(Player user, Player bot) {
        if (!user.getCurrentPokemon().isAlive()) {
            System.out.println(user.getCurrentPokemon().name + " has fainted! Choose another Pokémon.");
            user.switchPokemon();
        }

        System.out.println("\nYour turn:");
        Pokemon userPokemon = user.getCurrentPokemon();
        Pokemon botPokemon = bot.getCurrentPokemon();

        System.out.println("Your Pokémon: " + userPokemon.name + " (HP: " + userPokemon.hp + ", Mana: " + userPokemon.mana + ")");
        System.out.println("Opponent's Pokémon: " + botPokemon.name + " (HP: " + botPokemon.hp + ")");

        System.out.println("Choose an action:");
        System.out.println("1. Attack");
        System.out.println("2. Switch Pokémon");
        System.out.println("3. Regenerate Mana");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Choose your attack:");
                System.out.println("1. Light Attack (10 damage, 5 mana)");
                System.out.println("2. Medium Attack (20 damage, 10 mana)");
                System.out.println("3. Heavy Attack (30 damage, 20 mana)");
                int attackChoice = scanner.nextInt();
                switch (attackChoice) {
                    case 1:
                        userPokemon.attack(botPokemon, 10, 5);
                        break;
                    case 2:
                        userPokemon.attack(botPokemon, 20, 10);
                        break;
                    case 3:
                        userPokemon.attack(botPokemon, 30, 20);
                        break;
                    default:
                        System.out.println("Invalid attack choice!");
                }
                break;
            case 2:
                user.switchPokemon();
                return; // End the turn after switching
            case 3:
                userPokemon.regenMana();
                break;
            default:
                System.out.println("Invalid action choice!");
        }
    }

    static void botTurn(Player bot, Player user) {
        Pokemon botPokemon = bot.getCurrentPokemon();
        if (!botPokemon.isAlive()) {
            System.out.println(botPokemon.name + " has fainted! Bot is switching Pokémon.");
            int switchChoice;
            do {
                switchChoice = random.nextInt(3);
            } while (!bot.pokemons[switchChoice].isAlive());
            bot.switchPokemon(switchChoice);
        }

        System.out.println("\nBot's turn:");
        Pokemon userPokemon = user.getCurrentPokemon();

        int action = random.nextInt(100);
        if (action < 80) {
            int attackType = random.nextInt(3);
            switch (attackType) {
                case 0:
                    botPokemon.attack(userPokemon, 10, 5); // Light Attack
                    break;
                case 1:
                    botPokemon.attack(userPokemon, 20, 10); // Medium Attack
                    break;
                case 2:
                    botPokemon.attack(userPokemon, 30, 20); // Heavy Attack
                    break;
            }
        } else if (action < 90) {
            int switchChoice;
            do {
                switchChoice = random.nextInt(3);
            } while (!bot.pokemons[switchChoice].isAlive());
            bot.switchPokemon(switchChoice);
        } else {
            botPokemon.regenMana();
        }
    }
}
