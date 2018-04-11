/**
 * Created by AK on 7/09/2016.
 */
public class Station {

    private String stationName;
    private int zone;
    private int journeyStartCounter = 0;
    private int journeyEndCounter = 0;

    public Station(String name, int zone) {
        this.stationName = name;
        this.zone = zone;
    }

    public Station() {
    }

    public String getName() {return stationName;}

    public int getZone() {
        return zone;
    }

    public void incrementJourneyStartCounter() {
        journeyStartCounter += 1;
    }

    public void incrementJourneyEndCounter() {
        journeyEndCounter += 1;
    }
    /**
     * Prints the journey start and end records for a MyTi Card
     * @param startStation Journey Start
     * @param endStation Journey End
     * @param bulletPoint Bullet Points
     * @param travelTime Journey Travel time
     */
    public void printCardTravel(String startStation,String endStation,char bulletPoint,int travelTime){
        System.out.println(bulletPoint + ". " + startStation
                + " to " + endStation + " at " + travelTime);
    }

    /**
     * Prints the statistics of stations and the number of journeys started and ended there
     */

    public void print(){
        System.out.println( stationName + ": "+ journeyStartCounter
                +" journeys started here, "+ journeyEndCounter +" journeys ended here");
    }
}
