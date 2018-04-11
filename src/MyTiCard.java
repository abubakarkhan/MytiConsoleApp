import java.util.ArrayList;

public class MyTiCard {

    // some constants
    private final static double CREDIT_LIMIT = 100.0;  // maximum allowed credit
    private final static int LEGAL_MULTIPLE = 5; // multiple that we can re-charge by
    private double credit;
    private String cardID;
    private User user;
    private ArrayList<TravelPass> travelPassArrayList = new ArrayList<>(); // create a structure to keep track of travel passes purchased

    public MyTiCard(double credit, String cardID) {
        this.credit = credit;
        this.cardID = cardID;
    }

    public void addNewTravelPass(TravelPass travelPass){
        travelPassArrayList.add(travelPass);
    }

    /**
     * Check if a pass is an all day pass
     * @return boolean value
     */
    public boolean isAllDayPass(){
        boolean allDayPass = true;
        for (TravelPass travelPass : travelPassArrayList){
            allDayPass = travelPass.isAllDayPass();
        }
        return allDayPass;
    }

    /**
     * Check if a journey is covered by a 2 hr travel pass
     * @param day Day of journey
     * @param timeOfJourney    Time of journey
     * @return Boolean for journey validity
     */
    public boolean validateJourney(String day,int timeOfJourney){
        String travelDay;
        int travelTime;
        boolean validPass = true;
        for (TravelPass travelPass: travelPassArrayList){
            travelDay = travelPass.getDayOfTravel();
            travelTime = travelPass.getTimeOfPurchase();
            if ((travelDay.equalsIgnoreCase(day)) &&
                    ((travelTime + travelPass.getTwoHours()) >= timeOfJourney)){
                validPass = true;
            } else if (!travelPass.isAllDayPass()){
                System.out.println("You do not have a travel pass covering that day/time");
                validPass = false;
            }
        }
        return validPass;
    }
    /**
     * Check if a journey is covered by an all day travel pass
     * @param day Day of journey
     * @return Boolean for journey validity
     */
    public boolean validateJourney(String day){
        String travelDay;
        boolean validPass = true;
        for (TravelPass travelPass: travelPassArrayList){
            travelDay = travelPass.getDayOfTravel();
            if ((travelDay.equalsIgnoreCase(day))){
                validPass = true;
            }else if((!travelDay.equalsIgnoreCase(day))){
                System.out.println("You do not have a travel pass covering that day/time");
                validPass = false;
            }
        }
        return validPass;
    }

    /**
     * Print travel pass records
     */
    public void printTravelPass(){
        int counter = 0;
        for (TravelPass travelPass: travelPassArrayList){
            counter += 1;
            if (travelPass.isAllDayPass()){
                System.out.println(counter + ". All day Zone " + travelPass.getPassZones()
                        + " Travel Pass purchased on " + travelPass.getDayOfTravel()
                        + " at " + travelPass.getTimeOfPurchase());
            }else if (!travelPass.isAllDayPass()){
                System.out.println(counter + ". 2 hour Zone " + travelPass.getPassZones()
                        + " Travel Pass purchased on " + travelPass.getDayOfTravel()
                        + " at " + travelPass.getTimeOfPurchase());
            }
        }
    }

    public ArrayList<TravelPass> getTravelPassArrayList() {return travelPassArrayList;}

    public static double getCreditLimit() {return CREDIT_LIMIT;}

    public static int getLegalMultiple() {return LEGAL_MULTIPLE;}

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public String getCardID() {return cardID;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}
}