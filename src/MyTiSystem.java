import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Implementation of basic MyTi system outline.
 * A MyTi card can be used to purchase travel passes:
 *     2Hour or All Day, Zone 1 or Zones 1+2.
 * Each type of travel pass has different prices.
 * This program loops through options of purchasing a pass,
 *     topping up the MyTi card, or viewing current credit.
 */
public class MyTiSystem {

    // create a new Scanner from standard input
    private static Scanner input = new Scanner(System.in);
    private static List<MyTiCard> myTiCardArrayList = new ArrayList<>(); // Collection of MyTi Cards
    private static List<Station> stationArrayList = new ArrayList<>(5); // Collection of Stations
    private static List<Station> travelStartList = new ArrayList<>(); // Store Journey start details for a travel pass
    private static List<Station> travelEndList = new ArrayList<>(); // Store journey end details for a travel pass
    private static List<Integer> journeyTime = new ArrayList<>();   // Store journey times to be printed later
    private final static double ZONE_1_2HR_FARE = 3.50; // Zone 1, 2 Hour fare
    private final static double ZONE_1_AND_2_2HR_FARE = 5.00; // Zone 1 & 2, 2 Hour fare
    private final static double ZONE_1_All_DAY_FARE = 6.90; // Zone 1, All day fare
    private final static double ZONE_1_AND_2_All_DAY_FARE = 9.80; // Zone 1 & 2, All day Fare
    private final static int MAX_TIME = 2359; // Maximum valid time
    private static boolean zoneOneAndTwo; // To check the pass validity for zones
    private static Station journeyEndStation = null; // To use in print functions and fee data to collections
    private static Station journeyStartStation = null; // To use in print functions and fee data to collections
    private static Station stationObject = new Station(); // To use in print functions and fee data to collections

    /**
     * main program: this contains the main menu loop
     */
    public static void main(String[] args) {

        // CODE HERE:
        // create a collection of the MyTi passes available to the system
        // for this assignment, you may hard-code a number of passes and their ids
        myTiCardArrayList.add(new MyTiCard(0,"ac"));
        myTiCardArrayList.add(new MyTiCard(0,"bc"));
        myTiCardArrayList.add(new MyTiCard(0,"lc"));
        myTiCardArrayList.add(new MyTiCard(0,"cc"));
        // CODE HERE:
        // create a collection of stations for the system
        stationArrayList.add(new Station("Central",1));
        stationArrayList.add(new Station("Flagstaff",1));
        stationArrayList.add(new Station("Richmond",1));
        stationArrayList.add(new Station("Lilydale",2));
        stationArrayList.add(new Station("Epping",2));
        // for this assignment, you may hard-code a number of stations and their details

        addTestUser(); // Test user (Lawrence Cavedon)

        // main menu loop: print menu, then do something depending on selection
        int option = -1;
        while(option != 0){
            try{
                printMainMenu();
                option = input.nextInt();
                System.out.println();
                // perform correct action, depending on selection
                switch (option) {
                    case 1: purchasePass();
                        break;
                    case 2: recharge();
                        break;
                    case 3: showCredit();
                        break;
                    case 4: try{
                        takeJourney();
                    } catch (NullPointerException npe){
                        System.out.println("No Travel Pass Purchased");
                    }
                        break;
                    case 5: printAllJourneys();
                        break;
                    case 6: showTravelStatistics();
                        break;
                    case 7 : addUserToCard();
                        break;
                    case 0: System.out.println("Goodbye!");  // will quit---do nothing
                        break;
                    default:
                        // if no legal option selected, just go round and choose again
                        System.out.println("Invalid option!");
                }
            }catch (Exception e){
                System.out.println("Invalid Input,Please choose a valid option.");
                input.nextLine();
            }

        }

        // finishing processing ... close the input stream
        input.close();
    }

