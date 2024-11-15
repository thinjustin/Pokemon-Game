import java.util.Scanner;

class NumberSorter {
    // Private array of numbers
    private int[] numbers = new int[5];

    // Method to set the numbers (encapsulation)
    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    // Method to sort the numbers (bubble sort)
    public void sortNumbers() {
        // Bubble sort algorithm
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = 0; j < numbers.length - 1 - i; j++) {
                if (numbers[j] > numbers[j + 1]) {
                    // Swap numbers[j] and numbers[j + 1]
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
            }
        }
    }

    // Method to display the sorted numbers
    public void displaySortedNumbers() {
        System.out.println("Sorted numbers (lowest to highest):");
        for (int number : this.numbers) {
            System.out.print(number + " ");
        }
        System.out.println(); // For new line after displaying
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NumberSorter sorter = new NumberSorter();

        // Array to store user inputs
        int[] userInputNumbers = new int[5];

        // Getting input from the user with error checking
        System.out.println("Please enter 5 integers:");

        for (int i = 0; i < 5; i++) {
            System.out.print("Enter number " + (i + 1) + ": ");
            try {
                // Attempt to read an integer
                userInputNumbers[i] = scanner.nextInt();
            } catch (Exception e) {
                // If a non-integer input is detected, terminate the program
                System.out.println("Error: You entered an invalid value. Program will now terminate.");
                return; // Exit the program
            }
        }

        // Encapsulation: setting numbers, sorting, and displaying
        sorter.setNumbers(userInputNumbers);
        sorter.sortNumbers();
        sorter.displaySortedNumbers();
    }
}
