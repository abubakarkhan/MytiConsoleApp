/**
 * Created by AK on 7/09/2016.
 */
public class TravelPass {

    private String dayOfTravel;
    private int timeOfPurchase = -1;
    private final static int TWO_HOURS = 200;
    private String cardID;
    private boolean allDayPass;
    private String passZones;

    public TravelPass(String dayOfTravel, int timeOfPurchase, String cardID,boolean allDayPass ,String passZones) {
        this.dayOfTravel = dayOfTravel;
        this.timeOfPurchase = timeOfPurchase;
        this.cardID = cardID;
        this.allDayPass = allDayPass;
        this.passZones = passZones;
    }

    public String getDayOfTravel() {return dayOfTravel;}

    public int getTimeOfPurchase() {return timeOfPurchase;}

    public static int getTwoHours() {return TWO_HOURS;}

    public boolean isAllDayPass() {return allDayPass;}

    public String getPassZones() {return passZones;}
}