    /**
     * Print the main menu (you have to modify / add options)
     */
    private static void printMainMenu() {
        System.out.println();
        System.out.println("Select an option:");
        System.out.println("1. Purchase a travel pass");
        System.out.println("2. Recharge your MyTi card");
        System.out.println("3. Show remaining MyTi credit");
        System.out.println("4. Take a Journey using a MyTi card");
        System.out.println("5. Print all Journeys made using all MyTi cards");
        System.out.println("6. Show Station statistics");
        System.out.println("7. Add a new User");
        System.out.println("0. Quit");
        System.out.print("Please make a selection: ");
    }
    /**
     * Print the time menu
     */
    private static void printTimeMenu() {
        System.out.println();
        System.out.println("How long do you need a pass for:");
        System.out.println("a. 2 Hours");
        System.out.println("b. All Day");
        System.out.println("c. cancel");
        System.out.print("Your selection: ");
    }
    /**
     * Print the Zone menu
     */
    private static void printZoneMenu() {
        System.out.println();
        System.out.println("For which zones:");
        System.out.println("a. Zone 1");
        System.out.println("b. Zones 1 and 2");
        System.out.println("c. cancel");
        System.out.print("Your selection: ");
    }

    /**
     * Prints out all of the journeys made with all of the cards
     */
    private static void printAllJourneys(){
        char bulletPoint = 'a';
        for (MyTiCard myTiCard : myTiCardArrayList){
            System.out.println("MyTi Card: " + myTiCard.getCardID());
            if (myTiCard.getUser() != null)
                System.out.println("Owner: " + myTiCard.getUser().getName());
            if (!myTiCard.getTravelPassArrayList().isEmpty()){
                try {
                    myTiCard.printTravelPass();
                } catch (NullPointerException npe){
                    System.out.println("No Travel Pass Purchased for this card");
                }
                try{
                    for (int i = 0; i < travelEndList.size(); i++){
                        stationObject.printCardTravel(travelStartList.get(i).getName(),travelEndList.get(i).getName(),bulletPoint,journeyTime.get(i));
                        bulletPoint += 1;
                    }
                    System.out.println();
                }catch (NullPointerException npe){
                    System.out.print("");
                }
            }
            System.out.println();
        }
    }
    /**
     * Purchases a journey using you travel pass
     */
    private static void takeJourney()throws NullPointerException {
        String cardID = null, userInput, day = null;
        int time = -1;
        boolean validPass = true;
        System.out.print("Enter card ID: ");
        userInput = input.next();
        for (MyTiCard myTiCard : myTiCardArrayList){
            cardID = myTiCard.getCardID();
            if(cardID.equalsIgnoreCase(userInput)){
                if(myTiCard.getTravelPassArrayList().isEmpty()){
                    System.out.println("You do not have a valid travel pass for this card");
                    System.out.println("Please purchase a travel pass to take a journey");
                    return;
                }
                System.out.println();
                System.out.print("What day is your journey: ");
                if (!myTiCard.isAllDayPass()){
                    day = input.next();
                    time = validateTime();
                    validPass = myTiCard.validateJourney(day,time);
                    if (validPass == false){return;}
                    journeyTime.add(time);
                }
                else if (myTiCard.isAllDayPass()){
                    day = input.next();
                    time = validateTime();
                    validPass = myTiCard.validateJourney(day);
                    if (validPass == false){return;}
                    journeyTime.add(time);
                }
                else {
                    return;
                }
                break;
            }
        }

        if (!cardID.equalsIgnoreCase(userInput)){
            System.out.println("Not a Valid Card.");
            return;
        }
        stationInput();
        if (((journeyStartStation.getZone()+journeyEndStation.getZone()) == 3) && !zoneOneAndTwo){
            System.out.println("Your pass does not cover the zone 2 " +
                    "\nPlease purchase a new pass to travel in zone 2" );
            return;
        }
        journeyStartCounter(journeyStartStation.getName());
        journeyEndCounter(journeyEndStation.getName());
        journeyPrintMessage(journeyStartStation,journeyEndStation,day,time);
    }

    /**
     * Prints out the message on successful journey purchase
     * @param startStation Journey start station
     * @param endStation Journey End station
     * @param day Day of travel
     * @param time Time of Journey
     */
    private static void journeyPrintMessage(Station startStation, Station endStation, String day, int time){
        System.out.println("Your journey is for Zone "+ startStation.getZone()+ " + "
                + endStation.getZone()  +" on " + day + " starting at " + time + ". \n" +
                "Enjoy your travel!");
    }

