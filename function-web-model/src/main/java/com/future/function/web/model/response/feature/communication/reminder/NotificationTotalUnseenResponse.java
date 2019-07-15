package com.future.function.web.model.response.feature.communication.reminder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: priagung.satyagama
 * Created At: 4:28 PM 7/11/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTotalUnseenResponse {

    private Integer total;

}
