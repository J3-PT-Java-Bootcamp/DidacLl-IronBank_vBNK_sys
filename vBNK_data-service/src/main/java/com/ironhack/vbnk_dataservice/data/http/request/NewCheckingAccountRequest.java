package com.ironhack.vbnk_dataservice.data.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Schema(name = "New Checking Account Request")
@Tag(name = "HTTP Requests")
public class NewCheckingAccountRequest extends NewAccountRequest {

}
