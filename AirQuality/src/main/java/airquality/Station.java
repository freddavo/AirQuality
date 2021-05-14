package airquality;
public class Station {
    private String city;
    private int id;


    public Station(int id, String city){
        this.id = id;
        this.city = city; }


    public String getCity(){
        return city; }
    public void setCity(String newCity) {
        this.city = newCity; }


    public int getID() {
        return id; }
    public void setID(int id){
        this.id = id; }


}