package madelyntav.c4q.nyc.chipchop.DBObjects;

import java.util.Comparator;

/**
 * Created by c4q-madelyntavarez on 9/13/15.
 */
public class PriceSorter implements Comparator<Seller> {
    int returnVal;
    @Override
    public int compare(Seller one, Seller another) {
        if(Double.valueOf(one.getDistanceFromBuyer()) < Double.valueOf(another.getDistanceFromBuyer())){
            returnVal =  -1;
        }else if(Double.valueOf(one.getDistanceFromBuyer()) > Double.valueOf(another.getDistanceFromBuyer())){
            returnVal =  1;
        }else if(Double.valueOf(one.getDistanceFromBuyer()) == Double.valueOf(another.getDistanceFromBuyer())){
            returnVal =  0;
        }
        return returnVal;
    }
}
