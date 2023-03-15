import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;

public class Bus {
    public static double[][] createDistanceMatrix(){
        double randomNumber;
        double[][] distanceMatrix = new double[40][40];
        int max = 9;
        int min = 0;
        for (int i = 0; i < 40; i++){
            for (int j = i + 1; j < 40; j++){
                randomNumber = Math.random() * (max - min + 1) + min;
                distanceMatrix[i][j] = randomNumber;
                distanceMatrix[j][i] = randomNumber;
            }
        }
        return distanceMatrix;
    }

    public static String[] createNameList() throws FileNotFoundException {
        String[] nameList = new String[40];
        File file = new File(".\\names.txt");
        Scanner sc = new Scanner(file);
        int i = 0;
        while (sc.hasNextLine()) {
            nameList[i] = sc.nextLine();
            i++;
        }
        return nameList;
    }

    public static boolean isSettled(double[][] seatArrangement, int k){
        boolean truthy = false;
        for (int a = 0; a < seatArrangement.length; a++){
            for (int b = 0; b < seatArrangement[0].length; b++){
                if (k == seatArrangement[a][b]){
                    truthy = true;
                }
            }
        }
        return truthy;
    }

    public static ArrayList<Double> findReferences(double[][] seatArrangement, int i, int j){
        ArrayList<Double> references = new ArrayList<>();
        if(i != 0 && j != 0){
            references.add(seatArrangement[i - 1][j - 1]);
        }
        if(i != 0 && j != 3){
            references.add(seatArrangement[i - 1][j + 1]);
        }
        if(i != 0){
            references.add(seatArrangement[i - 1][j]);
        }
        if(j != 0){
            references.add(seatArrangement[i][j - 1]);
        }
        return references;
    }

    public static double[] findTheNearest(double[][] distanceMatrix, double[][] seatArrangement, int i, int j){
        int passenger = -1;
        double minDistance = 40;

        ArrayList<Double> references = findReferences(seatArrangement, i, j);

        for (int k = 0; k < distanceMatrix.length; k++){
            double totalDistance = 0;
            for (int l = 0; l < references.size(); l++){

                totalDistance += distanceMatrix[(references.get(l)).intValue()][k];
            }

            if (totalDistance < minDistance && !isSettled(seatArrangement, k)){
                minDistance = totalDistance;
                passenger = k;
            }
        }

        double[] returnValues = {passenger, minDistance};
        return returnValues;
    }

    public static double[][][] placePassengers(){
        double[][] distanceMatrix = createDistanceMatrix();
        double[][] seatArrangement = new double[10][4];
        double[][] totalDistance = new double[10][4];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                seatArrangement[i][j] = -1;
            }
        }

        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 4; j++){

                if (i == 0 && j == 0) {
                    int max = 39;
                    int min = 0;
                    double firstSeat = Math.random() * (max - min + 1) + min;
                    seatArrangement[i][j] = (int) (firstSeat);
                }
                else{
                    double[] passengerAndMinDistance = findTheNearest(distanceMatrix, seatArrangement, i, j);
                    seatArrangement[i][j] = (int) passengerAndMinDistance[0];
                    totalDistance[i][j] = passengerAndMinDistance[1];
                }
            }

        }
        double[][][] list = {seatArrangement, totalDistance};
        return  list ;
    }

    public static void displaySeatArrangement(double[][]seatArrangement, String[] nameList){
        System.out.println("-".repeat(41) + "PASSENGER LIST" + "-".repeat(41));
        for (int a = 0; a < 10; a++) {
            for (int b = 0; b < 4; b++) {
                int passengerNumber = (int)seatArrangement[a][b];
                String passengerName = nameList[passengerNumber];
                String passengerInfo = passengerNumber + ": " + passengerName;
                if (passengerNumber < 10){
                    passengerInfo = "0" + passengerInfo;
                }
                System.out.print(String.format("|%-23s", passengerInfo));
            }
            System.out.println("|");
        }
        System.out.println();
    }

    public static void displayTotalDistanceMatrix(double[][] totalDistance){
        System.out.println("-".repeat(7) + "DISTANCE MATRIX" + "-".repeat(7));
        for (int a = 0; a < 10; a++) {
            for (int b = 0; b < 4; b++) {
                double distance = totalDistance[a][b];
                String formattedDistance = String.format("%.2f ", distance);
                if (distance < 10){
                    formattedDistance = "0" + formattedDistance;
                }
                System.out.print(String.format("|" + formattedDistance));
            }
            System.out.println("|");
        }
        System.out.println();
    }

    public static void distanceSum(double[][] totalDistance){
        double distanceSum = 0;
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 4; j++){
                distanceSum += totalDistance[i][j];
            }
        }
        System.out.println(String.format("Sum of distances of all passengers: " + "%.2f " , distanceSum));
    }

    public static void main(String[] args) throws FileNotFoundException {
        String[] nameList = createNameList();
        double[][][] liste = placePassengers();
        double[][] seatArrangement = liste[0];
        double[][] totalDistance = liste[1];
        displaySeatArrangement(seatArrangement, nameList);
        displayTotalDistanceMatrix(totalDistance);
        distanceSum(totalDistance);
    }
}