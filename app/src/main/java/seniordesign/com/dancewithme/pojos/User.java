//package seniordesign.com.dancewithme.pojos;
//
///**
// * Created by ryantemplin on 9/16/15.
// */
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class User {
//
//    private static final String TAG = User.class.getSimpleName();
//
//    private String firstname;
//    private String lastname;
//    private String email;
//    private int userId;
//    private int credit;
//
//    private HashMap<String, GameBet> futureBetsHash = new HashMap<String, GameBet>(); // HashMap of Bets object (key == game_id)
//    private HashMap<String, GameBet> activeBetsHash = new HashMap<String, GameBet>(); // HashMap of Bets object (key == game_id)
//    private HashMap<String, GameBet> completedBetsHash = new HashMap<String, GameBet>(); // HashMap of Bets object (key == game_id)
//
//    public User(int userId, String firstname, String lastname, String email, int credit) {
//        this.userId = userId;
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.email = email;
//        this.credit = credit;
//    }
//
//    public void decrementTotalCredit(int credit){
//        this.credit -= credit;
//    }
//
//    @Override
//    public String toString() {
//        return "Current User: \n\tFirst:" + this.firstname + "\n\tLast:" + this.lastname + "\n\tId:" + this.userId;
//    }
//
//    public String getFirstname() {
//        return firstname;
//    }
//
//    public void setFirstname(String firstname) {
//        this.firstname = firstname;
//    }
//
//    public String getLastname() {
//        return lastname;
//    }
//
//    public void setLastname(String lastname) {
//        this.lastname = lastname;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getCredit() {
//        return this.credit;
//    }
//
//    public void setCredit(int credit) {
//        this.credit = credit;
//    }
//
//    public HashMap<String, GameBet> getFutureBetsHash() {
//        return futureBetsHash;
//    }
//
//    public void setFutureBetsHash(HashMap<String, GameBet> futureBetsHash) {
//        this.futureBetsHash = futureBetsHash;
//    }
//
//    public HashMap<String, GameBet> getActiveBetsHash() {
//        return activeBetsHash;
//    }
//
//    public void setActiveBetsHash(HashMap<String, GameBet> activeBetsHash) {
//        this.activeBetsHash = activeBetsHash;
//    }
//
//    public HashMap<String, GameBet> getCompletedBetsHash() {
//        return completedBetsHash;
//    }
//
//    public void setCompletedBetsHash(HashMap<String, GameBet> completedBetsHash) {
//        this.completedBetsHash = completedBetsHash;
//    }
//}
