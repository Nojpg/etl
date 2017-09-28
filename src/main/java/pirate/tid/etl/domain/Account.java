package pirate.tid.etl.domain;

import lombok.Data;
import lombok.ToString;
//import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nojpg on 28.09.17.
 */
@Entity
@Data
@Table(name = "provider")
public class Account implements DomainSerializable{

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

    public Account(String accountName, String trafficVolume, String address) {
        this.accountName=accountName;
        this.trafficVolume=trafficVolume;
        this.address=address;
    }

    public Account(){}
}
