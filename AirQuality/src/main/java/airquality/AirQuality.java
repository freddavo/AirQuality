package airquality;

public class AirQuality {
    private String status;
    private Dados data;
    private long time;


    public AirQuality(String status, Dados data, long time){
        this.status = status;
        this.data = data;
        this.time = time;
    }


    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status; }


    public Dados getData() {
        return data; }
    public void setData(Dados data){
        this.data = data; }


    public long getTime() {
        return time; }
    public void setTime(long time){
        this.time = time; }


}