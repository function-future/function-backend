package com.future.function.service.api.feature.batch;

import com.future.function.model.entity.feature.batch.Batch;

public interface BatchService {

  Batch findByNumber(long number);

}
