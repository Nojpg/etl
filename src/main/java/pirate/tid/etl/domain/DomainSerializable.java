package pirate.tid.etl.domain;

import java.io.Serializable;

/**
 * Created by Nojpg on 28.09.17.
 */
public interface DomainSerializable extends Serializable {

    default String getAccountName(){
        return null;
    }

    default String getTrafficVolume(){
        return null;
    }

    default String getCity(){
        return null;
    }

    default String getStreet(){
        return null;
    }

    default String getHouse(){
        return null;
    }

    default String getDate(){
        return null;
    }

    default String getCustomerName(){
        return null;
    }

    default String getAddress(){
        return null;
    }
}