    /**
     * Validate time entered for the journey
     */
    private static int validateTime(){
        boolean validTime = false;
        int time = 0;
        while (!validTime){
            try {
                System.out.print("What time is your Journey: ");
                time = input.nextInt();
                if ((time % 100) > 59 || time > MAX_TIME || time < 0){
                    System.out.println("Please enter a valid time");
                } else {
                    validTime = true;
                }
            }catch (Exception e){
                System.out.println("Please enter a valid time");
                input.nextLine();
            }
        }
        return time;
    }

    /**
     * Print station travel statistics
     */
    public static void showTravelStatistics(){
        System.out.println("Station travel statistics:");
        for (Station station : stationArrayList){
            station.print();
        }
    }

    /**
     * Count the number of journeys starting at a particular station
     * @param stationInput Station object where journey starts
     */
    public static void journeyStartCounter(String stationInput){
        for (Station station : stationArrayList){
            if (stationInput.equalsIgnoreCase(station.getName())){
                station.incrementJourneyStartCounter();
            }
        }
    }

    /**
     * Count the number of journeys ending at a particular station
     * @param stationInput station object where the journey ends
     */
    public static void journeyEndCounter(String stationInput){
        for (Station station : stationArrayList){
            if (stationInput.equalsIgnoreCase(station.getName())){
                station.incrementJourneyEndCounter();
            }
        }
    }

    /**
     * Ask for journey start and end stations validate the input and increment
     * the journey counters accordingly
     */
    private static void stationInput(){
        boolean validInput;
        String stationName = null;
        validInput = true;
        while (validInput) {
            System.out.print("Start Station: ");
            String startStation = input.next();
            for (Station station : stationArrayList) {
                stationName = station.getName();
                if (station.getName().equalsIgnoreCase(startStation)) {
                    journeyStartStation = station;
                    validInput = false;
                    travelStartList.add(station);
                    break;
                }
            }
            if (!startStation.equalsIgnoreCase(stationName))
                System.out.println("Please enter a valid station");
        }
        validInput = true;
        while (validInput) {
            System.out.print("End Station: ");
            String endStation = input.next();
            for (Station station : stationArrayList) {
                stationName = station.getName();
                if (station.getName().equalsIgnoreCase(endStation)) {
                    journeyEndStation = station;
                    validInput = false;
                    travelEndList.add(station);
                    break;
                }
            }
            if (!endStation.equalsIgnoreCase(stationName))
                System.out.println("Please enter a valid station");
        }
    }

    /**
     * Require the day of purchase for the travel pass
     * @return the day of purchase for the travel pass object
     */
    private static String purchaseDay(){
        System.out.println();
        System.out.print("Day of purchase: ");
        String day = input.next();
        return day;
    }

