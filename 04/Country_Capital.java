/**
 * Country_Capital - 2D Array Country and Capital Mapping.
 * Assignment 04: Slide 13 Exercise.
 */
public class Country_Capital {

    public static void main(String[] args){
        // Row 0 = countries, Row 1 = capitals
        String[][] country_capital_matrix = {
            {"America", "England", "Japan", "France", "Indonesia", "Iran", "Iraq"},
            {"Washington", "London", "Tokyo", "Paris", "Jakarta", "Tehran", "Baghdad"}
        };

        // Deterministic flat loop across matrix columns
        int total_pairs = country_capital_matrix[0].length;
        for(int i = 0; i < total_pairs; i++){
            String country = country_capital_matrix[0][i];
            String capital = country_capital_matrix[1][i];
            System.out.println("The capital of " + country + " is " + capital);
        }
    }
}
