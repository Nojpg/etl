package pirate.tid.etl.domain;

import lombok.Data;

@Data
public class CustomerName implements DomainSerializable{
    private String customerName;
    private String trafficVolume;
    private String date;
    private String address;
}
