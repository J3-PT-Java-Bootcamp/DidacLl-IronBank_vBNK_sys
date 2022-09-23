package com.ironhack.vbnk_transactionservice.data.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class DailyLog {
    @Id @GenericGenerator(name = "date_based_id_generator",
            strategy = "com.ironhack.vbnk_transactionservice.utils.LogIdGenerator")
    @GeneratedValue(generator = "date_based_id_generator")
    String id;


}