    /**
     * Require the purchase time of travel pass and check for validity
     * @return the purchase time of a travel pass
     */
    private static int purchaseTime(){
        boolean validTime = false;
        int time = 0;
        while (!validTime){
            try {
                System.out.print("Time of purchase: ");
                time = input.nextInt();
                if ((time % 100) > 59 || time > MAX_TIME || time < 0){
                    System.out.println("Please enter a valid time");
                } else {
                    validTime = true;
                }
            }catch (Exception e){
                System.out.println("Please enter a valid time");
                System.out.println();
                input.nextLine();
            }
        }
        return time;
    }
    /**
     * Purchase a travel pass using MyTi credit
     * Check for valid options
     * Deduct proper fare balance
     * Create relevant travel pass objects
     */
    private static void purchasePass() {
        // first, get the MyTi card that we plan to use to purchase Travel Pass
        String userInput, day = null, cardID = null;
        int time = 0;
        System.out.print("What is the id of the MyTi pass: ");
        userInput = input.next();
        // CODE GOES HERE: look up MyTi card matching this id --- if no match, return null
        for(MyTiCard myTiCard : myTiCardArrayList){
            cardID = myTiCard.getCardID();
            if (userInput.equals(cardID)) {
                printTimeMenu();
                String length = input.next();
                if (length .equals("c")) {return;}  // cancel
                else if (length.equals("a")){day = purchaseDay();time = purchaseTime();}
                else if (length.equals("b")){day = purchaseDay();time = purchaseTime();}
                printZoneMenu();
                String zones = input.next();
                if (zones.equals("c")) {return;}    // cancel
                System.out.println();
                // ... else we found a matching MyTi card: continue with purchasing Travel Pass
                // first check if valid options were selected
                if ((!length.equals("a") && !length.equals("b"))
                        || (!zones.equals("a") && !zones.equals("b"))) {
                    System.out.println("You have selected an illegal option. Please try again.");
                    // if not, then re-try purchasing a pass
                    purchasePass();
                } else {
                    // selected legal options --- purchase a Travel Pass on this MyTi card
                    // NOTE: you will need to Check Credit before finalising the purchase!
                    //   --- Raise an Exception if there is not enough credit
                    if (length.equals("a") && zones.equals("a")) {
                        // CODE HERE: purchase a 2 Hour Zone 1 Travel Pass on this MyKi card;
                        if(myTiCard.getCredit() >= ZONE_1_2HR_FARE){
                            double credit = myTiCard.getCredit();
                            credit -= ZONE_1_2HR_FARE;
                            myTiCard.setCredit(credit);
                            System.out.printf("You purchased a 2 Hour pass on " + myTiCard.getCardID() +" for Zones 1 , costing $%.2f\n",ZONE_1_2HR_FARE);
                            System.out.printf("Your remaining credit is $%.2f\n",myTiCard.getCredit());
                            myTiCard.addNewTravelPass(new TravelPass(day,time,myTiCard.getCardID(),false,"1"));
                            zoneOneAndTwo = false;
                        }else
                            System.out.println("Sorry, you don't have enough credit for that selection");
                        return;
                    } else if (length.equals("a") && zones.equals("b")) {
                        // CODE HERE: purchase a 2 Hour Zone 1 & 2 Travel Pass on this MyKi card;
                        if(myTiCard.getCredit() >= ZONE_1_AND_2_2HR_FARE){
                            double credit = myTiCard.getCredit();
                            credit -= ZONE_1_AND_2_2HR_FARE;
                            myTiCard.setCredit(credit);
                            System.out.printf("You purchased a 2 Hour pass on " + myTiCard.getCardID() +" for Zones 1 and 2, costing $%.2f\n",ZONE_1_AND_2_2HR_FARE);
                            System.out.printf("Your remaining credit is $%.2f\n",myTiCard.getCredit());
                            myTiCard.addNewTravelPass(new TravelPass(day,time,myTiCard.getCardID(),false,"1-2"));
                            zoneOneAndTwo = true;
                        }else
                            System.out.println("Sorry, you don't have enough credit for that selection");
                        return;
                    } else if (length.equals("b") && zones.equals("a")) {
                        // CODE HERE: purchase an All Day Zone 1 Travel Pass on this MyKi card;
                        if (myTiCard.getCredit() >= ZONE_1_All_DAY_FARE){
                            double credit = myTiCard.getCredit();
                            credit -= ZONE_1_All_DAY_FARE;
                            myTiCard.setCredit(credit);
                            System.out.printf("You purchased an All Day pass on " + myTiCard.getCardID() +" for Zone 1, costing $%.2f\n",ZONE_1_All_DAY_FARE);
                            System.out.printf("Your remaining credit is $%.2f\n",myTiCard.getCredit());
                            myTiCard.addNewTravelPass(new TravelPass(day,time,myTiCard.getCardID(),true,"1"));
                            zoneOneAndTwo = false;
                        }else
                            System.out.println("Sorry, you don't have enough credit for that selection");
                        return;
                    } else if (length.equals("b") && zones.equals("b")) {
                        // CODE HERE: purchase an All Day Zone 2 Travel Pass on this MyKi card;
                        if (myTiCard.getCredit() >= ZONE_1_AND_2_All_DAY_FARE){
                            double credit = myTiCard.getCredit();
                            credit -= ZONE_1_AND_2_All_DAY_FARE;
                            myTiCard.setCredit(credit);
                            System.out.printf("You purchased an All Day pass on " + myTiCard.getCardID() +" for Zones 1 and 2, costing $%.2f\n",ZONE_1_AND_2_All_DAY_FARE);
                            System.out.printf("Your remaining credit is $%.2f\n",myTiCard.getCredit());
                            myTiCard.addNewTravelPass(new TravelPass(day,time,myTiCard.getCardID(),true,"1-2"));
                            zoneOneAndTwo = true;
                        }else
                            System.out.println("Sorry, you don't have enough credit for that selection");
                        return;
                    }


                }
            }
        }
        if (!userInput.equals(cardID)){
            System.out.println("Not a Valid Card.");
        }
    }

