package com.future.function.common.properties.communication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Author: PriagungSatyagama
 * Created At: 13:27 07/07/2019
 */
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("reminder")
public class ReminderProperties {

  private long schedulerPeriod;

}
