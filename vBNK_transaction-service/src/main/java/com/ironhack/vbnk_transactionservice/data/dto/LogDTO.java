package com.ironhack.vbnk_transactionservice.data.dto;

import com.ironhack.vbnk_transactionservice.data.dao.DailyLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class LogDTO {
    String id;

    public static LogDTO fromEntity(DailyLog entity){
        return new LogDTO().setId(entity.getId());
    }
}
