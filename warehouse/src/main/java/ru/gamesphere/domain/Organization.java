package ru.gamesphere.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Organization {
  private Long organizationId;
  private String inn;
  private String name;
  private String accountNumber;
}
