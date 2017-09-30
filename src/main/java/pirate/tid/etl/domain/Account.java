package pirate.tid.etl.domain;

import lombok.Data;

import javax.persistence.*;

//import org.springframework.data.annotation.Id;

/**
 * Created by Nojpg on 28.09.17.
 */
@Entity
@Data
@Table(name = "provider")
public class Account implements DomainSerializable {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long account_id;
    //    @Column(name = "account_name")
    private String accountName;
    //    @Column(name = "traffic_volume")
    private String trafficVolume;
    //    @Column(name = "address")
    private String address;

    public Account(Long account_id, String accountName, String trafficVolume, String address) {
        this.account_id = account_id;
        this.accountName = accountName;
        this.trafficVolume = trafficVolume;
        this.address = address;
    }

    public Account(String accountName, String trafficVolume, String address) {
        this.accountName = accountName;
        this.trafficVolume = trafficVolume;
        this.address = address;
    }

    public Account() {
    }
}
