package com.jpa.model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.*;
import java.io.Serializable;

//标记这个类是实体类
@Entity
//标记这个实体类跟数据库表对应起来
@Table(name = "T_MENU")
@Builder
@Data
//打印tostring方法，里面为callSuper = true可以把父类也一起打印出来
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Coffee extends BaseEntity implements Serializable {

    private String name;

    //标记跟表里面的字段关联
    @Column
    @Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode",value = "CNY")})
    private Money price;
}
