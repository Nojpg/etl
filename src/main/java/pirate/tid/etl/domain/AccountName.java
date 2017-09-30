package pirate.tid.etl.domain;

import lombok.Data;

/**
 * Created by Nojpg on 28.09.17.
 */
@Data
public class AccountName implements DomainSerializable {
    private String accountName;
    private String trafficVolume;
    private String city;
    private String street;
    private String house;
}
