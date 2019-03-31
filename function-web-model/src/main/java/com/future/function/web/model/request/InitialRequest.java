package com.future.function.web.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
//@AllArgsConstructor // Commented as no field available; clashes with
// @NoArgsConstructor annotation
public class InitialRequest {}