    /**
     * Ask card ID
     * Check for validity
     * Recharge a MyTi card
     */
    private static void recharge() {
        // CODE HERE: Get MyTi card id from user and find matching MyTiCard
        String cardID = null, userInput;
        System.out.print("Enter card ID: ");
        userInput = input.next();
        for (MyTiCard myTiCard : myTiCardArrayList){
            cardID = myTiCard.getCardID();
            if (cardID.equals(userInput)){
                // read charge amount from input Scanner
                boolean recharge = false;
                while (!recharge){
                    System.out.print("How much credit do you want to add: ");
                    double amount = input.nextDouble();
                    if (amount % myTiCard.getLegalMultiple() == 0 && (amount + myTiCard.getCredit()) <= myTiCard.getCreditLimit()){
                        double credit = myTiCard.getCredit();
                        credit += amount;
                        myTiCard.setCredit(credit);
                        System.out.printf("Your credit = $%.2f\n",myTiCard.getCredit());
                        recharge = true;
                    }else if (amount % myTiCard.getLegalMultiple() != 0){
                        System.out.println("Sorry, you can only add multiples of $5.00");
                        System.out.println();
                    }else if ((amount + myTiCard.getCredit()) > myTiCard.getCreditLimit()){
                        System.out.println("Sorry, the max amount of credit allowed is $100.00");
                        System.out.println();
                    }
                }
                break;
            }
            // CODE HERE: add that credit to the MyTiCard
            // - check that it does not go above max amount (raise Exception if it does)
        }
        if (!cardID.equals(userInput))
        System.out.println("Not a Valid Card.");
    }
    /**
     * Display the remaining credit on MyTi card
     */
    private static void showCredit() {
        // CODE HERE: Get MyTi card id from user and find matching MyTiCard
        String cardID = null, userInput;
        System.out.print("Enter card ID: ");
        userInput = input.next();
        for (MyTiCard myTiCard : myTiCardArrayList){
            cardID = myTiCard.getCardID();
            if (cardID.equals(userInput)){
                // CODE HERE: Display credit for that MyTiCard
                System.out.printf("Your credit = $%.2f\n", myTiCard.getCredit());
                break;
            }
        }
        if(!cardID.equals(userInput))
            System.out.println("Not a Valid Card.");
    }
    public static void addTestUser(){
        for (MyTiCard myTiCard : myTiCardArrayList){
            if (myTiCard.getCardID().equalsIgnoreCase("lc")){
                myTiCard.setUser(new User("Lawrence Cavedon","lawrence.cavedon@rmit.edu.au"));
            }
        }
    }

    /**
     * Add a User to previously unregistered card
     */
    public static void addUserToCard(){
        boolean validChoice = false;
        String userInput;
        String userName, userEmail;
        while (!validChoice){
            System.out.print("Choose an id: ");
            userInput = input.next();
            for (MyTiCard myTiCard : myTiCardArrayList){
                if (myTiCard.getCardID().equalsIgnoreCase(userInput)){
                    if (myTiCard.getUser() != null){
                        System.out.println("Sorry, that id is already in use.");
                        break;
                    }else {
                        System.out.print("User Name: " );
                        userName = input.next();
                        System.out.print("User Email: ");
                        userEmail = input.next();
                        myTiCard.setUser(new User(userName,userEmail));
                        validChoice = true;
                    }
                }
            }
        }
    }

}